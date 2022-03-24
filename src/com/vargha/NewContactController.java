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

public class NewContactController implements Initializable {

    private boolean checkOne = false;
    private boolean checkTwo = false;
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

    NewContactController(Controller controller, ArrayList<String> names) {
        this.controller = controller;
        this.names = names;

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        okButton.setDisable(true);
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
        checkOne = true;
        if(checkTwo){
           okButton.setDisable(false);
        }
        checkIfEmpty();

    }

    @FXML
    private void phoneKey(){
        checkTwo = true;
        if(checkOne){
            okButton.setDisable(false);
        }
        checkIfEmpty();
    }

    @FXML private void mailKey(){
        checkTwo = true;
        if(checkOne){
            okButton.setDisable(false);
        }
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
        controller.newContactOk(contact);
    }

    @FXML
    private void cancel(){
        controller.cancel();
    }

    private void checkIfEmpty(){
        if((nameField.getText().length() != 0 && (phoneField.getText().length() != 0 )) || (nameField.getText().length()
                != 0 && (mailField.getText().length() != 0 ))){
            okButton.setDisable(false);
        } else {
            okButton.setDisable(true);
        }
    }


}
