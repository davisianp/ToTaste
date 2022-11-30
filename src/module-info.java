module idavis.c482project {
    requires javafx.controls;
    requires javafx.fxml;


    opens model to javafx.fxml;
    exports model;
    exports controller;
    opens controller to javafx.fxml;
}