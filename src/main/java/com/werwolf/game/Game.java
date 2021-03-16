package com.werwolf.game;

import com.werwolf.core.handler.AudioHandler;
import com.werwolf.core.handler.Handler;
import com.werwolf.game.controller.GameController;
import com.werwolf.game.controller.VotingController;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.internal.entities.GuildImpl;
import net.dv8tion.jda.internal.entities.MemberImpl;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class Game {

    private final static Logger LOGGER = LoggerFactory.getLogger(Game.class);

    private List<Player> players = new ArrayList<>();
    private final List<Long> bannedPlayerIds = new ArrayList<>();
    private final List<Werewolf> werewolves = new ArrayList<>();
    private Player host;
    private final long channelID;
    private final TextChannel channel;
    private long voiceChannelID = 0;
    private long wolfChannelID;
    private long mainGameMessage;
    private final GameController controller = new GameController(this);
    private final Guild guild;
    private boolean tumMode = false;

    public Game(TextChannel channel, Player host, Guild guild, Player... players) {
        this.channel = channel;
        this.channelID = channel.getIdLong();
        this.host = host;
        if (players.length > 0) this.players = Arrays.asList(players);
        this.players.add(host);
        this.guild = guild;
    }

    public boolean start() {
        if (controller.isActive()) {
            return false;
        }

        controller.setActive(true);

        //Story
        controller.sendIntroMessage();
        if (spawnWerewolves()) controller.setActive(true);

        //If there is a voice channel specified, we try to join
        if (voiceChannelID != 0) {
            VoiceChannel voiceChannel = guild.getVoiceChannelById(voiceChannelID);
            if(voiceChannel != null)
                AudioHandler.getAudioHandler().loadAndPlay(voiceChannel, "Never.mp3", true, true);
        }

        LOGGER.info("Erste Nacht gestartet");
        controller.nextNight();

        return true;
    }

    public boolean stop() {
        try {
            Handler.deleteGame(channelID);
            Objects.requireNonNull(guild.getTextChannelById(wolfChannelID)).delete().queue();
            //Close audio connection no matter what
            guild.getAudioManager().closeAudioConnection();
            LOGGER.info("Spiel erfolgreich gestoppt");
        }
        catch (Exception e){
           LOGGER.info("Failed to remove werewolf channel: " + e.getMessage());
        }
        return true;
    }

    public PlayerListStatus addPlayer(@NotNull Player player) {
        if(isActive()) return PlayerListStatus.unsuccessful;
        if (hasPlayer(player.getId())) return PlayerListStatus.contains;
        if (isBanned(player.getId())) return PlayerListStatus.isBanned;
        players.add(player);
        return PlayerListStatus.successful;
    }

    public PlayerListStatus removePlayer(@NotNull Player player) {
        return removePlayer(player.getId());
    }

    public PlayerListStatus removePlayer(long id) {
        if (!hasPlayer(id)) return PlayerListStatus.containsNot;
        else {
            for (Player player : players) {
                if (player.getId() == id) {
                    players.remove(player);
                    if (player.getId() == host.getId() && players.size() > 0) {
                        host = players.get(0);
                    }
                    return PlayerListStatus.successful;
                }
            }
        }
        return PlayerListStatus.unsuccessful;
    }

    public PlayerListStatus banPlayer(@NotNull Player player) {
        return banPlayer(player.getId());
    }

    public PlayerListStatus banPlayer(long id) {
        if (isBanned(id)) return PlayerListStatus.contains;
        bannedPlayerIds.add(id);
        removePlayer(id);
        return PlayerListStatus.successful;
    }

    public PlayerListStatus pardonPlayer(@NotNull Player player) {
        return pardonPlayer(player.getId());
    }

    public PlayerListStatus pardonPlayer(long id) {
        if (isBanned(id)) {
            bannedPlayerIds.remove(id);
            return PlayerListStatus.successful;
        }
        return PlayerListStatus.containsNot;
    }

    public boolean hasPlayer(long id) {
        for (Player player : players) {
            if (player.getId() == id) return true;
        }
        return false;
    }

    public boolean isBanned(long id) {
        for (Long bannedPlayer : bannedPlayerIds) {
            if (bannedPlayer == id) return true;
        }
        return false;
    }

    public boolean isActive() {
        return controller.isActive();
    }

    public void sendToWerewolfChannel(String msg) {
        try {
            Objects.requireNonNull(guild.getTextChannelById(wolfChannelID)).sendMessage(msg).queue();
        } catch (Exception e) {
            LOGGER.info("Failed to send message to werwolves: " + e.getMessage());
        }
    }

    public void sendToWerewolfChannel(MessageEmbed embed) {
        try {
            Objects.requireNonNull(guild.getTextChannelById(wolfChannelID)).sendMessage(embed).queue();
        } catch (Exception e) {
            LOGGER.info("Failed to send message to werwolves: " + e.getMessage());
        }
    }

    //Getter & Setter
    public List<Player> getPlayers() {
        return players;
    }

    public Player getHost() {
        return host;
    }

    public long getMainGameMessage() {
        return mainGameMessage;
    }

    public void setMainGameMessage(long mainGameMessage) {
        this.mainGameMessage = mainGameMessage;
    }

    public GameController getController() {
        return controller;
    }

    public TextChannel getChannel() {
        return channel;
    }

    public Long getCurrentVotingMessage() {
        if (controller.isActive()) {
            return controller.getVotingMessage();
        } else {
            return null;
        }
    }

    public VotingController getVotingController() {
        return controller.getVotingController();
    }

    public void setVoiceChannelID(long id) {
        voiceChannelID = id;
    }

    public Player getPlayer(long playerID) {
        for (Player player : players) {
            if (player.getId() == playerID) {
                return player;
            }
        }
        return null;
    }

    public void setTumMode(boolean tumMode) {
        this.tumMode = tumMode;
    }

    public boolean getTumMode() {
        return tumMode;
    }

    public VoiceChannel getVoiceChannel() {
        return guild.getVoiceChannelById(voiceChannelID);
    }

    //Methods
    private boolean spawnWerewolves() {
        int amount = 0;
        int playerSize = players.size();

        if (playerSize > 30) {
            amount = playerSize / 6;
        } else if (playerSize > 20) {
            amount = 5;
        } else if (playerSize > 10) {
            amount = 4;
        } else if (playerSize > 8) {
            amount = 3;
        } else if (playerSize > 5) {
            amount = 2;
        } else if (playerSize > 1) {
            amount = 1;
        }

        ArrayList<Integer> werewolves= new ArrayList<>();

        for (int i = 0; i < amount; i++) {
            int playerNumber;
            do{ playerNumber = (int) (Math.random() * playerSize);}while(werewolves.contains(playerNumber));
            werewolves.add(playerNumber);
            Player player = players.get(playerNumber);

            player.characterType = CharacterType.Werewolf;
            Werewolf werewolf = new Werewolf(player);
            this.werewolves.add(werewolf);
            player.sendMessage("You are a werewolf");
        }

        for (int i = 0; i < playerSize; i++) {
            if(!werewolves.contains(i)){
                players.get(i).sendMessage("You are a villager");
            }
        }

        TextChannel channel = guild.getTextChannelById(channelID);
        if (channel == null) return false;
        Category category = channel.getParent();
        int position = channel.getPosition();
        String werewolfChannelName = channel.getName() + "-werewolves";

        try {
            //If there is no werewolf channel, create a new one
            if (guild.getTextChannelsByName(werewolfChannelName, true).size() == 0) {
                guild.createTextChannel(channel.getName() + "-werewolves").setParent(category).setPosition(position).setSlowmode(10).complete();
            }

            wolfChannelID = guild.getTextChannelsByName(werewolfChannelName, true).get(0).getIdLong();

            TextChannel wolfChannel = guild.getTextChannelById(wolfChannelID);
            if (wolfChannel == null) return false;

            //Set the permissions for all players
            for (Player player : players) {
                IPermissionHolder permissionHolder = new MemberImpl((GuildImpl) guild, player.user);
                if (player.characterType != CharacterType.Werewolf)
                    wolfChannel.createPermissionOverride(permissionHolder).setDeny(Permission.VIEW_CHANNEL).queue();
                else
                    wolfChannel.createPermissionOverride(permissionHolder).setAllow(Permission.VIEW_CHANNEL).queue();
            }

        } catch (Exception e) {
            channel.sendMessage("Failed to create werewolves channel.").queue();
            LOGGER.info("Error creating werewolves channel " + e.getMessage());
            return false;
        }

        return true;
    }
}