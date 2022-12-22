module idavis.c482project {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.opencsv;


    opens model to javafx.fxml;
    exports model;
    exports controller;
    opens controller to javafx.fxml;
}