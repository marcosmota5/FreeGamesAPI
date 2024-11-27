module com.app.freegamesapi {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires com.google.gson;
    requires java.net.http;
    requires java.desktop;

    opens com.app.freegamesapi to javafx.fxml;
    exports com.app.freegamesapi;
    opens com.app.freegamesapi.Controllers to javafx.fxml;
    exports com.app.freegamesapi.Controllers to javafx.fxml;
}