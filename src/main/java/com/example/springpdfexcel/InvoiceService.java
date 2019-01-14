package com.example.springpdfexcel;

import com.example.springpdfexcel.entities.Product;
import com.example.springpdfexcel.repositories.ProductRepository;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.page.Page;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;
import net.sf.jasperreports.view.JasperViewer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
//import org.springframework.ui.jasperreports.JasperReportsUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Service
public class InvoiceService {
    ProductRepository repository;
//"classpath:jasper/product_template.jrxml"
    //jasper/product_template.jrxml /p_report.jrxml
    private final String invoice_template_path = "/jasper/product_template.jrxml";
    public static final Locale APP_LOCALE = new Locale("ru", "RU");

    @Autowired
    ResourceLoader resourceLoader;

    public InvoiceService(ProductRepository repository) {
        this.repository = repository;
    }

    public void generateInvoiceFor(Product product, Locale locale) throws IOException {

        File pdfFile = File.createTempFile("my-invoice", ".pdf");

        try (FileOutputStream pos = new FileOutputStream(pdfFile)) {

            final JasperReport report = loadTemplate();
//            final Map<String, Object> parameters = parameters(product, locale);
            final JRBeanCollectionDataSource dataSource =
                    new JRBeanCollectionDataSource(Collections.singletonList(repository.findById(product.getId()).get()));

//            JasperReportsUtils.renderAsPdf(report, null, dataSource, pos);

        } catch (final Exception e) {

        }
    }
//JRParameter.REPORT_LOCALE

    public JasperPrint exportToPDF() throws IOException, JRException {

//        //чтобы отображались кирилические символы
//        JRDesignStyle jrDesignStyle = new JRDesignStyle();
//        jrDesignStyle.setDefault(true);
//       // String pathFont = resourceLoader.getResource("classpath:open_sans/OpenSans-Regular.ttf").getURI().getPath();
//        String path = this.getClass().getResource("/open_sans/OpenSans-Regular.ttf").getPath();
//        jrDesignStyle.setPdfFontName(path);
//        jrDesignStyle.setPdfEncoding("Identity-H");
//        jrDesignStyle.setPdfEmbedded(true);

         //  final InputStream reportInputStream = this.getClass().getResourceAsStream(invoice_template_path);
           final InputStream jreport = this.getClass().getResourceAsStream("/jasper/product_template.jasper");
           final JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(repository.findAll());

            //JasperReport jasperReport = JasperCompileManager.compileReport(reportInputStream);
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("REPORT_LOCALE", APP_LOCALE );
//            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jreport, parameters, dataSource);
//        jasperPrint.addStyle(jrDesignStyle);
        return jasperPrint;
    }

    public String exportToHtml(String location) throws JRException {
        final InputStream jreport = this.getClass().getResourceAsStream("/jasper/product_template.jasper");
        final JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(repository.findAll());
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("REPORT_LOCALE", APP_LOCALE );
        JasperPrint jasperPrint = JasperFillManager.fillReport(jreport, parameters, dataSource);
        String destFileName = location + "report2.html";
        JasperExportManager.exportReportToHtmlFile(jasperPrint, destFileName);
        return  destFileName;

    }

    public void exportToXLS(String pathLocation) throws JRException, IOException {
        final InputStream jreport = this.getClass().getResourceAsStream("/jasper/product_template.jasper");
        final JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(repository.findAll());
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("REPORT_LOCALE", APP_LOCALE );
        JasperPrint jasperPrint = JasperFillManager.fillReport(jreport, parameters, dataSource);
        String destFileName = pathLocation + "report2.xls";
        JRXlsExporter exporter = new JRXlsExporter();
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(destFileName));

        SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
        configuration.setOnePagePerSheet(true);
        exporter.setConfiguration(configuration);

        exporter.exportReport();

        File pdfFile = File.createTempFile("my-invoice", ".xls");
        try (FileOutputStream pos = new FileOutputStream(pdfFile)) {

            JRXlsExporter tmpExporter = new JRXlsExporter();
            tmpExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            tmpExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(pos));

            tmpExporter.setConfiguration(configuration);

            tmpExporter.exportReport();

        } catch (final Exception e) {

        }

    }

    private Map<String, Object> parameters(Product product, Locale locale) {
        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("product", product);
        parameters.put("REPORT_LOCALE", locale);
        return parameters;
    }

    private JasperReport loadTemplate() throws JRException {
        final InputStream reportInputStream = getClass().getResourceAsStream(invoice_template_path);
        final JasperDesign jasperDesign = JRXmlLoader.load(reportInputStream);
        return JasperCompileManager.compileReport(jasperDesign);
    }
}
