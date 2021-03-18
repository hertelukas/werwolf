package com.werwolf.game.roles;

public enum CharacterType {
    Villager(false, false, 1, 0),
    Werewolf(true,true, 1, 1),
    Seer(true, false, 1, 0),
    Witch(true, false, 1, 0),
    LittleGirl(false, true, 1, 0),
    Sheriff(true, false, 1, 0),
    Hunter(false,false, 1, 0),
    Jailor(true, false, 10, 0),
    Bodyguard(true, false, 1, 0);

    private final boolean canVote;
    private final boolean canSeeWWChannel;
    //Je höher die Priorität, desto früher ist der Carackter am Zug
    private final int priority;
    /*
    0:  Fürs Dorf
    1:  Für die Werewölfe
     */
    private final int good_bad_special;

    private CharacterType(boolean canVote, boolean canSeeWWChannel, int priority, int good_bad_special) {
        this.canVote = canVote;
        this.canSeeWWChannel = canSeeWWChannel;
        this.priority = priority;
        this.good_bad_special = good_bad_special;
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

    public int getGood_bad_special() {
        return good_bad_special;
    }
}