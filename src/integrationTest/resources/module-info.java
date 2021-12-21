module com.example.javafxtest.integrationtest {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;

    requires com.google.common;

    requires org.junit.jupiter.api;
    requires org.testfx.junit5;
    requires org.testfx;
    requires org.assertj.core;

    requires com.example.javafxtest;

    exports com.example.javafxtest.integrationtest;
    opens com.example.javafxtest.integrationtest;
}