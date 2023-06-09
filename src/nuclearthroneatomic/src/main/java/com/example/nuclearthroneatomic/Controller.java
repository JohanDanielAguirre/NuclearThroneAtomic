package com.example.nuclearthroneatomic;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.MenuItem;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.GameWorld;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.time.LocalTimer;
import javafx.geometry.Point2D;
import javafx.scene.ImageCursor;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.util.Duration;
import model.*;
import com.almasb.fxgl.audio.Music;


import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.EnumSet;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static com.almasb.fxgl.dsl.FXGL.*;
    public class Controller extends GameApplication {
        private final GameFactory gameFactory = new GameFactory();
        private Entity player;

        private ArrayList<Entity> enemies=new ArrayList<>();
        public static boolean isRealoding;
        public static boolean portalSpawned = false;
        public static boolean nextLevel = false;

        private int level = 1;

        private int bulletsEnemies = 0;

        protected LocalTimer attackTimer;
        protected Duration nextAttack = Duration.seconds(2);

        @Override
        protected void initSettings(GameSettings settings) {
            settings.setTitle("Nuclear Throne Atomic");
            settings.setMainMenuEnabled(true);
            settings.setGameMenuEnabled(true);
            //settings.setIntroEnabled(true);
            settings.setFullScreenAllowed(true);
            settings.setEnabledMenuItems(EnumSet.of(MenuItem.EXTRA));
            settings.getCredits().addAll(Arrays.asList(
                    "Designer . . . . . . . Johan Daniel Aguirre",
                    "Program Team . . . . . . Juan Sebastián Libreros",
                    "                          Juan Jose Barrera",
                    "                          Johan Daniel Aguirre",
                    "El mas lindo . . . . . . Juan Sebastián Libreros",
                    "Master Thief . . . . . . Johan Daniel Aguirre"
            ));
            settings.setFullScreenFromStart(true);
        }

        @Override
        protected void onPreInit() {
            getSettings().setGlobalSoundVolume(0.1);
            getSettings().setGlobalMusicVolume(1.0);

            loopBGM("muiscformenus.mp3");

        }


        @Override
        protected void initGame() {
            getGameWorld().addEntityFactory(gameFactory);

            loadLevel();
        }

        private void cleanUpLevel(){
            getGameWorld().getEntitiesByType(
                    Types.WEAPON,
                    Types.ENEMY,
                    Types.WALL,
                    Types.PLAYER,
                    Types.PORTAL,
                    Types.BULLET,
                    Types.BACKGROUND
            ).forEach(Entity::removeFromWorld);
            AmmoIndicatorComponent ammoIndicatorComponent = player.getComponent(AmmoIndicatorComponent.class);
            WeaponIndicator weaponIndicator = player.getComponent(WeaponIndicator.class);
            luckyindicatorComponent luckyComponent = player.getComponent(luckyindicatorComponent.class);
            medicnicator medicineIndicator = player.getComponent(medicnicator.class);
            Lifeindicator lifeindicator = player.getComponent(Lifeindicator.class);
            if (ammoIndicatorComponent != null) {
                ammoIndicatorComponent.clearUI();
            }
            if(luckyComponent!=null){
                luckyComponent.clearUI();
            }
            if (medicineIndicator!=null){
                medicineIndicator.clearUI();
            }
            if(lifeindicator!=null){
                lifeindicator.clearUI();
            }
            if (weaponIndicator != null){
                weaponIndicator.clearUI();
            }
            isRealoding=false;
            enemies.clear();
        }

        private void loadLevel(){

            ImageCursor customCursor;

            switch (level){
                case 1:
                    spawn("BackgroundLevel1");
                    player = spawn("Avatar", getAppWidth() / 2 - 5, getAppHeight() / 2 - 5);
                    spawn("Weapon2");

                    GameFactory.generateWall(100,125,200,50);
                    GameFactory.generateWall(200,125,200,50);
                    GameFactory.generateWall(100,425,200,50);
                    GameFactory.generateWall(200,525,200,50);

                    for (int i = 0; i < 3; i++) {
                        enemies.add(spawn("enemy"));
                    }
                    PlayerControl.setMedkits(3);
                    PlayerControl.setLucky(1);
                    customCursor = new ImageCursor(FXGL.getAssetLoader().loadImage("mouse/mousecomplete.png"));
                    FXGL.getGameScene().getRoot().setCursor(customCursor);
                    break;
                case 2:
                    spawn("BackgroundLevel2");
                    player = spawn("Avatar", getAppWidth() / 2 - 5, getAppHeight() / 2 - 5);
                    spawn("Weapon");
                    PlayerControl.setMedkits(3);
                    PlayerControl.setLucky(1);
                    customCursor = new ImageCursor(FXGL.getAssetLoader().loadImage("mouse/mousepresecondlevel.png"));
                    FXGL.getGameScene().getRoot().setCursor(customCursor);
                    for (int i = 0; i < 6; i++) {
                        enemies.add(spawn("enemy"));
                    }

                    break;
                case 3:
                    spawn("BackgroundLevel3");
                    player = spawn("Avatar", getAppWidth() / 2 - 5, getAppHeight() / 2 - 5);
                    spawn("Weapon");
                    spawn("Weapon2");
                    PlayerControl.setMedkits(3);
                    PlayerControl.setLucky(1);
                    customCursor = new ImageCursor(FXGL.getAssetLoader().loadImage("mouse/mouseprefinallevel.png"));
                    FXGL.getGameScene().getRoot().setCursor(customCursor);
                    for (int i = 0; i < 12; i++) {
                        enemies.add(spawn("enemy"));
                    }

                    break;
                default:
                   uWin();
                    break;
            }

            GameFactory.generateSurroundingWalls();
        }
        @Override
        protected void initPhysics() {
            getPhysicsWorld().addCollisionHandler(new CollisionHandler(Types.PLAYER, Types.WEAPON) {
                @Override
                protected void onCollisionBegin(Entity player, Entity weapon) {
                    if (weapon.getComponent(WeaponComponent.class).getEnemy() != null) {
                        return;
                    }
                    PlayerWeaponComponent weaponComponent = player.getComponent(PlayerWeaponComponent.class);
                    weaponComponent.setWeapon(weapon);
                    int ammoCount = 0;
                    String weaponName = weapon.getComponent(WeaponComponent.class).getName();
                    if (weaponName.equals("MachineGun")) {
                        ammoCount = 12;
                    } else if (weaponName.equals("Sniper")) {
                        ammoCount = 5;
                    }
                    weaponComponent.setAmmoCount(ammoCount);
                }
            });
            getPhysicsWorld().addCollisionHandler(new CollisionHandler(Types.BULLET, Types.WALL) {
                @Override
                protected void onCollisionBegin(Entity bullet, Entity wall) {
                    TypeBullet typeBullet = bullet.getComponent(BulletComponent.class).getTypeBullet();
                    if (typeBullet == TypeBullet.ENEMY) {
                        getGameWorld().removeEntity(wall);
                        getGameWorld().removeEntity(bullet);
                        return;
                    }

                    getGameWorld().removeEntity(wall);
                    getGameWorld().removeEntity(bullet);
                }
            });
            getPhysicsWorld().addCollisionHandler(new CollisionHandler(Types.PLAYER, Types.WALL) {
                private double prevX;
                private double prevY;

                @Override
                protected void onCollisionBegin(Entity player, Entity wall) {
                    prevX = player.getX();
                    prevY = player.getY();

                    if (player.getY() < 51) { //Colision arriba
                        prevY += 5;
                    }
                    if (player.getX() < 51) { //Colision izquierda
                        prevX += 5;
                    }
                    if (player.getY() > 480) { //Colision abajo
                        prevY -= 5;
                    }
                    if (player.getX() > 690) { //Colision derecha
                        prevX -= 5;
                    }
                }

                @Override
                protected void onCollision(Entity player, Entity wall) {
                    player.setX(prevX);
                    player.setY(prevY);


                }
            });

            getPhysicsWorld().addCollisionHandler(new CollisionHandler(Types.PLAYER, Types.BULLET) {
                @Override
                protected void onCollision(Entity player, Entity bullet) {
                   TypeBullet typeBullet= bullet.getComponent(BulletComponent.class).getTypeBullet();
                   if(typeBullet==TypeBullet.ENEMY){
                       PlayerControl.setLife(PlayerControl.getLife()-1);
                       bullet.removeFromWorld();
                   }
                }
            });
            getPhysicsWorld().addCollisionHandler(new CollisionHandler(Types.ENEMY, Types.BULLET) {
                @Override
                protected void onCollision(Entity enemy, Entity bullet) {
                    TypeBullet typeBullet = bullet.getComponent(BulletComponent.class).getTypeBullet();
                    if (typeBullet == TypeBullet.PLAYER) {
                        for (int i = 0; i < enemies.size(); i++) {
                            if (enemies.get(i).equals(enemy)) {
                                enemies.get(i).getComponent(Enemycontrol.class).setLife();
                                bullet.removeFromWorld();
                                return;
                            }
                        }
                    }
                }
            });

            getPhysicsWorld().addCollisionHandler(new CollisionHandler(Types.PLAYER, Types.PORTAL){
                @Override
                protected void onCollision(Entity player, Entity portal){
                    nextLevel = true;
                }
            });

            getPhysicsWorld().addCollisionHandler(new CollisionHandler(Types.ENEMY, Types.WALL) {
                private double prevX;
                private double prevY;


                @Override
                protected void onCollisionBegin(Entity enemy, Entity wall) {
                    prevX = enemy.getX();
                    prevY = enemy.getY();

                    attackTimer = FXGL.newLocalTimer();
                    attackTimer.capture();

                    if (enemy.getY() < 51) { //Colision arriba
                        prevY += 5;
                    }
                    if (enemy.getX() < 51) { //Colision izquierda
                        prevX += 5;
                    }
                    if (enemy.getY() > 480) { //Colision abajo
                        prevY -= 5;
                    }
                    if (enemy.getX() > 690) { //Colision derecha
                        prevX -= 5;
                    }
                }

                @Override
                protected void onCollision(Entity enemy, Entity wall) {

                    enemy.setX(prevX);
                    enemy.setY(prevY);
                    if (attackTimer.elapsed(nextAttack)) {
                        if (FXGLMath.randomBoolean(0.3f)){
                            enemy.getComponent(Enemycontrol.class).attackPlayer();
                        }
                        nextAttack = Duration.seconds(2 * Math.random());
                        attackTimer.capture();
                    }

                }
            });


        }
        @Override
        protected void initInput() {
                getInput().addAction(new UserAction("lucky") {
                    @Override
                    protected void onAction() {
                        player.getComponent(PlayerControl.class).lucky();
                    }
                }, KeyCode.Q);
                getInput().addAction(new UserAction("health") {
                    @Override
                    protected void onActionBegin() {
                        player.getComponent(PlayerControl.class).health();
                    }
                }, KeyCode.F);
            getInput().addAction(new UserAction("Right") {
                @Override
                protected void onAction() {
                    player.getComponent(PlayerControl.class).moveRight();
                }
            }, KeyCode.D);

            getInput().addAction(new UserAction("Left") {
                @Override
                protected void onAction() {
                    player.getComponent(PlayerControl.class).moveLeft();
                }
            }, KeyCode.A);

            getInput().addAction(new UserAction("Up") {
                @Override
                protected void onAction() {
                    player.getComponent(PlayerControl.class).moveUp();
                }
            }, KeyCode.W);

            getInput().addAction(new UserAction("Down") {
                @Override
                protected void onAction() {
                    player.getComponent(PlayerControl.class).moveDown();
                }
            }, KeyCode.S);

            getInput().addAction(new UserAction("Reload") {
                @Override
                protected void onAction() {
                    PlayerWeaponComponent weaponComponent = player.getComponent(PlayerWeaponComponent.class);
                    if (weaponComponent.getWeapon() != null) {
                        if (!isRealoding) {
                            player.getComponent(PlayerControl.class).reload();
                            isRealoding = true;
                        }
                    }
                }
            }, MouseButton.SECONDARY);
            getInput().addAction(new UserAction("Shoot") {
                @Override
                protected void onActionBegin() {
                    PlayerWeaponComponent weaponComponent = player.getComponent(PlayerWeaponComponent.class);
                    if (weaponComponent.getWeapon() != null) {
                        if (weaponComponent.getAmmoCount() == 1 && !isRealoding) {
                            player.getComponent(PlayerControl.class).reload();
                        }
                        if (weaponComponent.getAmmoCount() > 0 && !isRealoding) {
                            weaponComponent.decreaseAmmoCount();
                            spawn("Bullet");
                            System.out.println(weaponComponent.getAmmoCount());
                        } else {
                            if (!isRealoding) {
                                player.getComponent(PlayerControl.class).reload();
                                isRealoding = true;
                            }
                        }
                    }
                }
            }, MouseButton.PRIMARY);
        }

        public String frase(){
            String m;
            int a= (int) (Math.random() * 14);
            switch(a){
                case 1:
                    m="No hay nada más emocionante que te disparen y no te den.";
                    break;
                case 2:
                    m="Hasta un ejercito de mil hombres cae\n cuando un hombre piensa como mil de ellos";
                    break;
                case 3:
                    m="Esos salvajes murieron duramente, como lobos heridos y acorralados.\n Eran sucios, ruidosos y olían. Y yo los quería.";
                    break;
                case 4:
                    m="La muerte de un hombre es una tragedia.\n La muerte de millones es estadística.";
                    break;
                case 5:
                    m="No dependas de nadie en este mundo...\n Porque hasta tu propia sombra te abandona en la oscuridad.";
                    break;
                case 6:
                    m="El hombre adecuado en el sitio equivocado\n puede cambiar el rumbo del mundo.";
                    break;
                case 7:
                    m="Debemos luchar por los que viven\n y por los que aún no han nacido.";
                    break;
                case 8:
                    m="Un hombre elige, un esclavo obedece.";
                    break;
                case 9:
                    m="Si de verdad existe el mal en este mundo,\n éste reside en el mismo corazón del hombre.";
                    break;
                case 10:
                    m="Si la historia debe cambiar, que cambie. Si el mundo es destruido, que así sea.\n Si mi destino es morir, simplemente me reiré de él.";
                    break;
                case 11:
                    m="Nadie es innecesario.";
                    break;
                case 12:
                    m="¡Es mejor morir de pie que vivir de rodillas!";
                    break;
                case 13:
                    m="Adios... Mundo cruel";
                    break;
                case 14:
                    m="No importa cuán oscura que sea la noche, el día siempre vuelve a aparecer\n y nuestro viaje comienza una vez más.";
                    break;
                default:
                    m="¡Detras de ti Comediante!";
                    break;
            }
            return m;
        }

        private void showGameOver() {
            getDialogService().showConfirmationBox("Game Over: "+frase() +"\n Play Again?", yes -> {
                if (yes) {
                    PlayerControl.setLife(3);
                    cleanUpLevel();
                    level = 1;
                    loadLevel();
                } else {
                    getGameController().exit();
                }
            });
        }

        private void uWin() {
            getDialogService().showConfirmationBox("You win: Sos el mejor" +"Play Again?", yes -> {
                if (yes) {
                    PlayerControl.setLife(3);
                    cleanUpLevel();
                    level = 1;
                    loadLevel();
                } else {
                    getGameController().exit();
                }
            });
        }


        @Override
        protected void onUpdate(double tpf) {
            //Pasar de nivel por el portal,se hace por medio de una colision

            if (PlayerControl.getLife()<= 0){
                showGameOver();
            }

            if (getGameWorld().getEntitiesByType(Types.ENEMY).size() == 0 && !portalSpawned){
                spawn("Portal");
                portalSpawned = true;
            }

            if (nextLevel){
                portalSpawned = false;
                cleanUpLevel();
                level++;
                nextLevel = false;
                loadLevel();

            }
        }

        public static void main(String[] args) {
            launch(args);
        }
}
