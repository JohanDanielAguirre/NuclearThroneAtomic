package model;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import javafx.scene.input.KeyCode;

public class Player extends Component {
    private int positionx;
    private int positiony;
    private int speed;
    private int health;
    private Weapon weapon1;
    private Weapon weapon2;
    private Power p;

    public Player(int positionx, int positiony, int speed, int health, Weapon weapon1, Weapon weapon2, Power p) {
        this.positionx = positionx;
        this.positiony = positiony;
        this.speed = speed;
        this.health = health;
        this.weapon1 = weapon1;
        this.weapon2 = weapon2;
        this.p = p;
    }


    public int getPositionx() {
        return positionx;
    }

    public void setPositionx(int positionx) {
        this.positionx = positionx;
    }

    public int getPositiony() {
        return positiony;
    }

    public void setPositiony(int positiony) {
        this.positiony = positiony;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public Weapon getWeapon1() {
        return weapon1;
    }

    public void setWeapon1(Weapon weapon1) {
        this.weapon1 = weapon1;
    }

    public Weapon getWeapon2() {
        return weapon2;
    }

    public void setWeapon2(Weapon weapon2) {
        this.weapon2 = weapon2;
    }

    public Power getP() {
        return p;
    }

    public void setP(Power p) {
        this.p = p;
    }

    @Override
    public void onUpdate(double tpf) {
        if (FXGL.getInput().equals(KeyCode.W)) {
            entity.translateY(-speed);
        }
        if (FXGL.getInput().equals(KeyCode.S)) {
            entity.translateY(speed);
        }
        if (FXGL.getInput().equals(KeyCode.A)) {
            entity.translateX(-speed);
        }
        if (FXGL.getInput().equals(KeyCode.D)) {
            entity.translateX(speed);
        }
    }
}
