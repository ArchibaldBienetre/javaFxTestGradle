package com.example.javafxtest;

import com.google.common.annotations.VisibleForTesting;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.stage.FileChooser;
import javafx.stage.Window;

//import javax.annotation.Nullable;
import java.io.File;

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
        File tempTexFile = getTexChooser().showOpenDialog(getWindow());
        updateTexFile(tempTexFile);

    }

    private FileChooser getTexChooser() {
        FileChooser texChooser = new FileChooser();
        texChooser.setTitle("Pick a TeX file");
        FileChooser.ExtensionFilter texFilter = new FileChooser.ExtensionFilter("TeX files", "*.tex");
        texChooser.getExtensionFilters().add(texFilter);
        texChooser.setSelectedExtensionFilter(texFilter);
        return texChooser;
    }

    @VisibleForTesting
    void updateTexFile(/*@Nullable*/ File tempTexFile) {
        if (tempTexFile != null) {
            texFile = tempTexFile;
            texFileLabel.setText(texFile.getAbsolutePath());
            texFileLabel.setTooltip(new Tooltip(texFile.getAbsolutePath()));
        }
    }

    @FXML
    public void onOpenCsvButtonClick(ActionEvent ignored) {
        File tempCsvFile = getCsvChooser().showOpenDialog(getWindow());
        updateCsvFile(tempCsvFile);
    }

    private FileChooser getCsvChooser() {
        FileChooser csvChooser = new FileChooser();
        csvChooser.setTitle("Pick a CSV file");
        FileChooser.ExtensionFilter csvFilter = new FileChooser.ExtensionFilter("CSV files", "*.csv");
        csvChooser.getExtensionFilters().add(csvFilter);
        csvChooser.setSelectedExtensionFilter(csvFilter);
        return csvChooser;
    }

    @VisibleForTesting
    void updateCsvFile(/*@Nullable*/ File newCsvFile) {
        if (newCsvFile != null) {
            csvFile = newCsvFile;
            csvFileLabel.setText(csvFile.getAbsolutePath());
            csvFileLabel.setTooltip(new Tooltip(csvFile.getAbsolutePath()));
        }
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