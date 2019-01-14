package com.example.springpdfexcel;

import com.example.springpdfexcel.entities.Product;
import com.example.springpdfexcel.reports.AbstractITextPdfView;
import com.example.springpdfexcel.services.ProductService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public class PdfView extends AbstractITextPdfView {

    @Autowired
    private ProductService productService;

    public PdfView() {


    }

    @Override
    protected void buildPdfDocument(Map<String, Object> model, com.itextpdf.text.Document document, com.itextpdf.text.pdf.PdfWriter writer, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Product> products = (List<Product>) productService.findAll();
        document.add(new Paragraph("PDF create with Spring Framework"));

        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100.0f);
        table.setWidths(new float[]{3.0f, 2.0f, 2.0f, 2.0f});
        table.setSpacingBefore(10);

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(BaseColor.WHITE);

        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(BaseColor.BLUE);
        cell.setPadding(4);

        cell.setPhrase(new Phrase("Name", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Quantity", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Price", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Category", font));
        table.addCell(cell);

        for (Product product : products) {
            table.addCell(product.getName());
            table.addCell(String.valueOf(product.getQuantity()));
            table.addCell(String.valueOf(product.getPrice()));
            table.addCell(product.getCategoryName());
        }

        document.add(table);
    }


}
