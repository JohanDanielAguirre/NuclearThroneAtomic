package model;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;

public class EnemyWeaponComponent extends Component {
    private Entity weapon;

    public void setWeapon(Entity weapon) {
        this.weapon = weapon;
    }

    public Entity getWeapon() {
        return weapon;
    }

}
