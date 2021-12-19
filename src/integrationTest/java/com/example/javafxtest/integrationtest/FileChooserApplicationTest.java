package com.example.javafxtest.integrationtest;

import com.example.javafxtest.FileChooserApplication;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.stage.Window;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.ApplicationTest;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

import static org.hamcrest.Matchers.containsString;
import static org.testfx.assertions.api.Assertions.assertThat;

@ExtendWith(ApplicationExtension.class)
public class FileChooserApplicationTest {

    private static final String DUMMY_CSV_FILE_NAME = "dummyCsv.csv";
    private static final String DUMMY_TEX_FILE_NAME = "dummyTex.tex";

    private Button openCsvButton;
    private Label csvFileLabel;
    private Button openTexButton;
    private Label texFileLabel;
    private Button renderPdfButton;

    @BeforeAll
    public static void setupForHeadlessTesting() {
        HeadlessHelper.setupForHeadlessTesting();
    }

    @BeforeEach
    public void setup() throws Exception {
        ApplicationTest.launch(FileChooserApplication.class);

        putDummyFilesIntoUserHome();
    }

    // FXFileChooser always has the user's home as the very first pre-selected item in the search path.
    // As I found it hard to select a different directory, I work around the issue nad put a file into the user home
    private void putDummyFilesIntoUserHome() throws IOException {
        Path userHome = Paths.get(System.getProperty("user.home"));

        File dummyCsv = getDummyFileFromClasspath(DUMMY_CSV_FILE_NAME);
        Path targetCsv = userHome.resolve(DUMMY_CSV_FILE_NAME);
        if (!targetCsv.toFile().exists()) {
            Files.copy(dummyCsv.toPath(), targetCsv);
        }

        File dummyTex = getDummyFileFromClasspath(DUMMY_TEX_FILE_NAME);
        Path targetTex = userHome.resolve(DUMMY_TEX_FILE_NAME);
        if (!targetTex.toFile().exists()) {
            Files.copy(dummyTex.toPath(), targetTex);
        }
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
    void testFileSelection(FxRobot robot) {
        // arrange
        lookUpUiNodes(robot);

        // act
        robot.clickOn(openCsvButton);
        selectFirstMatchingFileInUserHome(robot);

        // assert
        assertThat(openCsvButton).hasText("Pick a CSV file!");
        assertThat(csvFileLabel).hasText(containsString(DUMMY_CSV_FILE_NAME));
        assertThat(csvFileLabel.getTooltip().getText()).contains(DUMMY_CSV_FILE_NAME);
    }

    @Test
    @Disabled("not yet fully implemented")
    void onRenderPdfButtonClick_onMissingCsv_showsAlert(FxRobot robot) {
        // arrange
        lookUpUiNodes(robot);

        // FIXME inject these into the controller
        File nonExistentCsv = new File("nonexistent.csv");
        File dummyTex = getDummyFileFromClasspath(DUMMY_TEX_FILE_NAME);

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
        File dummyCsv = getDummyFileFromClasspath(DUMMY_CSV_FILE_NAME);
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
        File dummyCsv = getDummyFileFromClasspath(DUMMY_CSV_FILE_NAME);
        File nonexistentTex = getDummyFileFromClasspath(DUMMY_TEX_FILE_NAME);

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

    /**
     * With a FXFileChooserDialog open, select a file.
     * <p/>
     * Although FXFileChooser is already much better than plain JavaFX, I still had to work a round a lot of issues.
     * <p/>
     * Find referenced UI elements in filechooser-0.0.6.jar!/net/raumzeitfalle/fx/filechooser/FileChooserView.fxml
     */
    private void selectFirstMatchingFileInUserHome(FxRobot robot) {
        // cannot edit this directly, as it is bound
//        TextField selectedFile = robot.lookup("#selectedFile").queryAs(TextField.class);
//        selectedFile.setEditable(true);
//        selectedFile.setText(dummyCsv.getAbsolutePath());

        // cannot choose the test files directory
        // I think I need to work around this... https://stackoverflow.com/a/40476164/1143126
//        SplitMenuButton chooseDirectory = robot.lookup("#chooser").queryAs(SplitMenuButton.class);
//        robot.clickOn(chooseDirectory);
//        List<MenuItem> items = chooseDirectory.getItems();
//        MenuItem lastItem = chooseDirectory.getItems().get(items.size() - 1);

        chooseCsvInListOfFiles(robot);

        // for some reason, I can't even click "OK"
        // pressOkButton(robot);
    }

    private void chooseCsvInListOfFiles(FxRobot robot) {
        ListView<?> listOfFiles = robot.lookup("#listOfFiles").queryListView();
        // I cannot do this programmatically, it seems
//        listOfFiles.getSelectionModel().selectFirst();
//        listOfFiles.refresh();
//        robot.clickOn(listOfFiles);

        moveMouseToUpperLeftPlusOffset(robot, listOfFiles, 15);
        robot.doubleClickOn(MouseButton.PRIMARY);
    }

    private void pressOkButton(FxRobot robot) {
        Set<Node> allOkButtons = robot.lookup("#okButton").queryAll();
        assertThat(allOkButtons).hasSize(1);
        Node okButton = allOkButtons.iterator().next();
        assertThat(okButton).isEnabled();

        // neither of these move the mouse to the actual button's position
        robot.moveTo(okButton);
        moveMouseToUpperLeftPlusOffset(robot, okButton, 10);

        robot.clickOn(MouseButton.PRIMARY);

        // does not work, either
        robot.clickOn(okButton);
    }

    private void moveMouseToUpperLeftPlusOffset(FxRobot robot, Node node, int offset) {
        Point2D nodeUpperLeft = node.localToScene(0, 0);
        Scene scene = node.getScene();
        Window window = scene.getWindow();
        robot.moveTo(new Point2D(
                nodeUpperLeft.getX() + scene.getX() + window.getX() + offset,
                nodeUpperLeft.getY() + scene.getY() + window.getY() + offset
        ));
    }
}

