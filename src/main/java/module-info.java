module com.example.javafxtest {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;

    requires com.google.common;

    // ! Do not commit this commented-in: It causes issues with integration tests for some reason
    // requires jsr305;
    // requires filechooser;

    opens com.example.javafxtest;
    exports com.example.javafxtest;
}