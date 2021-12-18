package com.example.javafxtest.integrationtest;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.ApplicationTest;

import java.io.File;
import java.net.URL;

import static org.testfx.assertions.api.Assertions.assertThat;

import com.example.javafxtest.FileChooserApplication;

//import javax.annotation.Nullable;

@ExtendWith(ApplicationExtension.class)
public class FileChooserApplicationTest extends AbstractApplicationTest {

    public static final String LAME_EXCUSE = "It seems file choosers are rooted in the concrete OS and can't be controlled nicely using TestFX. " +
            "Let's trust the framework, and, in another test, inject member values so we can test other things.";
    private Button openCsvButton;
    private Label csvFileLabel;
    private Button openTexButton;
    private Label texFileLabel;
    private Button renderPdfButton;

    @BeforeEach
    public void setup() throws Exception {
        ApplicationTest.launch(FileChooserApplication.class);
    }

    /**
     * @param robot - Will be injected by the test runner.
     */
    @Test
    public void testInitialUiTexts(FxRobot robot) {
        // arrange
        lookUpUiNodes(robot);

        // assert
        assertThat(openCsvButton).hasText("Pick a CSV file!");
        assertThat(csvFileLabel).hasText("[CSV file]");
        assertThat(openTexButton).hasText("Pick a TEX file!");
        assertThat(texFileLabel).hasText("[TEX file]");
        assertThat(renderPdfButton).hasText("Render PDFs!");
    }


    @Test
    @Disabled(LAME_EXCUSE)
    // see GtkCommonDialogs: private static native FileChooserResult _showFileChooser
    void testFileSelection(FxRobot robot) {
        // arrange
        lookUpUiNodes(robot);
        File dummyCsv = getDummyFileFromClasspath("dummyCsv.csv");
        File dummyTex = getDummyFileFromClasspath("dummyTex.tex");

        // act
        robot.clickOn(openCsvButton);

        // assert
        assertThat(openCsvButton).hasText("Pick a CSV file!");
        assertThat(csvFileLabel).hasText(dummyCsv.getAbsolutePath());
        assertThat(csvFileLabel.getTooltip().getText()).isEqualTo(dummyCsv.getAbsolutePath());
    }

    @Test
    @Disabled("not yet fully implemented")
    void onRenderPdfButtonClick_onMissingCsv_showsAlert(FxRobot robot) {
        // arrange
        lookUpUiNodes(robot);

        // FIXME inject these into the controller
        File nonExistentCsv = new File("nonexistent.csv");
        File dummyTex = getDummyFileFromClasspath("dummyTex.tex");

        // act
        robot.clickOn(renderPdfButton);

        // assert
        // lookup Alert (it's a dialog), then do assertions
        // https://stackoverflow.com/a/59152238/1143126
        Node dialogPaneNode = robot.lookup(".dialog-pane").query();
        assertThat(dialogPaneNode).isNotNull();
        assertThat(dialogPaneNode).isInstanceOf(DialogPane.class);
        DialogPane dialogPane = (DialogPane) dialogPaneNode;
        assertThat(dialogPane.getHeaderText()).isEqualTo("Error");
        assertThat(dialogPane.getContentText()).contains("does not exist");
        assertThat(dialogPane.getContentText()).contains("CSV");
    }

    @Test
    @Disabled("not yet fully implemented")
    void onRenderPdfButtonClick_onMissingTex_showsAlert(FxRobot robot) {
        // arrange
        lookUpUiNodes(robot);

        // FIXME inject these into the controller
        File dummyCsv = getDummyFileFromClasspath("dummyCsv.csv");
        File nonexistentTex = new File("nonexistent.tex");

        // act
        robot.clickOn(renderPdfButton);

        // assert
        // lookup Alert (it's a dialog), then do assertions
        // https://stackoverflow.com/a/59152238/1143126
        Node dialogPaneNode = robot.lookup(".dialog-pane").query();
        assertThat(dialogPaneNode).isNotNull();
        assertThat(dialogPaneNode).isInstanceOf(DialogPane.class);
        DialogPane dialogPane = (DialogPane) dialogPaneNode;
        assertThat(dialogPane.getHeaderText()).isEqualTo("Error");
        assertThat(dialogPane.getContentText()).contains("does not exist");
        assertThat(dialogPane.getContentText()).contains("TEX");
    }

    @Test
    @Disabled("not yet fully implemented")
    void onRenderPdfButtonClick_withValidFiles_showsNoError(FxRobot robot) {
        // arrange
        lookUpUiNodes(robot);

        // FIXME inject these into the controller
        File dummyCsv = getDummyFileFromClasspath("dummyCsv.csv");
        File nonexistentTex = getDummyFileFromClasspath("dummyTex.tex");

        // act
        robot.clickOn(renderPdfButton);

        // assert
        // no Alert dialog shown
        Node dialogPaneNode = robot.lookup(".dialog-pane").query();
        assertThat(dialogPaneNode).isNull();
    }

    private void lookUpUiNodes(FxRobot robot) {
        openCsvButton = robot.lookup("#openCsvButton").queryButton();
        csvFileLabel = robot.lookup("#csvFileLabel").queryAs(Label.class);
        openTexButton = robot.lookup("#openTexButton").queryButton();
        texFileLabel = robot.lookup("#texFileLabel").queryAs(Label.class);
        renderPdfButton = robot.lookup("#renderPdfButton").queryButton();
    }

    private File getDummyFileFromClasspath(String fileName) {
        URL resource = getClass().getClassLoader().getResource(fileName);
        Assertions.assertThat(resource)
                .describedAs("file not found in classpath: " + fileName)
                .isNotNull();
        return new File(resource.getFile());
    }
}

