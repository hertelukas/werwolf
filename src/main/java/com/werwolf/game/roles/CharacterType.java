package com.werwolf.game.roles;

public enum CharacterType {
    Villager(false, false, 10, Team.Village),
    Werewolf(true,true, 20, Team.Werewolf),
    Seer(true, false, 10,  Team.Village),
    LittleGirl(false, true, 10,  Team.Village),
    Sheriff(true, false, 10,  Team.Village),
    Hunter(true,false, 10,  Team.Village),
    Jailor(true, false, 1,  Team.Village),
    Bodyguard(true, false, 5,  Team.Village),
    SerialKiller(true, false, 15, Team.SerialKiller),
    Prostitute(true, false, 9, Team.Village),
    Spy(false, false, 10, Team.Village);

    private final boolean canVote;
    private final boolean canSeeWWChannel;
    //Je niedriger die Priorität, desto früher ist der Carackter am Zug
    private final int priority;
    /*
    0:  Fürs Dorf
    1:  Für die Werewölfe
     */
    private final Team team;

    CharacterType(boolean canVote, boolean canSeeWWChannel, int priority, Team team) {
        this.canVote = canVote;
        this.canSeeWWChannel = canSeeWWChannel;
        this.priority = priority;
        this.team = team;
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

    public Team getTeam() {
        return team;
    }
}