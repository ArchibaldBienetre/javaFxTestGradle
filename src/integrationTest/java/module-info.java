module com.example.javafxtest.integrationtest {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;

    requires com.example.javafxtest;

    exports com.example.javafxtest.integrationtest;

    requires org.junit.jupiter.api;
    requires org.testfx.junit5;
    requires org.testfx;
    requires org.assertj.core;
}