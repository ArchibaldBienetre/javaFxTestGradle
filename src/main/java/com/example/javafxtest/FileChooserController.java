package com.example.javafxtest;

import com.google.common.annotations.VisibleForTesting;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.stage.Window;
import net.raumzeitfalle.fx.filechooser.FXFileChooserStage;
import net.raumzeitfalle.fx.filechooser.PathFilter;
import net.raumzeitfalle.fx.filechooser.Skin;
import net.raumzeitfalle.fx.filechooser.locations.Locations;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public class FileChooserController {

    @FXML
    private Node mainLayout;
    @FXML
    private Label csvFileLabel;
    @FXML
    private Label texFileLabel;

    private File texFile;
    private File csvFile;


    @FXML
    public void onOpenTexButtonClick(ActionEvent ignored) {
        FXFileChooserStage texChooser = getTexChooser();
        Optional<Path> selectedFile = texChooser.showOpenDialog(getWindow());
        updateTexFile(selectedFile);
    }

    private FXFileChooserStage getTexChooser() {
        return getFileChooser("TeX file", "tex", "Pick a TeX file");
    }

    @VisibleForTesting
    void updateTexFile(Optional<Path> tempTexFilePath) {
        tempTexFilePath.ifPresent(path -> {
            texFile = path.toFile();
            texFileLabel.setText(texFile.getAbsolutePath());
            texFileLabel.setTooltip(new Tooltip(texFile.getAbsolutePath()));
        });
    }

    @FXML
    public void onOpenCsvButtonClick(ActionEvent ignored) {
        FXFileChooserStage csvChooser = getCsvChooser();
        Optional<Path> selectedFile = csvChooser.showOpenDialog(getWindow());
        updateCsvFile(selectedFile);
    }

    private FXFileChooserStage getCsvChooser() {
        return getFileChooser("CSV file", "csv", "Pick a CSV file");
    }

    private FXFileChooserStage getFileChooser(String filterLabel, String fileExtension, String dialogTitle) {
        PathFilter filter = PathFilter.forFileExtension(filterLabel, fileExtension);
        FXFileChooserStage chooser;
        try {
            chooser = FXFileChooserStage.create(Skin.DEFAULT, filter);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        chooser.setTitle(dialogTitle);
        chooser.addLocations(List.of(
                Locations.at(Path.of(".")),
                Locations.at(Path.of("./src/integrationTest/resources"))
        ));
        return chooser;
    }

    @VisibleForTesting
    void updateCsvFile(Optional<Path> tempCsvFilePath) {
        tempCsvFilePath.ifPresent(path -> {
            csvFile = path.toFile();
            csvFileLabel.setText(csvFile.getAbsolutePath());
            csvFileLabel.setTooltip(new Tooltip(csvFile.getAbsolutePath()));
        });
    }

    @FXML
    public void onRenderPdfButtonClick(ActionEvent ignored) {
        try {
            if (!validateCsvFile()) {
                return;
            }
            if (!validateTexFile()) {
                return;
            }
            transformToPdf();
        } catch (Exception e) {
            showErrorMessage("An exception occurred - " + e.getClass().getSimpleName() + ": '" + e.getMessage() + "'");
        }
    }

    /**
     * @return true iff existing file
     */
    private boolean validateCsvFile() {
        if (csvFile == null || !csvFile.exists()) {
            showErrorMessage("CSV file not selected or does not exist!");
            return false;
        }
        return true;
    }


    /**
     * @return true iff existing file
     */
    private boolean validateTexFile() {
        if (texFile == null || !texFile.exists()) {
            showErrorMessage("TEX file not selected or does not exist!");
            return false;
        }
        return true;
    }

    private void transformToPdf() {
        // do nothing, for now
        showSuccessMessage("Starting PDF rendering...");
    }

    private void showSuccessMessage(String text) {
        Alert errorAlert = new Alert(Alert.AlertType.INFORMATION);
        errorAlert.setHeaderText("Success");
        errorAlert.setContentText(text);
        errorAlert.showAndWait();
    }

    private void showErrorMessage(String text) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Error");
        errorAlert.setContentText(text);
        errorAlert.showAndWait();
    }

    private Window getWindow() {
        return mainLayout.getScene().getWindow();
    }
}