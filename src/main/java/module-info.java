module com.example.javafxtest {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;

    opens com.example.javafxtest to javafx.fxml;
    exports com.example.javafxtest;
}