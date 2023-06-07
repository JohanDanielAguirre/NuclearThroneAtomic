package model;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.Texture;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameScene;
public class luckyindicatorComponent extends Component {
    private Texture luckytexture;
    private Text text;
    private static double positionX = 200;
    private static double positionY = 100;

    @Override
    public void onAdded() {
        text=new Text();
        luckytexture = FXGL.getAssetLoader().loadTexture("hud/trebol.png");
        luckytexture.setTranslateX(positionX-600);
        luckytexture.setTranslateY(positionY-600);
        luckytexture.setScaleX(0.08);
        luckytexture.setScaleY(0.08);
        text.setFill(Color.BLACK);
        text.setTranslateX(positionX + 20);
        text.setTranslateY(positionY +35);
        text.setFont(Font.font(30));
        getGameScene().addUINode(luckytexture);
        getGameScene().addUINode(text);
    }

    @Override
    public void onUpdate(double tpf) {
        updatelucky();
    }
    private void updatelucky() {
        int lifeaccount;
        PlayerControl playerControl = FXGL.getGameWorld()
                .getSingleton(Types.PLAYER)
                .getComponent(PlayerControl.class);
        luckytexture.dispose();
        luckytexture = FXGL.getAssetLoader().loadTexture("hud/trebol.png");
        luckytexture.setScaleX(0.08);
        luckytexture.setScaleY(0.08);
        luckytexture.setTranslateX(positionX-460);
        luckytexture.setTranslateY(positionY-425);
        getGameScene().addUINode(luckytexture);
        lifeaccount = PlayerControl.getLucky();
        text.setText("X " + lifeaccount);

    }
}
