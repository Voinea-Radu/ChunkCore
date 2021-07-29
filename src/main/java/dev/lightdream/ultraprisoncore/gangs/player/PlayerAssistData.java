package dev.lightdream.ultraprisoncore.gangs.player;
public class PlayerAssistData {
    private double damage;
    private long lastDamage;

    public PlayerAssistData(double var1, long var3) {
        this.damage = var1;
        this.lastDamage = var3;
    }

    public double getDamage() {
        return this.damage;
    }

    public void setDamage(double var1) {
        this.damage = var1;
    }

    public void addDamage(double var1) {
        this.damage += var1;
    }

    public long getLastDamage() {
        return this.lastDamage;
    }

    public void setLastDamage(long var1) {
        this.lastDamage = var1;
    }
}
