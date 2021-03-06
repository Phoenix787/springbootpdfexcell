package com.example.springpdfexcel;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.page.History;
import com.vaadin.flow.dom.ElementFactory;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.html.NativeButton;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.server.StreamResource;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

import static com.vaadin.flow.dom.ElementFactory.createButton;


@Route("excel-report")
public class ExcelReport extends Div {

    public ExcelReport() {
//        Element element = new Element("object");
//        element.setAttribute("type", "application/x-excel");
//        element.getStyle().set("display", "block");
//
//        Element filler = ElementFactory.createDiv(
//                "Pushing or replacing history state should not affect the scroll position. Scroll down for buttons to click.");
//        filler.getStyle().set("height", "50vh");
//
//        NativeButton button = new NativeButton("Generate report");
//        button.addClickListener( event-> {
//            StreamResource streamResource = new StreamResource("report.xls",
//                    () -> getClass().getResourceAsStream("/report2.xls"));
//            element.setAttribute("data", streamResource);
//        });
//
//        History history = UI.getCurrent().getPage().getHistory();
//        getElement().appendChild(filler, element, button.getElement());

        try {
            Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHeader"+
                    "D:\\homework\\springbootpdfexcel\\springbootpdfexcel\\src\\main\\resources\\report2.xls");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
