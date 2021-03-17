package com.werwolf.game;

public enum CharacterType {
    Villager(false, false),
    Werewolf(true,true),
    Seer(true, false),
    Witch(true, false),
    LittleGirl(false, true),
    Sheriff(true, false),
    Hunter(false,false),
    Jailor(true, false);

    private final boolean canVote;
    private final boolean canSeeWWChannel;

    private CharacterType(boolean canVote, boolean canSeeWWChannel) {
        this.canVote = canVote;
        this.canSeeWWChannel = canSeeWWChannel;
    }

    //Getter und Setter
    boolean canVote() {
        return canVote;
    }

    public boolean isCanSeeWWChannel() {
        return canSeeWWChannel;
    }
}
