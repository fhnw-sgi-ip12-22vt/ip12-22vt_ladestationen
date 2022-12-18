module com.home.connectncharge {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.home.connectncharge to javafx.fxml;
    exports com.home.connectncharge.controller;
    opens com.home.connectncharge.controller to javafx.fxml;
    exports com.home.connectncharge;
}