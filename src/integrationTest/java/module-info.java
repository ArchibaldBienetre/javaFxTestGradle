module com.example.javafxtest.integrationtest {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;

    requires com.google.common;
    requires jsr305; // automatic name

    requires com.example.javafxtest;

    requires org.junit.jupiter.api;
    requires org.testfx.junit5;
    requires org.testfx;
    requires org.assertj.core;

    exports com.example.javafxtest.integrationtest;
}