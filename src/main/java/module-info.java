module com.example.javafxtest {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;

    opens com.example.javafxtest;
    exports com.example.javafxtest;
}