package model;

import com.almasb.fxgl.entity.component.Component;

public class BulletComponent extends Component {
    private String name;

    private TypeBullet typeBullet;

    public BulletComponent(TypeBullet typeBullet) {
        this.typeBullet =  typeBullet;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TypeBullet getTypeBullet() {
        return typeBullet;
    }

    public void setTypeBullet(TypeBullet typeBullet) {
        this.typeBullet = typeBullet;
    }
}
