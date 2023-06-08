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
        luckytexture.setTranslateX(positionX-40);
        luckytexture.setTranslateY(positionY+3);
        luckytexture.setScaleX(0.4);
        luckytexture.setScaleY(0.4);
        text.setFill(Color.BLACK);
        text.setTranslateX(positionX + 50);
        text.setTranslateY(positionY +58);
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
        lifeaccount = PlayerControl.getLucky();
        text.setText("X " + lifeaccount);
    }
    public void clearUI() {
        getGameScene().removeUINodes(luckytexture,text);
    }
}

