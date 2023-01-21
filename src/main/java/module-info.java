module ee.mikk.familyfeudquestionsui {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires lombok;
    requires org.apache.commons.lang3;
    requires com.fasterxml.jackson.databind;

    opens ee.mikk.familyfeudquestionsui to javafx.fxml;
    exports ee.mikk.familyfeudquestionsui;
    exports ee.mikk.familyfeudquestionsui.dto;
}