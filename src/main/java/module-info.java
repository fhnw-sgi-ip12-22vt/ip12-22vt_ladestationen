module ch.ladestation.connectncharge {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    requires org.slf4j;             //slf4j-api-2.0.0-alpha1.jar
    requires org.slf4j.simple;      //slf4j-simple-2.0.0-alpha1.jar & simplelogger.properties
    requires com.pi4j;
    requires com.pi4j.plugin.raspberrypi;
    requires com.pi4j.plugin.pigpio;
    requires com.pi4j.library.pigpio;
    requires java.logging;

    uses com.pi4j.extension.Extension;
    uses com.pi4j.provider.Provider;

    opens ch.ladestation.connectncharge to javafx.fxml;
    exports ch.ladestation.connectncharge.controller;
    opens ch.ladestation.connectncharge.controller to javafx.fxml;
    exports ch.ladestation.connectncharge;
}