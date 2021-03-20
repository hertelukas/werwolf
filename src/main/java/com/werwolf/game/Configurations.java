package com.werwolf.game;

public class Configurations {

    private int preset = 0;
    private int seernum = 0;
    private int hunternum = 0;
    private int littleGirlnum = 0;
    private int sheriffnum = 0;
    private int jailornum = 0;
    private int bodyguardnum = 0;
    private int prositutesnum = 0;
    private int spynum = 0;
    private boolean major = true;
    private boolean serialKiller = false;
    private boolean isEnglish = true;
    private boolean playMusic = true;
    private boolean playAudio = true;
    private boolean showRole = false;
    private boolean write = false;

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

    public int getJailornum() {
        return jailornum;
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

    public void setJailornum(int jailornum) {
        this.jailornum = jailornum;
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

    public int getBodyguardnum() {
        return bodyguardnum;
    }

    public void setBodyguardnum(int bodyguardnum) {
        this.bodyguardnum = bodyguardnum;
    }

    public boolean isShowRole() {
        return showRole;
    }

    public void setShowRole(boolean showRole) {
        this.showRole = showRole;
    }

    public boolean canWrite() {
        return write;
    }

    public void setWrite(boolean write) {
        this.write = write;
    }

    public boolean getSerialkiller() {
        return serialKiller;
    }

    public void setSerialkiller(boolean serialKiller) {
        this.serialKiller = serialKiller;
    }

    public boolean getMajor() {
        return major;
    }

    public void setMajor(boolean major) {
        this.major = major;
    }

    /**
     * Sets all items according to the preset
     * witch, seer, hunter, littlegirl, sheriff, jailor, bodyguard, serial killer
     * @param num
     */
    public void setAll(int[] num) {
        if(num.length != 8)
            return;
        seernum = num[1];
        hunternum = num[2];
        littleGirlnum = num[3];
        sheriffnum = num[4];
        jailornum = num[5];
        bodyguardnum = num[6];
        if(num[7] == 1) serialKiller = true;
    }

    public int getPrositutesnum() {
        return prositutesnum;
    }

    public void setPrositutesnum(int prositutesnum) {
        this.prositutesnum = prositutesnum;
    }

    public int getSpynum() {
        return spynum;
    }

    public void setSpynum(int spynum) {
        this.spynum = spynum;
    }
}