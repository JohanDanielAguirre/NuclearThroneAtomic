package model;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.BoundingBoxComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.scene.image.Image;
import javafx.util.Duration;

import java.util.ArrayList;

public class PortalControl extends Component {

    private AnimationChannel animIdle;
    private AnimatedTexture texture;
    private BoundingBoxComponent bbox;

    public PortalControl(AnimatedTexture texture) {
        this.bbox = new BoundingBoxComponent();
        this.texture = texture;

        var imagesIdle = new ArrayList<Image>();
        for (int i = 1; i <= 4; i++) {
            String imagePath = "portal/" + i + ".png";
            imagesIdle.add(FXGL.getAssetLoader().loadImage(imagePath));
        }

        animIdle = new AnimationChannel(imagesIdle, Duration.seconds(0.5));

    }
}
