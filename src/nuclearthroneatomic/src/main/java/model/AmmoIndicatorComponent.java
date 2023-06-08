package model;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.Texture;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getAssetLoader;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameScene;

public class AmmoIndicatorComponent extends Component {
    private Texture ammoTexture;
    private Text text;
    private static double positionX = 50;
    private static double positionY = 30;

    @Override
    public void onAdded() {
        text=new Text();
        ammoTexture = FXGL.getAssetLoader().loadTexture("Munition/1.png");
        ammoTexture.setTranslateX(positionX-425);
        ammoTexture.setTranslateY(positionY-400);
        ammoTexture.setScaleX(0.08);
        ammoTexture.setScaleY(0.08);

        text.setFill(Color.BLACK);
        text.setTranslateX(positionX + 60);
        text.setTranslateY(positionY +60);
        text.setFont(Font.font(30));

        getGameScene().addUINode(ammoTexture);
        getGameScene().addUINode(text);
    }

    @Override
    public void onUpdate(double tpf) {
        updateAmmoCount();
    }
    private void updateAmmoCount() {
        int ammoCount;
        PlayerWeaponComponent weaponComponent = FXGL.getGameWorld()
                .getSingleton(Types.PLAYER)
                .getComponent(PlayerWeaponComponent.class);
        if (weaponComponent != null && weaponComponent.getWeapon() != null) {
            ammoTexture.dispose();
            if (weaponComponent.getWeapon().getComponent(WeaponComponent.class).getName().equals("MachineGun")) {
                ammoTexture = FXGL.getAssetLoader().loadTexture("Munition/1.png");
                ammoTexture.setScaleX(0.08);
                ammoTexture.setScaleY(0.08);
                ammoTexture.setTranslateX(positionX-425);
                ammoTexture.setTranslateY(positionY-400);
                getGameScene().addUINode(ammoTexture);
            } else {
                ammoTexture = FXGL.getAssetLoader().loadTexture("Munition/2.png");
                ammoTexture.setScaleX(2);
                ammoTexture.setScaleY(2);
                ammoTexture.setTranslateX(positionX+20);
                ammoTexture.setTranslateY(positionY+40);
                getGameScene().addUINode(ammoTexture);
            }
            ammoCount = weaponComponent.getAmmoCount();
            text.setText("X " + ammoCount);
        } else {
            text.setText("X " + 0);
        }
    }
}