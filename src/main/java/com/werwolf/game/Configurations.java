package com.werwolf.game;

public class Configurations {

    private int preset = 0;
    private int witchnum = 0;
    private int seernum = 0;
    private int hunternum = 0;
    private int littleGirlnum = 0;
    private int sheriffnum = 0;
    private int jailernum = 0;
    private boolean isEnglish = true;
    private boolean playMusic = true;
    private boolean playAudio = true;

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

    public int getJailernum() {
        return jailernum;
    }

    public boolean isEnglish() {
        return isEnglish;
    }

    public boolean playAudio() {
        return playAudio;
    }

    public boolean playMusic() {
        return playMusic;
    }

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

    public void setJailernum(int jailernum) {
        this.jailernum = jailernum;
    }

    public void setEnglish(boolean english) {
        isEnglish = english;
    }

    public void setPlayAudio(boolean playAudio) {
        this.playAudio = playAudio;
    }

    public void setPlayMusic(boolean playMusic) {
        this.playMusic = playMusic;
    }

    public int getPreset() {
        return preset;
    }

    public void setPreset(int preset) {
        this.preset = preset;
    }
}