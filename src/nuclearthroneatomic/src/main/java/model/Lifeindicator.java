package model;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.Texture;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameScene;

public class Lifeindicator extends Component {
    private Texture lifetexture;
    private Text text;
    private static double positionX = 50;
    private static double positionY = 100;

    @Override
    public void onAdded() {
        text=new Text();
        lifetexture = FXGL.getAssetLoader().loadTexture("hud/lifeplayer.png");
        lifetexture.setTranslateX(positionX-600);
        lifetexture.setTranslateY(positionY-600);
        lifetexture.setScaleX(0.08);
        lifetexture.setScaleY(0.08);
        text.setFill(Color.BLACK);
        text.setTranslateX(positionX + 20);
        text.setTranslateY(positionY +35);
        text.setFont(Font.font(30));
        getGameScene().addUINode(lifetexture);
        getGameScene().addUINode(text);
    }

    @Override
    public void onUpdate(double tpf) {
        updatelife();
    }
    private void updatelife() {
        int lifeaccount;
        PlayerControl playerControl = FXGL.getGameWorld()
                .getSingleton(Types.PLAYER)
                .getComponent(PlayerControl.class);
            lifetexture.dispose();
                lifetexture = FXGL.getAssetLoader().loadTexture("hud/lifeplayer.png");
                lifetexture.setScaleX(0.08);
                lifetexture.setScaleY(0.08);
                lifetexture.setTranslateX(positionX-460);
                lifetexture.setTranslateY(positionY-425);
                getGameScene().addUINode(lifetexture);
            lifeaccount = PlayerControl.getLife();
            text.setText("X " + lifeaccount);
    }
}
