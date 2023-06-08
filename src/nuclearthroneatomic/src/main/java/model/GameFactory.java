package model;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.OffscreenCleanComponent;
import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.util.Duration;

import java.util.ArrayList;

import static com.almasb.fxgl.dsl.FXGLForKtKt.*;

public class GameFactory implements EntityFactory {
    @Spawns("Avatar")
    public Entity newAvatar(SpawnData data) {
        var images = new ArrayList<Image>();
        for (int i = 1; i <= 4; i++) {
            String imagePath = "Avatar/1-Idle/" + i + ".png";
            images.add(FXGL.getAssetLoader().loadImage(imagePath));
        }
        var channel = new AnimationChannel(images, Duration.seconds(0.5));
        AnimatedTexture texture = new AnimatedTexture(channel);
        return entityBuilder()
                .type(Types.PLAYER)
                .viewWithBBox(texture.loop())
                .scale(2, 2)
                .at(getAppWidth() / 2 - 75, getAppHeight() / 2 - 75)
                .with(new CollidableComponent(true))
                .with(new PlayerControl(texture))
                .with(new PlayerWeaponComponent())
                .with(new AmmoIndicatorComponent())
                .with(new Lifeindicator())
                .with(new medicnicator())
                .with(new luckyindicatorComponent())
                .build();
    }

    @Spawns("Weapon")
    public Entity newWeapon(SpawnData data) {
        String weaponName="MachineGun";
        return createWeaponEntity("Guns/1.png",weaponName,12);
    }

    @Spawns("Weapon2")
    public Entity newWeapon2(SpawnData data) {
        String weaponName="Sniper";
        return createWeaponEntity("Guns/2.png",weaponName,3);
    }

    private Entity createWeaponEntity(String imagePath, String weaponName,int ammo) {
        return entityBuilder()
                .type(Types.WEAPON)
                .scale(0.10,0.10)
                .viewWithBBox(imagePath)
                .with(new CollidableComponent(true))
                .with(new WeaponComponent(weaponName,ammo))
                .at(random(0,500),random(0,500))
                .build();
    }
    @Spawns("enemy")
    public Entity newEnemy(SpawnData data) {
        var images = new ArrayList<Image>();
        for (int i = 1; i <= 4; i++) {
            String imagePath = "enemy/1-Idle/" + i + ".png";
            images.add(FXGL.getAssetLoader().loadImage(imagePath));
        }
        var channel = new AnimationChannel(images, Duration.seconds(0.5));
        AnimatedTexture texture = new AnimatedTexture(channel);

        return entityBuilder()
                .type(Types.ENEMY)
                .viewWithBBox(texture.loop())
                .scale(2,2)
                .with(new CollidableComponent(true))
                .with(new OffscreenCleanComponent())
                .with(new Enemycontrol(texture))
                .at(random(0,500),random(0,500))
                .build();
    }
    @Spawns("Bullet")
    public Entity newBullet(SpawnData data) {
        Entity player = getGameWorld().getSingleton(Types.PLAYER);
        Entity weapon = player.getComponent(PlayerWeaponComponent.class).getWeapon();
        if (weapon.getComponent(WeaponComponent.class).getName().equals("MachineGun")) {
            return createBullet("Bullet/1.png", "MachineGunBullet", 750,0.02,0.02,25,230);
        } else {
            return createBullet("Bullet/2.png", "SniperBullet", 1500,0.5,0.5,-262,-75);
        }
    }

    private Entity createBullet(String image,String name, int speed,double scaleX, double scaleY, double posX, double posY) {
        Entity player = getGameWorld().getSingleton(Types.PLAYER);
        Entity weapon = player.getComponent(PlayerWeaponComponent.class).getWeapon();
        Point2D direction = getInput().getMousePositionWorld().subtract(player.getPosition());

        return entityBuilder()
                .type(Types.BULLET)
                .viewWithBBox(image)
                .with(new ProjectileComponent(direction, speed))
                .with(new BulletComponent(name))
                .with(new CollidableComponent(true))
                .with(new OffscreenCleanComponent())
                .scale(scaleX, scaleY)
                .rotate(45)
                .at(weapon.getPosition().subtract(posX, posY))
                .build();
    }

    public static void generateSurroundingWalls() {
        int screenWidth = getAppWidth()-25;
        int screenHeight = getAppHeight()-25;

        double wallWidth = 30.0;

        for (double y = -5; y <= screenHeight; y += wallWidth) {
            spawnWall(-5, y, wallWidth, wallWidth); // Lado izquierdo
            spawnWall(screenWidth - wallWidth, y, wallWidth, wallWidth); // Lado derecho
        }

        for (double x = -5; x <= screenWidth; x += wallWidth) {
            spawnWall(x, -5, wallWidth, wallWidth); // Parte superior
            spawnWall(x, screenHeight - wallWidth, wallWidth, wallWidth); // Parte inferior
        }
    }
    @Spawns("Wall")
    private static Entity spawnWall(double x, double y, double width, double height) {
        Entity wall = entityBuilder()
                .type(Types.WALL)
                .at(x, y)
                .view("Wall/1.png")
                .scale(0.1,0.1)
                .with(new CollidableComponent(true))
                .build();
        getGameWorld().addEntity(wall);
        return wall;
    }

    @Spawns("Portal")
    public Entity spawnPortal(SpawnData data){
        var images = new ArrayList<Image>();
        for (int i = 1; i <= 4; i++) {
            String imagePath = "portal/" + i + ".png";
            images.add(FXGL.getAssetLoader().loadImage(imagePath));
        }
        var channel = new AnimationChannel(images, Duration.seconds(0.5));
        AnimatedTexture texture = new AnimatedTexture(channel);

       return entityBuilder()
               .type(Types.PORTAL)
               .viewWithBBox(texture.loop())
               .at(0, 0)
               .scale(0.15,0.15)
               .with(new PortalControl(texture))
               .with(new CollidableComponent(true))
               .build();
    }


}
