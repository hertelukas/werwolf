package com.werwolf.game.roles;

public enum CharacterType {
    Villager(false, false, 1),
    Werewolf(true,true, 1),
    Seer(true, false, 1),
    Witch(true, false, 1),
    LittleGirl(false, true, 1),
    Sheriff(true, false, 1),
    Hunter(false,false, 1),
    Jailor(true, false, 10),
    Bodyguard(true, false, 1);

    private final boolean canVote;
    private final boolean canSeeWWChannel;
    //Je höher die Priorität, desto früher ist der Carackter am Zug
    private final int priority;

    private CharacterType(boolean canVote, boolean canSeeWWChannel, int priority) {
        this.canVote = canVote;
        this.canSeeWWChannel = canSeeWWChannel;
        this.priority = priority;
    }

    //Getter und Setter
    public boolean canVote() {
        return canVote;
    }

    public boolean isCanSeeWWChannel() {
        return canSeeWWChannel;
    }

    public int getPriority() {
        return priority;
    }
}