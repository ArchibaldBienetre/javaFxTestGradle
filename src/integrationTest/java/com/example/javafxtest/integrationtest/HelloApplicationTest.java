package com.example.javafxtest.integrationtest;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.example.javafxtest.HelloApplication;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.ApplicationTest;

//import static org.testfx.assertions.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(ApplicationExtension.class)
public class HelloApplicationTest {

    // headless testing: no UI will pop up, this should work on CI
    @BeforeAll
    public static void setupForHeadlessTesting() throws Exception {
        System.setProperty("monocle.platform", "Headless");
        System.setProperty("testfx.robot", "glass");
        System.setProperty("glass.platform", "Monocle");
        System.setProperty("embedded", "monocle");
        System.setProperty("testfx.headless", "true");
        System.setProperty("prism.order", "sw");
        // System.setProperty("prism.text", "t2k");
        System.setProperty("prism.text", "native");
        System.setProperty("java.awt.headless", "true");
    }

    @BeforeEach
    public void setup() throws Exception {
        ApplicationTest.launch(HelloApplication.class);
    }

    @Test
    public void testButtonClick(FxRobot robot) {
        // arrange
        Button helloButton = robot.lookup("#helloButton").queryButton();
        Label helloLabel = robot.lookup("#welcomeText").queryAs(Label.class);
        assertThat(helloLabel.getText()).isEmpty();

        robot.clickOn(helloButton);

        assertThat(helloLabel.getText())
                .isNotEmpty()
                .isEqualTo("Welcome to this JavaFX Application!");
    }
}
