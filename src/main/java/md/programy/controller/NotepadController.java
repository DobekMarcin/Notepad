package md.programy.controller;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

public class NotepadController {
    @FXML
    private MenuItem pselectAllTextContextMenuId;
    @FXML
    private MenuItem pasteTextContextMenuId;
    @FXML
    private MenuItem copyTextContextMenuId;
    @FXML
    private TextArea notepadTextArea;
    @FXML
    private TextField statusTextField;

    private File file;

    private Integer BOLD_FLAG = 0;
    private Integer ITALIC_FLAG=0;

    private Stage primaryStage;

    private SimpleStringProperty copyText = new SimpleStringProperty();
    private SimpleStringProperty notepadText = new SimpleStringProperty();
    private SimpleStringProperty statusText = new SimpleStringProperty();
    private SimpleStringProperty selectedText = new SimpleStringProperty();

    private Clipboard clip;

    public void initialize() {
        clip = Clipboard.getSystemClipboard();
        clip.clear();
        file = null;
        statusTextField.setEditable(false);
        statusTextField.textProperty().bind(statusText);
        notepadTextArea.textProperty().bindBidirectional(notepadText);
        notepadText.set("");
        statusText.setValue("Gotowy");
        copyTextContextMenuId.disableProperty().bind(notepadTextArea.selectedTextProperty().isEmpty());
        pasteTextContextMenuId.disableProperty().bind(copyText.isEmpty());
        pselectAllTextContextMenuId.disableProperty().bind(notepadTextArea.textProperty().isEmpty());
        addKeyCombinationToTextArea();
    }

    private void addKeyCombinationToTextArea() {
        final KeyCombination keyCombCtrC = new KeyCodeCombination(KeyCode.C, KeyCombination.SHORTCUT_DOWN);
        final KeyCombination keyCombCtrV = new KeyCodeCombination(KeyCode.V, KeyCombination.SHORTCUT_DOWN);
        notepadTextArea.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (keyCombCtrC.match(event)) {
                    if (!(clip.getString() == null)) {
                        copyText.set(clip.getString());
                        addLog("Skopiowano znaków: " + copyText.getValue().length());
                    }
                }
                if (keyCombCtrV.match(event)) {
                    addLog("Wklejono znaków: " + copyText.getValue().length());
                }
            }
        });
    }

    public void closeApplication() {
        Platform.exit();
        System.exit(0);
    }

    public void openFileMenuItem() {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            FileChooser fileChooser = new FileChooser();
            fileChooserTxtFilter(fileChooser);
            fileChooser.setTitle("Wybierz plik");
            File file2 = fileChooser.showOpenDialog(getPrimaryStage());
            if (file2 != null) {
                file = file2;
                Scanner scReadFile = new Scanner(file);
                while (scReadFile.hasNextLine()) {
                    stringBuilder.append(scReadFile.nextLine()).append("\n");
                }
                scReadFile.close();
                notepadText.set(stringBuilder.toString());
                addLog(file.getName());
            } else {
                addLog("Nie wybrano pliku!");
            }
        } catch (Exception e) {
            addLog("Nie można otworzyć pliku!");
        } finally {
            notepadTextArea.requestFocus();
        }
    }

    private static void fileChooserTxtFilter(FileChooser fileChooser) {
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"));
    }

    public void saveFileMenuItem() {
        if (file == null) {
            saveNewFile();
        } else {
            saveFile();
        }
    }

    private void saveNewFile() {
        FileChooser saveFileChooser = new FileChooser();
        saveFileChooser.setTitle("Wybierz lokalizację zapisu pliku");
        fileChooserTxtFilter(saveFileChooser);
        file = saveFileChooser.showSaveDialog(getPrimaryStage());
        if (file != null)
            saveFile();
        else
            addLog("Nie wybrano pliku!");
    }

    private void saveFile() {
        try {
            PrintWriter printWriter = new PrintWriter(file);
            printWriter.append(notepadText.getValue());
            printWriter.close();
            addLog("Zapisano plik: " + file.getName());
        } catch (Exception e) {
            addLog("Nie można zapisać pliku!");
        }
    }

    public void saveNewFileMenuItem() {
        saveNewFile();
    }

    private void addLog(String newLog) {
        statusText.set(newLog);
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void copyTextContextmenu() {
        copyText.set(notepadTextArea.getSelectedText());
        ClipboardContent content = new ClipboardContent();
        content.putString(copyText.getValue());
        clip.setContent(content);
        addLog("Skopiowano znaków: " + copyText.getValue().length());

    }

    public void pasteTextContextmenu() {
        int caretPosition = notepadTextArea.getCaretPosition();
        StringBuilder stringBuilder = new StringBuilder(notepadText.getValue());
        stringBuilder.insert(caretPosition, copyText.getValue());
        notepadTextArea.setText(stringBuilder.toString());
        notepadTextArea.positionCaret(caretPosition + copyText.getValue().length());
        addLog("Wklejono znaków: " + copyText.getValue().length());
    }

    public void selectAllTextContextmenu() {
        notepadTextArea.selectAll();
    }


    public void menuInfo() {

    }
}
