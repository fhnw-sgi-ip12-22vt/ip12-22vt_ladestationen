module ch.ladestation.connectncharge {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens ch.ladestation.connectncharge to javafx.fxml;
    exports ch.ladestation.connectncharge.controller;
    opens ch.ladestation.connectncharge.controller to javafx.fxml;
    exports ch.ladestation.connectncharge;
}