package com.vargha;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


import java.net.URL;
import java.util.ResourceBundle;

public class AboutController implements Initializable {
    @FXML private ImageView imageView;
    private Controller controller;

    AboutController(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Image image = new Image("file:icon.png");
        imageView.setImage(image);
        imageView.setVisible(true);
    }

    @FXML private void ok(){
        controller.cancel();
    }
}
