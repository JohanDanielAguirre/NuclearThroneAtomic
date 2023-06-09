package model;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.time.LocalTimer;
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
import java.util.List;

import static com.almasb.fxgl.dsl.FXGLForKtKt.*;

public class Enemycontrol  extends Component {
    private AnimationChannel animIdle, animWalk;
    private AnimatedTexture texture;
    private BoundingBoxComponent bbox;
    private boolean isMoving;
    private boolean isFacingRight;

    private int life = 3;
    public  void setLife() {
        life--;
    }

    public int getLife() {
        return life;
    }

    protected LocalTimer attackTimer;
    protected Duration nextAttack = Duration.seconds(2);


    public Enemycontrol(AnimatedTexture texture) {
        this.bbox = new BoundingBoxComponent();
        this.texture = texture;

        var imagesIdle = new ArrayList<Image>();
        for (int i = 1; i <= 4; i++) {
            String imagePath = "enemy/1-Idle/" + i + ".png";
            imagesIdle.add(FXGL.getAssetLoader().loadImage(imagePath));
        }
        var imagesRun = new ArrayList<Image>();
        for (int i = 1; i <= 6; i++) {
            String imagePath = "enemy/2-Run/" + i + ".png";
            imagesRun.add(FXGL.getAssetLoader().loadImage(imagePath));
        }
        animIdle = new AnimationChannel(imagesIdle, Duration.seconds(0.5));
        animWalk = new AnimationChannel(imagesRun, Duration.seconds(0.5));
    }

    @Override
    public void onAdded() {
        EnemyWeaponComponent weaponComponent = entity.getComponent(EnemyWeaponComponent.class);
        weaponComponent.setWeapon(spawn("Weapon"));
        weaponComponent.getWeapon().getComponent(WeaponComponent.class).setEnemy(entity);
        attackTimer = FXGL.newLocalTimer();
        attackTimer.capture();
    }
    @Override
    public void onUpdate(double tpf) {
        Entity player = getGameWorld().getSingleton(Types.PLAYER);
        Point2D playerPosition = player.getPosition();
        Point2D enemyPosition = entity.getPosition();
        double distance = enemyPosition.distance(playerPosition);
        if (distance <= 150) { // Ajusta el valor de distancia según tus necesidades
            isMoving = false;
            if (!texture.getAnimationChannel().equals(animIdle)) {
                texture.loopAnimationChannel(animIdle);
            }
            if (attackTimer.elapsed(nextAttack)) {
                if (FXGLMath.randomBoolean(0.5f)){
                    attackPlayer();
                }
                nextAttack = Duration.seconds(2 * Math.random());
                attackTimer.capture();
            }


        } else {
            isMoving = true;
            moveToPlayer(playerPosition);
        }
        Entity weapon = getEntity().getComponent(EnemyWeaponComponent.class).getWeapon();
        if (weapon != null) {
            playerPosition = getGameWorld().getSingleton(Types.PLAYER).getPosition();
            enemyPosition = getEntity().getPosition();

            Point2D direction = playerPosition.subtract(enemyPosition).normalize();

            double rotationRadians = Math.atan2(direction.getY(), direction.getX());
            double rotationDegrees = Math.toDegrees(rotationRadians);

            weapon.setPosition(enemyPosition.subtract(265, 60));
            weapon.setRotation(rotationDegrees);

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
            if(getLife()<=0){
                entity.getComponent(EnemyWeaponComponent.class).getWeapon().removeFromWorld();
                entity.removeFromWorld();
            }
        }
    }

    public void attackPlayer(){
            List<Entity> enemies = getGameWorld().getEntitiesByType(Types.ENEMY);
            Point2D playerPosition = getGameWorld().getSingleton(Types.PLAYER).getPosition();

        for (int i = 0; i < enemies.size(); i++) {
            Entity enemy = enemies.get(i);
            Point2D enemyPosition = enemy.getPosition();
            Point2D direction = playerPosition.subtract(enemyPosition).normalize();

            SpawnData spawnData = new SpawnData();
            spawnData.put("direction", direction);
            spawnData.put("weapon",enemy.getComponent(EnemyWeaponComponent.class).getWeapon());
            enemy.getComponent(EnemyWeaponComponent.class).getWeapon().getComponent(WeaponComponent.class).setBullet(spawn("EnemyBullet", spawnData));
        }
        
    }

    public void destroyWall(Point2D direction,Entity enemy){
        SpawnData spawnData = new SpawnData();
        spawnData.put("direction", direction);
        spawnData.put("weapon",enemy.getComponent(EnemyWeaponComponent.class).getWeapon());
        enemy.getComponent(EnemyWeaponComponent.class).getWeapon().getComponent(WeaponComponent.class).setBullet(spawn("EnemyBullet", spawnData));
    }

    public void moveToPlayer(Point2D playerPosition) {
        Point2D enemyPosition = entity.getPosition();
        // Calcular la dirección hacia el jugador
        Point2D direction = playerPosition.subtract(enemyPosition).normalize();
        // Mover al enemigo en la dirección del jugador
        entity.translate(direction.multiply(2.5)); // Ajusta la velocidad de movimiento según tus necesidades
            if (!texture.getAnimationChannel().equals(animWalk)) {
                texture.loopAnimationChannel(animWalk);
            }
    }
}