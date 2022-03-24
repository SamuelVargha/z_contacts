package com.vargha;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class EditContactController implements Initializable {
    private Controller controller;
    private ArrayList<String> names;

    @FXML
    private TextField nameField;

    @FXML
    private TextField phoneField;

    @FXML
    private TextField mailField;

    @FXML
    private Button okButton;

    EditContactController(Controller controller, ArrayList<String> names) {
        this.controller = controller;
        this.names = names;
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        okButton.setDisable(true);
        TableView<Contact> tableView = controller.getTable();

        if (tableView.getSelectionModel().getSelectedItem().getName().length() != 0) {
            nameField.setText(tableView.getSelectionModel().getSelectedItem().getName());
        }
        if (tableView.getSelectionModel().getSelectedItem().getPhone().length() != 0) {
            phoneField.setText(tableView.getSelectionModel().getSelectedItem().getPhone());
        }
        if (tableView.getSelectionModel().getSelectedItem().getEmail().length() != 0) {
            mailField.setText(tableView.getSelectionModel().getSelectedItem().getEmail());
        }

        phoneField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(!newValue.matches("\\d*")){
                    phoneField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }

    @FXML
    private void nameKey(){
        okButton.setDisable(false);
        checkIfEmpty();

    }

    @FXML
    private void phoneKey(){
        okButton.setDisable(false);
        checkIfEmpty();
    }

    @FXML private void mailKey(){
        okButton.setDisable(false);
        checkIfEmpty();
    }


    @FXML
    private void ok(){
        if(names.contains(nameField.getText())){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Cannot have two contacts of the same name");
            alert.showAndWait();
            return;
        }
        Contact contact = new Contact(0, nameField.getText(), phoneField.getText(), mailField.getText());
        controller.editContactOk(contact);
    }

    @FXML
    private void cancel(){
        controller.cancel();
    }

    private void checkIfEmpty(){
        if(nameField.getText().length() == 0 || (phoneField.getText().length() == 0 )){
            okButton.setDisable(true);
        }
    }


}
