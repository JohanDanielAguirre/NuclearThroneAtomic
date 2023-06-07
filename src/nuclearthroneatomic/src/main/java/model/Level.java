package model;

import com.almasb.fxgl.entity.Entity;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

public abstract class Level {

    public List<Entity> enemies = new ArrayList<>();

    private Pane storyPane = new Pane();
    private Pane rootPane;


}
