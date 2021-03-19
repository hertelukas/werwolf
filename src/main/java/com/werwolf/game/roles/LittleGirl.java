package com.werwolf.game.roles;

public class LittleGirl extends Villager {

    public LittleGirl(Player player) {
        super(player);
        this.username = player.username;
        this.id = player.id;
        this.isAlive = player.isAlive;
        this.user = player.user;
        setCharacterType(CharacterType.LittleGirl);
    }

    /**
     * Mädchen kann in der Nacht nicht voten, weshalb sie weder die vote noch die reset Methode überschreibt
     */
}