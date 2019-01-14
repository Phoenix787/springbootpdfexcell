package com.example.springpdfexcel;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.NativeButton;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

import java.io.IOException;

@Route("")
public class Menu extends Div {
    public Menu() {
        String route = UI.getCurrent().getRouter().getUrl(PathComponent.class);
        String routeReport = UI.getCurrent().getRouter().getUrl(MainView.class);
        String routeExcel = UI.getCurrent().getRouter().getUrl(ExcelReport.class);

        Anchor link = new Anchor(route, "Path | ");
        link.setTarget("_blank");
        add(link);

        Anchor reportLink = new Anchor(routeReport, "Спецификация | ");
        reportLink.setTarget("_blank");
        add(reportLink);
//
//        Anchor excelLink = new Anchor(routeExcel, "Спецификация Excel");
//        excelLink.setTarget("_blank");
//        add(excelLink);

//        NativeButton button = new NativeButton("Navigate to spec");
//        button.addClickListener(e->{
//            button.getUI().ifPresent(ui -> ui.navigate("report"));
//        });

        NativeButton button = new NativeButton("Navigate to spec");
        button.addClickListener(event->{
            try {
                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler "+
                        "report2.xls");
            } catch (IOException e) {
                e.printStackTrace();
            };
        });
        add(button);



        //открывается в той же самой вкладке
//        Div menu = new Div();
//        menu.add(new RouterLink("Path", PathComponent.class));
//        menu.add(new RouterLink("Спецификация", MainView.class));
//
//        add(menu);


    }
}
