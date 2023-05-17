package model;
public class Weapon {
    private int damage;
    private int munition;
    private int range;
    public Weapon(int damage, int munition, int range) {
        this.damage = damage;
        this.munition = munition;
        this.range = range;
    }

    public int getDamage() {
        return damage;
    }

    public int getMunition() {
        return munition;
    }

    public int getRange() {
        return range;
    }


    public void setDamage(int damage) {
        this.damage = damage;
    }

    public void setMunition(int munition) {
        this.munition = munition;
    }

    public void setRange(int range) {
        this.range = range;
    }

}
