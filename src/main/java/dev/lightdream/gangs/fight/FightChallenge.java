package dev.lightdream.gangs.fight;

import dev.lightdream.gangs.gang.Gang;

public class FightChallenge {
    private Gang challengerGang;
    private Gang challengedGang;
    private int members;
    private double money;

    public FightChallenge(Gang var1, Gang var2, int var3, double var4) {
        this.challengerGang = var1;
        this.challengedGang = var2;
        this.members = var3;
        this.money = var4;
    }

    public Gang getChallengerGang() {
        return this.challengerGang;
    }

    public void setChallengerGang(Gang var1) {
        this.challengerGang = var1;
    }

    public Gang getChallengedGang() {
        return this.challengedGang;
    }

    public void setChallengedGang(Gang var1) {
        this.challengedGang = var1;
    }

    public int getMembers() {
        return this.members;
    }

    public void setMembers(int var1) {
        this.members = var1;
    }

    public double getMoney() {
        return this.money;
    }

    public void setMoney(double var1) {
        this.money = var1;
    }
}
