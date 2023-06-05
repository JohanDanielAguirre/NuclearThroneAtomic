package com.example.nuclearthroneatomic.controller;

import com.example.nuclearthroneatomic.HelloApplication;
import com.example.nuclearthroneatomic.model.Player;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

public class Scenario1Controller extends Thread implements Initializable {

    @FXML
    protected Canvas canvas;

    private GraphicsContext gc;

    private Player player;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gc = canvas.getGraphicsContext2D();
        String ur = "file:"+ HelloApplication.class.getResource("duckMain.png").getPath();
        player = new Player(250,200,10,100,null,null,null,new Image(ur));
        setDaemon(true);
        start();
    }

    @Override
    public void run() {
        super.run();

        while (player.getHealth() > 0){
            gc.setFill(Color.WHITE);

            gc.drawImage(player.getAvatar(),player.getPositionx(),player.getPositiony(),50,50);

            gc.fillRect(player.getPositionx(),player.getPositiony(),50,50);


        }
    }
}
