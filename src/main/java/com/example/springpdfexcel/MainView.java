package com.example.springpdfexcel;

import com.example.springpdfexcel.entities.Product;
import com.example.springpdfexcel.services.ProductService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.NativeButton;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.page.BodySize;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.StreamResource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Stream;

@Route("report")
@BodySize(height = "100vh", width = "100vw")
public class MainView extends Div implements BeforeEnterObserver {

    @Autowired
    private ProductService productService;

    @Autowired
    private InvoiceService invoiceService;
    private Page page;
    private String path;
    private UI current;

    public MainView() {

//        StreamResource streamResource =
//                new StreamResource("report.pdf", () -> getClass().getResourceAsStream("/report.pdf"));

//        StreamResource streamResource =
//                new StreamResource("report.pdf", () -> getPdfInputStream());

        StreamResource streamResource =
                new StreamResource("report.pdf", () -> export());

        PdfBrowserViewer pdfBrowserViewer = new PdfBrowserViewer(streamResource);
        pdfBrowserViewer.setHeight("100%");
        add(pdfBrowserViewer);
        setHeight("100%");
        com.vaadin.flow.component.html.Anchor download = new com.vaadin.flow.component.html.Anchor(new StreamResource(
                "report.pdf", () -> export()), ""
        );
        download.getElement().setAttribute("download", true);
        download.add(new Button(new Icon(VaadinIcon.DOWNLOAD_ALT)));
        add(download);

        //todo то что ниже нужно дорабатывать
//        Element element = new Element("object");
//        element.setAttribute("type", "application/vnd.ms-excel");
//        NativeButton button = new NativeButton("Generate report");
//        button.addClickListener( event-> {
//            StreamResource streamResource1 = new StreamResource("report.xls",
//                    () -> getClass().getResourceAsStream("/report2.xls"));
//            element.setAttribute("data", streamResource1);
//        });
//
//        add(button);
//
//        UI.getCurrent().getElement().appendChild(element, button.getElement());
    }

    public InputStream getPdfInputStream(){

        List<Product> products = (List<Product>) productService.findAll();

        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {

            PdfPTable table = new PdfPTable(5);
            table.setSpacingBefore(10);
            table.setWidthPercentage(100);
            table.setWidths(new int[]{1, 3, 3, 3, 5});

            BaseFont opensansbold = BaseFont.createFont("open_sans/OpenSans-Bold.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
//            Font fontHeader = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
            Font fontHeader = new Font(opensansbold, 11);
            Font fontH3 = new Font(opensansbold, 15);
            fontHeader.setColor(BaseColor.WHITE);

            BaseFont helvetica = BaseFont.createFont("open_sans/OpenSans-Regular.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

            Font font = new Font(helvetica, 10, Font.NORMAL);

            addTableHeader(table);

            PdfPCell cell = new PdfPCell();
            cell.setPadding(4);
            cell.setBorderWidth(0.2f);
//            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//            cell.setHorizontalAlignment(Element.ALIGN_CENTER);

            for (Product product : products) {

                cell.setPhrase(new Phrase(product.getId().toString(), font));
                table.addCell(cell);

                cell.setPhrase(new Phrase(product.getName(), font));
                table.addCell(cell);

                cell.setPhrase(new Phrase(product.getPrice().toString(), font));
                table.addCell(cell);

                cell.setPhrase(new Phrase(String.valueOf(product.getQuantity()), font));
                table.addCell(cell);

                cell.setPhrase(new Phrase(product.getCategoryName(), font));
                table.addCell(cell);
            }
            Paragraph header = new Paragraph("Отчёт на дату заказа", fontH3);
//            header.setAlignment(Element.ALIGN_CENTER);

            PdfWriter.getInstance(document, out);
            document.open();
            document.add(header);
            document.add(table);
            document.close();


        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
        return new ByteArrayInputStream(out.toByteArray());
    }

    public InputStream export() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        JasperPrint jasperPrint = null;
        try {
            jasperPrint = invoiceService.exportToPDF();
        } catch (IOException | JRException e) {
            e.printStackTrace();
        }
        try {
//            JasperViewer jasperViewer = new JasperViewer(jasperPrint);
//            jasperViewer.setVisible(true);
            JasperExportManager.exportReportToPdfStream(jasperPrint, out);
            invoiceService.exportToXLS(path);

        } catch (JRException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ByteArrayInputStream(out.toByteArray());
    }


    private void addTableHeader(PdfPTable table) {
        try {
            BaseFont opensansbold = BaseFont.createFont("open_sans/OpenSans-Bold.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font fontHeader = new Font(opensansbold, 11);
            fontHeader.setColor(BaseColor.WHITE);
            Stream.of("№", "Имя", "Цена", "Количество", "Категория")
                    .forEach(columnHeader->{
                        PdfPCell header = new PdfPCell();
                        header.setBackgroundColor(BaseColor.GRAY);
                        header.setBorderWidth(0.2f);
//                        header.setHorizontalAlignment(Element.ALIGN_CENTER);
//                        header.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        header.setPadding(4);
                        header.setPhrase(new Phrase(columnHeader, fontHeader));
                        table.addCell(header);
                    });
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        current = UI.getCurrent();
        path = event.getLocation().getPath();

    }
}
