package model;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;

public class PlayerWeaponComponent extends Component{
    private Entity weapon;
    private int ammoCount;
    public void setWeapon(Entity weapon) {
        this.weapon = weapon;
    }

    public Entity getWeapon() {
        return weapon;
    }

    public PlayerWeaponComponent() {
        ammoCount = 0;
    }

    public int getAmmoCount() {
        return ammoCount;
    }

    public void decreaseAmmoCount() {
        ammoCount--;
    }

    public void setAmmoCount(int ammoCount) {
        this.ammoCount = ammoCount;
    }
}
