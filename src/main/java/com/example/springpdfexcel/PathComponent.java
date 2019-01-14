package com.example.springpdfexcel;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;

@Route("path")
public class PathComponent extends Div {
    public PathComponent() {
        setText("Hello @Route!");
    }
}
