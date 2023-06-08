package com.example.nuclearthroneatomic;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.MenuItem;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.GameWorld;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.CollisionHandler;
import javafx.scene.ImageCursor;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
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
                    Types.PORTAL
            ).forEach(Entity::removeFromWorld);
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
                    for (int i = 0; i < 3; i++) {
                        enemies.add(spawn("enemy"));
                    }
                    customCursor = new ImageCursor(FXGL.getAssetLoader().loadImage("mouse/mouseprefirstlevelboss.png"));
                    FXGL.getGameScene().getRoot().setCursor(customCursor);
                    break;
                case 2:
                    spawn("BackgroundLevel2");
                    player = spawn("Avatar", getAppWidth() / 2 - 5, getAppHeight() / 2 - 5);
                    spawn("Weapon");
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

            getPhysicsWorld().addCollisionHandler(new CollisionHandler(Types.ENEMY,Types.ENEMY) {
                private double prevX;
                private double prevY;


                @Override
                protected void onCollisionBegin(Entity enemy, Entity enemy1) {
                    prevX = enemy.getX();
                    prevY = enemy.getY();

                }

                @Override
                protected void onCollision(Entity enemy, Entity enemy1) {
                    enemy.setX(prevX);
                    enemy.setY(prevY);
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
                    m="Hasta un ejercito de mil hombres cae cuando un hombre piensa como mil de ellos";
                    break;
                case 3:
                    m="Esos salvajes murieron duramente, como lobos heridos y acorralados. Eran sucios, ruidosos y olían. Y yo los quería.";
                    break;
                case 4:
                    m="La muerte de un hombre es una tragedia. La muerte de millones es estadística.";
                    break;
                case 5:
                    m="No dependas de nadie en este mundo... Porque hasta tu propia sombra te abandona en la oscuridad.";
                    break;
                case 6:
                    m="El hombre adecuado en el sitio equivocado puede cambiar el rumbo del mundo.";
                    break;
                case 7:
                    m="Debemos luchar por los que viven y por los que aún no han nacido.";
                    break;
                case 8:
                    m="Un hombre elige, un esclavo obedece.";
                    break;
                case 9:
                    m="Si de verdad existe el mal en este mundo, éste reside en el mismo corazón del hombre.";
                    break;
                case 10:
                    m="Si la historia debe cambiar, que cambie. Si el mundo es destruido, que así sea. Si mi destino es morir, simplemente me reiré de él.";
                    break;
                case 11:
                    m="Nadie es innecesario.";
                    break;
                case 12:
                    m="¡Es mejor morir de pie que vivir de rodillas!";
                    break;
                case 13:
                    m="Lo correcto... ¿Qué es? Si haces lo correcto... ¿Haces... feliz... a todo el mundo?";
                    break;
                case 14:
                    m="No importa cuán oscura que sea la noche, el día siempre vuelve a aparecer y nuestro viaje comienza una vez más.";
                    break;
                default:
                    m="¡Detras de ti imbecil!";
                    break;
            }
            return m;
        }

        private void showGameOver() {
            getDialogService().showConfirmationBox("Game Over: "+ frase() +"Play Again?", yes -> {
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
            getDialogService().showConfirmationBox("You win: "+ frase() +"Play Again?", yes -> {
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
