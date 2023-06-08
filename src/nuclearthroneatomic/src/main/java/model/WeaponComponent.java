package model;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;

public class WeaponComponent extends Component {
    private String name;
    private int ammo;
    private Entity bullet;
    public static boolean isReloading;

    private Entity enemy;

    public WeaponComponent(String name, int ammo) {
        this.ammo = ammo;
        this.name = name;
    }

    public Entity getEnemy() {
        return enemy;
    }

    public void setEnemy(Entity enemy) {
        this.enemy = enemy;
    }

    public String getName() {
        return name;
    }

    public Entity getBullet() {
        return bullet;
    }

    public void setBullet(Entity bullet) {
        this.bullet = bullet;
    }

    public int getAmmo() {
        return ammo;
    }
    public boolean isReloading() {
        return isReloading;
    }

    public void setReloading(boolean reloading) {
        isReloading = reloading;
    }
}
