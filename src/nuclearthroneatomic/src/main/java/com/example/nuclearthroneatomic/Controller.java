package com.example.nuclearthroneatomic;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.MenuItem;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.CollisionHandler;
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

import static com.almasb.fxgl.dsl.FXGL.*;
    public class Controller extends GameApplication {
        private final GameFactory gameFactory = new GameFactory();
        private Entity player;
        public static boolean isRealoding;

        private Music music;

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
        }

        @Override
        protected void onPreInit() {
            getSettings().setGlobalSoundVolume(0.1);
            getSettings().setGlobalMusicVolume(1.0);
            String uri = "musicformenus.mp3";
           loopBGM(uri);

        }


        @Override
        protected void initGame() {
            getGameWorld().addEntityFactory(gameFactory);
            loadLevel();
        }

        private void loadLevel(){

            getGameWorld().getEntities().forEach(Entity::removeFromWorld);

            switch (level){
                case 1:
                    player = spawn("Avatar", getAppWidth() / 2 - 15, getAppHeight() / 2 - 15);
                    spawn("Weapon");
                    for (int i = 0; i < 3; i++) {
                        spawn("enemy");
                    }
                    spawn("Portal");
                    break;
                case 2:
                    player = spawn("Avatar", getAppWidth() / 2 - 15, getAppHeight() / 2 - 15);
                    spawn("Weapon2");

                    for (int i = 0; i < 6; i++) {
                        spawn("enemy");
                    }

                    break;
                case 3:
                    player = spawn("Avatar", getAppWidth() / 2 - 15, getAppHeight() / 2 - 15);
                    spawn("Weapon");
                    spawn("Weapon2");

                    for (int i = 0; i < 12; i++) {
                        spawn("enemy");
                    }

                    break;
                default:
                   // uWin();
                    break;
            }

            GameFactory.generateSurroundingWalls();
        }
        @Override
        protected void initPhysics() {
            getPhysicsWorld().addCollisionHandler(new CollisionHandler(Types.PLAYER, Types.WEAPON) {
                @Override
                protected void onCollisionBegin(Entity player, Entity weapon) {
                    PlayerWeaponComponent weaponComponent = player.getComponent(PlayerWeaponComponent.class);
                    weaponComponent.setWeapon(weapon);
                    int ammoCount = 0;
                    String weaponName = weapon.getComponent(WeaponComponent.class).getName();
                    if (weaponName.equals("MachineGun")) {
                        ammoCount = 10;
                    } else if (weaponName.equals("Sniper")) {
                        ammoCount = 3;
                    }
                    weaponComponent.setAmmoCount(ammoCount);
                }
            });
            getPhysicsWorld().addCollisionHandler(new CollisionHandler(Types.BULLET, Types.WALL) {
                @Override
                protected void onCollisionBegin(Entity bullet, Entity wall) {
                    getGameWorld().removeEntity(wall);
                    getGameWorld().removeEntity(bullet);
                }
            });
                getPhysicsWorld().addCollisionHandler(new CollisionHandler(Types.PLAYER, Types.WALL) {
                    private double prevX;
                    private double prevY;

                    @Override
                    protected void onCollisionBegin(Entity player, Entity wall) {
                       prevX= player.getX();
                       prevY = player.getY();

                        if(player.getY()<51){ //Colision arriba
                            prevY+=5;
                        }
                        if(player.getX()<51){ //Colision izquierda
                            prevX +=5;
                        }
                        if(player.getY()>480){ //Colision abajo
                            prevY -=5;
                        }
                        if(player.getX()>690){ //Colision derecha
                            prevX -=5;
                        }
                    }

                    @Override
                    protected void onCollision(Entity player, Entity wall) {
                        player.setX(prevX);
                        player.setY(prevY);
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

        @Override
        protected void onUpdate(double tpf) {

        }

        public static void main(String[] args) {
            launch(args);
        }
}
