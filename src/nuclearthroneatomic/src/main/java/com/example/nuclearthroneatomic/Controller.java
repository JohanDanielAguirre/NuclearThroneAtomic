package com.example.nuclearthroneatomic;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.CollisionHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import model.*;

import static com.almasb.fxgl.dsl.FXGL.*;
    public class Controller extends GameApplication {
        private final GameFactory gameFactory = new GameFactory();
        private Entity player;
        public static boolean isRealoding;
        @Override
        protected void initSettings(GameSettings settings) {
        }
        @Override
        protected void initGame() {
            getGameWorld().addEntityFactory(gameFactory);
            player = spawn("Avatar", getAppWidth() / 2 - 15, getAppHeight() / 2 - 15);
            spawn("Weapon");
            spawn("Weapon2");
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
            getPhysicsWorld().addCollisionHandler(new CollisionHandler(Types.WALL, Types.PLAYER) {
                @Override
                protected void onCollisionBegin(Entity player, Entity wall) {
                }
            });
        }
        @Override
        protected void initInput() {
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
        public static void main(String[] args) {
            launch(args);
        }
}
