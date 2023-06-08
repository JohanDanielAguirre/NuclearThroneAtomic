package model;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.*;
import com.almasb.fxgl.entity.components.BoundingBoxComponent;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import com.example.nuclearthroneatomic.Controller;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.almasb.fxgl.dsl.FXGLForKtKt.*;

public class PlayerControl extends Component {
    private AnimationChannel animIdle, animWalk;
    private AnimatedTexture texture;
    private BoundingBoxComponent bbox;
    private boolean isMoving;
    private boolean isFacingRight;
    private static int life=1;

    public static int getLife() {
        return life;
    }
    private static int lucky=1;

    public static int getLucky() {
        return lucky;
    }
    private static int medkits=3;

    public static int getMedkits() {
        return medkits;
    }
    private  long time = 0;


    public PlayerControl(AnimatedTexture texture) {
        this.bbox = new BoundingBoxComponent();
        this.texture =texture;

        var imagesIdle = new ArrayList<Image>();
        for (int i = 1; i <= 4; i++) {
            String imagePath = "Avatar/1-Idle/" + i + ".png";
            imagesIdle.add(FXGL.getAssetLoader().loadImage(imagePath));
        }
        var imagesRun = new ArrayList<Image>();
        for (int i = 1; i <= 6; i++) {
            String imagePath = "Avatar/2-Run/" + i + ".png";
            imagesRun.add(FXGL.getAssetLoader().loadImage(imagePath));
        }
        animIdle = new AnimationChannel(imagesIdle,  Duration.seconds(0.5));
        animWalk = new AnimationChannel(imagesRun,  Duration.seconds(0.5));
    }
    @Override
    public void onUpdate(double tpf) {
        double screenWidth = getAppWidth();
        double screenHeight = getAppHeight();

        double minX = 0;
        double minY = 0;
        double maxX = screenWidth - entity.getWidth();
        double maxY = screenHeight - entity.getHeight();

        double playerX = entity.getX();
        double playerY = entity.getY();

        if (playerX < minX) {
            entity.setX(minX);
        } else if (playerX > maxX) {
            entity.setX(maxX);
        }

        if (playerY < minY+30) {
            entity.setY(minY+30);
        } else if (playerY > maxY-40) {
            entity.setY(maxY-40);
        }
        if (isMoving) {
            if (!texture.getAnimationChannel().equals(animWalk)) {
                texture.loopAnimationChannel(animWalk);
            }
        } else {
            if (!texture.getAnimationChannel().equals(animIdle)) {
                texture.loopAnimationChannel(animIdle);
            }
        }
        isMoving = false;
        facing();
        Entity weapon = getEntity().getComponent(PlayerWeaponComponent.class).getWeapon();
        if (weapon != null) {
            weapon.setPosition(getEntity().getPosition().subtract(265,60));
            Point2D direction = getInput().getMousePositionWorld()
                    .subtract(entity.getPosition());
            weapon.rotateToVector(direction);
            if(direction.getX()<0){
                weapon.rotateBy(180);
                weapon.setScaleX(-0.15);
            }
            else{
                weapon.setScaleX(0.15);
            }
            if (!getGameWorld().getEntities().contains(weapon)) {
                getGameWorld().addEntity(weapon);
            }
        }
    }
    public void moveRight() {
        entity.translateX(5);
        texture.setScaleX(1);
        isFacingRight=true;
        isMoving=true;
    }

    public void moveLeft() {
        entity.translateX(-5);
        texture.setScaleX(-1);
        isMoving=true;
        isFacingRight=false;
    }

    public void moveUp() {
        entity.translateY(-5);
        isMoving=true;
    }

    public void moveDown(){
        entity.translateY(5);
        isMoving=true;
    }

    public void health() {
        if (life >= 3) {
            return;
        }

        if (medkits > 0) {
            life++;
            --medkits;
        }
    }

    public void lucky() {
        if (lucky > 0) {
            life = 999;
            --lucky;
            getGameTimer().runOnceAfter(() -> {
                life = 3;
            }, javafx.util.Duration.seconds(5));
        }
    }
    public void reload() {
        getGameTimer().runOnceAfter(() -> {
            PlayerWeaponComponent weaponComponent= getEntity().getComponent(PlayerWeaponComponent.class);
            if(weaponComponent.getWeapon().getComponent(WeaponComponent.class).getName().equals("MachineGun")){
                weaponComponent.setAmmoCount(10);
            }
            else{
                weaponComponent.setAmmoCount(3);
            }
            Controller.isRealoding = false;
        }, Duration.seconds(3));
    }
    public void facing(){
        Point2D direction = getInput().getMousePositionWorld()
                .subtract(entity.getPosition());
        if(direction.getX()>0){
            isFacingRight=true;
            texture.setScaleX(1);
        }
        else{
            isFacingRight=false;
            texture.setScaleX(-1);
        }
    }
}
