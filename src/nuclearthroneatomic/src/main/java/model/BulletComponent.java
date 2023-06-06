package model;

import com.almasb.fxgl.entity.component.Component;

public class BulletComponent extends Component {
    private String name;

    public BulletComponent(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
