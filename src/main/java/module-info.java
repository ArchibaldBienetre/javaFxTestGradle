module com.example.javafxtest {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;

    requires com.google.common;

    opens com.example.javafxtest;
    exports com.example.javafxtest;
}