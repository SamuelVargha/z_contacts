package com.vargha;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class Controller implements Initializable {

    private ObservableList<Contact> contacts = FXCollections.observableArrayList();
    private ArrayList<String> names = new ArrayList<>();
    private NewContactController newContactController;
    private EditContactController editContactController;
    private AboutController aboutController;
    private ContactFile currentFile = new ContactFile(null, new ArrayList<>());
    private Stage secondStage = new Stage();
    @FXML private TableView<Contact> table;
    @FXML private TextField findField;
    @FXML private TableColumn<Contact, String> nameCol;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nameCol.setSortType(TableColumn.SortType.DESCENDING);
        table.setPlaceholder(new Label(""));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        FilteredList<Contact> filteredList = new FilteredList<>(contacts, p -> true);

        findField.textProperty().addListener(((observable, oldValue, newValue) -> {
            filteredList.setPredicate(contact -> {
                if (newValue == null || newValue.isEmpty()){
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if(contact.getName().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                }
                return false;
            });
        }));

        SortedList<Contact> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedList);
    }


    public Controller() {
        newContactController = new NewContactController(this,names);
        editContactController = new EditContactController(this, names);
        aboutController = new AboutController(this);
    }

    @FXML
    private void tableNew(){
        if(!checkIfSaved()){
            return;
        }
        contacts.clear();
        currentFile = new ContactFile(null, new ArrayList<>());
    }

    @FXML
    private void open(){
        if(!checkIfSaved()){
            return;
        }
        boolean check = true;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Documents"));
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setTitle("Choose a file");
        File file = fileChooser.showOpenDialog(null);

        if(file != null){
            Path path = file.toPath();
            List<String> list = new ArrayList<>();

            try {
                Scanner scanner = new Scanner(file);
                while (scanner.hasNextLine()){
                    list.add(scanner.nextLine());
                }
                scanner.close();
            } catch (FileNotFoundException e){
                Alert alert = new Alert(Alert.AlertType.ERROR, "File not found", ButtonType.CLOSE);
                alert.setTitle("Error");
                alert.show();
            }
            for (String line: list){
                if(line.contains("█")){
                    check = true;
                    break;
                }
                check = false;
            }

            if(!check){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText("Unable to open this file");
                alert.showAndWait();
                return;
            }
            contacts.clear();

            for (int i = 0; i < list.size(); i++){
                ArrayList<String> temp = new ArrayList<>(Arrays.asList(list.get(i).split("█")));
                try{
                    System.out.println();
                } catch (IndexOutOfBoundsException e){
                    temp.add(1, "");
                } try{
                    System.out.println();
                } catch (IndexOutOfBoundsException e){
                    temp.add(2, "");
                } try{
                    System.out.println();
                } catch (IndexOutOfBoundsException e){
                    temp.add(3, "");
                }
                contacts.add(new Contact(0, temp.get(1), temp.get(2), temp.get(3)));
            }
            contacts.sort(Comparator.comparing(Contact::getName));
            for(int i = 0; i < contacts.size(); i++){
                contacts.get(i).setId(i + 1);
            }

            try {
                currentFile = new ContactFile(path, list);

            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.CLOSE);
                alert.setTitle("Error");
                alert.show();
            }
        }
    }

    @FXML
    private void save(){
        try {
            if(currentFile.getPath() == null){
                saveAs();
            }
        } catch (NullPointerException e){
            saveAs();
            return;
        }
        Path path;
        path = currentFile.getPath();

        String text;
        List<String> textList = new ArrayList<>();


        for(Contact contact : contacts){
            text = (contact.getId() + "█" + contact.getName() + "█" +  contact.getPhone() + "█" + contact.getEmail());
            textList.add(text);
        }

        try {
            Files.write(path, textList, StandardOpenOption.CREATE);
            currentFile = new ContactFile(path, textList);

        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.CLOSE);
            alert.setTitle("Error");
            alert.showAndWait();
        }
    }

    @FXML
    private void saveAs(){
        Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
        infoAlert.setTitle("Directory");
        infoAlert.setHeaderText("Choose a directory");
        infoAlert.setContentText("");
        infoAlert.showAndWait();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Documents"));
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setTitle("Choose a directory");
        File file = fileChooser.showSaveDialog(null);

        Path path;
        try {
            path = file.toPath();
        }catch (NullPointerException e){
            return;
        }
        String text;
        List<String> textList = new ArrayList<>();

        for(Contact contact : contacts){
            text = (contact.getId() + "█" + contact.getName() + "█" +  contact.getPhone() + "█" + contact.getEmail());
            textList.add(text);
        }

        try {
            Files.write(path, textList, StandardOpenOption.CREATE);
            currentFile = new ContactFile(path, textList);

        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.CLOSE);
            alert.setTitle("Error");
            alert.showAndWait();
        }
    }

    @FXML
    private void exit(){
        if(!checkIfSaved()){
            return;
        }
        Platform.exit();
    }

    @FXML
    private void add(){
        for(Contact contact : contacts){
            names.add(contact.getName());
        }
        FXMLLoader newLoader = new FXMLLoader(getClass().getResource("newContactUI.fxml"));
        newLoader.setControllerFactory(t -> newContactController);
        try{
            secondStage.setTitle("ZContacts");
            Parent root = newLoader.load();
            secondStage.setScene(new Scene(root, 380, 160));
            secondStage.getIcons().add(new Image("file:icon.png"));
            secondStage.setResizable(false);
            secondStage.setAlwaysOnTop(true);
            secondStage.show();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    void newContactOk(Contact contact){
        contacts.add(contact);
        secondStage.close();
        contacts.sort(Comparator.comparing(Contact::getName));
        for(int i = 0; i < contacts.size(); i++){
            contacts.get(i).setId(i + 1);
        }
    }

    void cancel(){
        secondStage.close();
    }

    @FXML
    private void edit(){
        for(Contact contact : contacts){
            names.add(contact.getName());
        }
        Contact c = table.getSelectionModel().getSelectedItem();
        if(c == null){
            return;
        }
        FXMLLoader newLoader = new FXMLLoader(getClass().getResource("editContactUI.fxml"));
        newLoader.setControllerFactory(t -> editContactController);
        try{
            secondStage.setTitle("ZContacts");
            Parent root = newLoader.load();
            secondStage.setScene(new Scene(root, 380, 160));
            secondStage.getIcons().add(new Image("file:icon.png"));
            secondStage.setResizable(false);
            secondStage.setAlwaysOnTop(true);
            secondStage.show();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    void editContactOk(Contact contact){
        eDelete();
        contacts.add(contact);
        secondStage.close();

        contacts.sort(Comparator.comparing(Contact::getName));
        for(int i = 0; i < contacts.size(); i++){
            contacts.get(i).setId(i + 1);
        }
    }

    @FXML
    private void duplicate(){
        Contact c = table.getSelectionModel().getSelectedItem();
        if(c == null){
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Do you wish to duplicate the contact: " + c.getName() + "?");
        alert.showAndWait();
        if(alert.getResult() == ButtonType.OK){
            String temp = c.getName();
            contacts.add(new Contact(0, temp + "*", c.getPhone(), c.getEmail()));
        }

        contacts.sort(Comparator.comparing(Contact::getName));
        for(int i = 0; i < contacts.size(); i++){
            contacts.get(i).setId(i + 1);
        }
    }

    @FXML
    private void delete(){
        Contact c = table.getSelectionModel().getSelectedItem();
        if(c == null){
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Do you wish to delete the contact: " + c.getName() + "?");
        alert.showAndWait();
        if(alert.getResult() == ButtonType.OK){
            contacts.remove(c);
            contacts.sort(Comparator.comparing(Contact::getName));
            for(int i = 0; i < contacts.size(); i++){
                contacts.get(i).setId(i + 1);
            }
        }
    }

    @FXML
    private void eDelete(){
        Contact c = table.getSelectionModel().getSelectedItem();
        contacts.remove(c);
        contacts.sort(Comparator.comparing(Contact::getName));
        for(int i = 0; i < contacts.size(); i++){
            contacts.get(i).setId(i + 1);
        }
    }

    @FXML
    private void about(){
        FXMLLoader newLoader = new FXMLLoader(getClass().getResource("aboutUI.fxml"));
        newLoader.setControllerFactory(t -> aboutController);
        try{
            secondStage.setTitle("ZContacts");
            Parent root = newLoader.load();
            secondStage.setScene(new Scene(root, 385, 250));
            secondStage.getIcons().add(new Image("file:icon.png"));
            secondStage.setResizable(false);
            secondStage.setAlwaysOnTop(true);
            secondStage.show();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private boolean checkIfSaved(){
        List<String> textList = new ArrayList<>();
        String text;
        for(Contact contact : contacts){
            text = (contact.getId() + "█" + contact.getName() + "█" +  contact.getPhone() + "█" + contact.getEmail());
            textList.add(text);
        }

        if(currentFile.getContent().size() == 0 && textList.size() == 0){
            return true;
        } else if (currentFile.getContent().size() == 0){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Would you like to save first?");
            ButtonType buttonSave = new ButtonType("Save");
            ButtonType buttonDoNotSave = new ButtonType("Don't save");
            ButtonType buttonCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(buttonSave, buttonDoNotSave, buttonCancel);

            Optional<ButtonType> result = alert.showAndWait();
            if(result.get() == buttonSave){
                save();
            } else if(result.get() == buttonCancel){
                return false;
            } else if(result.get() == buttonDoNotSave){
                return true;
            } else {
                return false;
            }
            return true;
        }

        for(int i = 0; i < currentFile.getContent().size(); i++){

            if(!currentFile.getContent().get(i).equals(textList.get(i))){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setHeaderText("Would you like to save first?");
                ButtonType buttonSave = new ButtonType("Save");
                ButtonType buttonDoNotSave = new ButtonType("Don't save");
                ButtonType buttonCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
                alert.getButtonTypes().setAll(buttonSave, buttonDoNotSave, buttonCancel);

                Optional<ButtonType> result = alert.showAndWait();
                if(result.get() == buttonSave){
                    save();
                } else if(result.get() == buttonCancel){
                    return false;
                } else if(result.get() == buttonDoNotSave){
                    return true;
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    TableView<Contact> getTable(){
        return table;
    }

    void close(){
        if(checkIfSaved()){
            Platform.exit();
        }
    }
}
