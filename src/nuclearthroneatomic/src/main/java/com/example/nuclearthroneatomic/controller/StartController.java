package com.example.nuclearthroneatomic.controller;

import com.example.nuclearthroneatomic.HelloApplication;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class StartController implements Initializable {

    @FXML
    protected Button playBtn;

    @FXML
    protected Canvas canvaStart;

    private GraphicsContext gc;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        gc = canvaStart.getGraphicsContext2D();
        printImage();

    }

    private void printImage(){
        String uri = "file:"+ HelloApplication.class.getResource("icesigood.png").getPath();;
        gc.drawImage(new Image(uri),0,0,600,400);
    }

    @FXML
    public void onMouseClicked(){

        HelloApplication.hideWindow((Stage) playBtn.getScene().getWindow());
        HelloApplication.showWindow("Scenario1");

    }
}
