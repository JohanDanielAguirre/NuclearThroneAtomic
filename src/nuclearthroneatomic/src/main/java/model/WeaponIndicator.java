package model;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.Texture;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameScene;

public class WeaponIndicator extends Component {

    private Texture weaponTexture;
    private static double positionX = 575;
    private static double positionY = 600;

    @Override
    public void onAdded() {

        weaponTexture = FXGL.getAssetLoader().loadTexture("Guns/hand.png");
        weaponTexture.setTranslateX(positionX-100);
        weaponTexture.setTranslateY(positionY-100);
        weaponTexture.setScaleX(0.75);
        weaponTexture.setScaleY(0.75);
        getGameScene().addUINode(weaponTexture);
    }

    @Override
    public void onUpdate(double tpf) {

       updateImage();
    }

    private void updateImage(){
        PlayerWeaponComponent weaponComponent = FXGL.getGameWorld()
                .getSingleton(Types.PLAYER)
                .getComponent(PlayerWeaponComponent.class);
        if (weaponComponent != null && weaponComponent.getWeapon() != null) {
            weaponTexture.dispose();
            if (weaponComponent.getWeapon().getComponent(WeaponComponent.class).getName().equals("MachineGun")) {
                weaponTexture = FXGL.getAssetLoader().loadTexture("Guns/1.png");
            } else {
                weaponTexture = FXGL.getAssetLoader().loadTexture("Guns/2.png");
            }
            weaponTexture.setTranslateX(positionX-180);
            weaponTexture.setTranslateY(positionY-150);
            weaponTexture.setScaleX(0.35);
            weaponTexture.setScaleY(0.35);
            getGameScene().addUINode(weaponTexture);
        }


    }

    public void clearUI() {
        getGameScene().removeUINodes(weaponTexture);
    }
}
