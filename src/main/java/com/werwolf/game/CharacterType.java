package com.werwolf.game;

public enum CharacterType {
    Villager(false), Werewolf(true), Seer(true), Witch(true), LittleGirl(true), Sheriff(true), Hunter(false);

    private final boolean canVote;

    private CharacterType(boolean canVote) {
        this.canVote = canVote;
    }

    public boolean canVote() {
        return canVote;
    }
}
