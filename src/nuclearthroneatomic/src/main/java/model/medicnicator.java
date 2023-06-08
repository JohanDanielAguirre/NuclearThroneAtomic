package model;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.Texture;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameScene;
public class medicnicator extends Component {
    private Texture medictexture;
    private Text text;
    private static double positionX = 200;
    private static double positionY = 50;

    @Override
    public void onAdded() {
        text=new Text();
        medictexture = FXGL.getAssetLoader().loadTexture("hud/medkit.png");
        medictexture.setTranslateX(positionX-50);
        medictexture.setTranslateY(positionY-20);
        medictexture.setScaleX(0.4);
        medictexture.setScaleY(0.4);
        text.setFill(Color.BLACK);
        text.setTranslateX(positionX + 50);
        text.setTranslateY(positionY +40);
        text.setFont(Font.font(30));
        getGameScene().addUINode(medictexture);
        getGameScene().addUINode(text);
    }

    @Override
    public void onUpdate(double tpf) {
        updatelucky();
    }
    private void updatelucky() {
        int lifeaccount;
        lifeaccount = PlayerControl.getMedkits();
        text.setText("X " + lifeaccount);
    }
    public void clearUI() {
        getGameScene().removeUINodes(medictexture,text);
    }
}

