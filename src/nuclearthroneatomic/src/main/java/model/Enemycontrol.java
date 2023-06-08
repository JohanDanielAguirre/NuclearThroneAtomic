package model;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import javafx.geometry.Point2D;
        import com.almasb.fxgl.dsl.FXGL;
        import com.almasb.fxgl.entity.Entity;
        import com.almasb.fxgl.entity.component.Component;
        import com.almasb.fxgl.entity.components.BoundingBoxComponent;
        import com.almasb.fxgl.texture.AnimatedTexture;
        import com.almasb.fxgl.texture.AnimationChannel;
        import javafx.geometry.Point2D;
        import javafx.scene.image.Image;
        import javafx.util.Duration;
        import java.util.ArrayList;
        import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameWorld;
        import static com.almasb.fxgl.dsl.FXGLForKtKt.getInput;

public class Enemycontrol  extends Component {
    private AnimationChannel animIdle, animWalk;
    private AnimatedTexture texture;
    private BoundingBoxComponent bbox;
    private boolean isMoving;
    private boolean isFacingRight;
    private static int life = 3;

    public Enemycontrol(AnimatedTexture texture) {
        this.bbox = new BoundingBoxComponent();
        this.texture = texture;
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
        animIdle = new AnimationChannel(imagesIdle, Duration.seconds(0.5));
        animWalk = new AnimationChannel(imagesRun, Duration.seconds(0.5));
    }
    /*
    @Override
    public void onUpdate(double tpf) {
        Entity player = getGameWorld().getSingleton(Types.PLAYER);
        Point2D playerPosition = player.getPosition();
        Point2D enemyPosition = entity.getPosition();
        double distance = enemyPosition.distance(playerPosition);
        if (distance <= 100) { // Ajusta el valor de distancia según tus necesidades
            // Atacar al jugador
            attackPlayer();
        } else {
            // Moverse hacia el jugador
            moveToPlayer(playerPosition);
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
        Entity weapon = getEntity().getComponent(PlayerWeaponComponent.class).getWeapon();
    }

    private void attackPlayer() {

        // Obtener la posición del enemigo
        Entity enemy = getEntity();
        Point2D enemyPosition = enemy.getPosition();

        // Obtener la posición actual del jugador
        Point2D playerPosition = getGameWorld().getSingleton(Types.PLAYER).getPosition();

        // Calcular la dirección del enemigo hacia la posición del jugador
        Point2D direction = playerPosition.subtract(enemyPosition).normalize();

        // Crear y spawnear una nueva bala
        Entity bullet = FXGL.spawn("Bullet",
                new SpawnData(enemyPosition)
                        .put("direction", direction));

    }

    private void moveToPlayer(Point2D playerPosition) {
        // Obtener la posición del enemigo
        Point2D enemyPosition = entity.getPosition();
        // Calcular la dirección hacia el jugador
        Point2D direction = playerPosition.subtract(enemyPosition).normalize();
        // Mover al enemigo en la dirección del jugador
        entity.translate(direction.multiply(100)); // Ajusta la velocidad de movimiento según tus necesidades
    }

     */
}