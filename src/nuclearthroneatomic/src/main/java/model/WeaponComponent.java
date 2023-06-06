package model;

import com.almasb.fxgl.entity.component.Component;

public class WeaponComponent extends Component {
    private String name;
    private int ammo;

    public static boolean isReloading;

    public WeaponComponent(String name, int ammo) {
        this.ammo = ammo;
        this.name = name;
    }

    public String getName() {
        return name;
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
