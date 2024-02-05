module sn.dev.sponsorship_app {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires TrayNotification;


    opens sn.dev.sponsorship_app to javafx.fxml;
    exports sn.dev.sponsorship_app;
    exports sn.dev.sponsorship_app.controllers;
    opens sn.dev.sponsorship_app.controllers to javafx.fxml;
}