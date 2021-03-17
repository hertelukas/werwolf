package com.werwolf.game;

public class Configurations {

    private int witchnum = 0;
    private int seernum = 0;
    private int hunternum = 0;
    private int littleGirlnum = 0;
    private int sheriffnum = 0;
    private boolean isEnglish = true;

    public int getWitchnum() {
        return witchnum;
    }

    public int getSeernum() {
        return seernum;
    }

    public int getHunternum() {
        return hunternum;
    }

    public int getLittleGirlnum() {
        return littleGirlnum;
    }

    public int getSheriffnum() {
        return sheriffnum;
    }

    public boolean isEnglish(){return isEnglish;}

    public void setWitchnum(int witchnum) {
        this.witchnum = witchnum;
    }

    public void setSeernum(int seernum) {
        this.seernum = seernum;
    }

    public void setHunternum(int hunternum) {
        this.hunternum = hunternum;
    }

    public void setLittleGirlnum(int littleGirlnum) {
        this.littleGirlnum = littleGirlnum;
    }

    public void setSheriffnum(int sheriffnum) {
        this.sheriffnum = sheriffnum;
    }

    public void setEnglish(boolean english) {
        isEnglish = english;
    }
}