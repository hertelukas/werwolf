package com.werwolf.game;

import com.werwolf.core.handler.AudioHandler;
import com.werwolf.core.handler.Handler;
import com.werwolf.game.controller.GameController;
import com.werwolf.game.controller.VotingController;
import com.werwolf.game.roles.*;
import com.werwolf.helpers.UserMessageCreator;
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

    private boolean gamestartsend = false;
    private boolean tummodeenablesend = false;

    private List<Player> players = new ArrayList<>();
    private final List<Long> bannedPlayerIds = new ArrayList<>();
    private final List<Werewolf> werewolves = new ArrayList<>();
    private Player host;
    private final long channelID;
    private final TextChannel channel;
    private long voiceChannelID = 0;
    private long wolfChannelID;
    private long mainGameMessage;
    private final GameController controller;
    private final Guild guild;
    private boolean tumMode = false;
    private final Configurations configurations = new Configurations();

    public Game(TextChannel channel, Player host, Guild guild, Player... players) {
        this.channel = channel;
        this.channelID = channel.getIdLong();
        this.host = host;
        if (players.length > 0) this.players = Arrays.asList(players);
        this.players.add(host);
        this.guild = guild;
        this.controller = new GameController(this);
    }

    public boolean start() {
        if (controller.isActive()) {
            return false;
        }

        controller.setActive(true);
        setMainWritePermissions();

        //Story
        controller.sendIntroMessage();
        if (createRoles()) controller.setActive(true);

        //If there is a voice channel specified, we try to join
        if (voiceChannelID != 0) {
            VoiceChannel voiceChannel = guild.getVoiceChannelById(voiceChannelID);
            if (voiceChannel != null)
                AudioHandler.getAudioHandler().loadAndPlay(this, "Werwolf.wav", true, true);
        }

        LOGGER.info("Bürgermeisterwahl");
        controller.mayorelection(true, null);
        Handler.addWerewolfChannel(wolfChannelID, this);


        return true;
    }

    public boolean stop() {
        try {
            Handler.deleteGame(channelID);
            Objects.requireNonNull(guild.getTextChannelById(wolfChannelID)).delete().queue();
            //Close audio connection no matter what
            guild.getAudioManager().closeAudioConnection();
            controller.setActive(false);
            setMainWritePermissions();
            LOGGER.info("Spiel erfolgreich gestoppt");
        } catch (Exception e) {
            LOGGER.warn("Failed to remove werewolf channel: " + e.getMessage());
        }
        return true;
    }

    public PlayerListStatus addPlayer(@NotNull Player player) {
        if (isActive()) return PlayerListStatus.unsuccessful;
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
            LOGGER.warn("Failed to send message to werwolves: " + e.getMessage());
        }
    }

    public void sendToWerewolfChannel(MessageEmbed embed) {
        try {
            Objects.requireNonNull(guild.getTextChannelById(wolfChannelID)).sendMessage(embed).queue();
        } catch (Exception e) {
            LOGGER.warn("Failed to send message to werwolves: " + e.getMessage());
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

    public Configurations getConfigurations() {
        return configurations;
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

    public boolean isGamestartsend() {
        return gamestartsend;
    }

    public void setGamestartsend(boolean gamestartsend) {
        this.gamestartsend = gamestartsend;
    }

    public boolean isTummodeenablesend() {
        return tummodeenablesend;
    }

    public void setTummodeenablesend(boolean tummodeenablesend) {
        this.tummodeenablesend = tummodeenablesend;
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



    public void setWerwolfWritePermissions(boolean value){
        for (Player player : players) {
            if (!player.getCharacterType().isCanSeeWWChannel()) continue;
            IPermissionHolder permissionHolder = new MemberImpl((GuildImpl) guild, player.getUser());
            try {
                if (player.isAlive() && value)
                    Objects.requireNonNull(guild.getTextChannelById(wolfChannelID)).putPermissionOverride(permissionHolder).setAllow(Permission.MESSAGE_WRITE).queue();
                else
                    Objects.requireNonNull(guild.getTextChannelById(wolfChannelID)).putPermissionOverride(permissionHolder).setDeny(Permission.MESSAGE_WRITE).queue();
            } catch (Exception e) {
                LOGGER.warn("Failed to change writing permissions in werwolf channel: " + e.getMessage());
            }
        }
    }


    public void setMainWritePermissions() {
        for (Player player : players) {
            if (player.getId() == host.getId()) continue;

            IPermissionHolder permissionHolder = new MemberImpl((GuildImpl) guild, player.getUser());

            try {
                if (!configurations.canWrite() && isActive())
                    Objects.requireNonNull(guild.getTextChannelById(channelID)).putPermissionOverride(permissionHolder).setDeny(Permission.MESSAGE_WRITE).queue();
                else
                    Objects.requireNonNull(guild.getTextChannelById(channelID)).putPermissionOverride(permissionHolder).setAllow(Permission.MESSAGE_WRITE).queue();
            } catch (Exception e) {
                LOGGER.warn("Failed to update write permissions for main channel: " + e.getMessage());
            }
        }
    }


    //Methods
    private boolean createRoles() {
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
        } else if (playerSize > 2) {
            amount = 1;
        }

        ArrayList<Integer> werewolves = new ArrayList<>();

        for (int i = 0; i < amount; i++) {
            int playerNumber;
            do {
                playerNumber = (int) (Math.random() * playerSize);
            } while (werewolves.contains(playerNumber));

            werewolves.add(playerNumber);
            players.set(playerNumber, new Werewolf(players.get(playerNumber)));
            Player player = players.get(playerNumber);

            this.werewolves.add((Werewolf) players.get(playerNumber));
            player.sendMessage(UserMessageCreator.getCreator().getMessage(this, "role-werewolf"));
            LOGGER.info(player.getUsername() + " ist ein Werwolf");
        }

        //Spzialrollen verteilen
        presetCheck(amount);
        int seertmp = configurations.getSeernum();
        int huntertmp = configurations.getHunternum();
        int littleGirltmp = configurations.getLittleGirlnum();
        int sherifftmp = configurations.getSheriffnum();
        int jailortmp = configurations.getJailornum();
        int bodytmp = configurations.getBodyguardnum();
        int killertmp = 0;
        int prostitutestmp = configurations.getPrositutesnum();
        int spytmp = configurations.getSpynum();
        if (configurations.getSerialkiller()) killertmp = 1;


        for (int i = werewolves.size(); i < playerSize; i++) {
            int playerNumber;
            do {
                playerNumber = (int) (Math.random() * playerSize);
            } while (players.get(playerNumber).getCharacterType() != CharacterType.Villager);

            if (seertmp-- > 0) {
                players.set(playerNumber, new Seer(players.get(playerNumber)));
                players.get(playerNumber).sendMessage(UserMessageCreator.getCreator().getMessage(this, "role-seer"));
                LOGGER.info(players.get(playerNumber).getUsername() + " ist der Seher");
            } else if (huntertmp-- > 0) {
                players.set(playerNumber, new Hunter(players.get(playerNumber)));
                players.get(playerNumber).sendMessage(UserMessageCreator.getCreator().getMessage(this, "role-hunter"));
                LOGGER.info(players.get(playerNumber).getUsername() + " ist der Jäger");
            } else if (littleGirltmp-- > 0) {
                players.set(playerNumber, new LittleGirl(players.get(playerNumber)));
                players.get(playerNumber).sendMessage(UserMessageCreator.getCreator().getMessage(this, "role-littlegirl"));
                LOGGER.info(players.get(playerNumber).getUsername() + " ist das Mädchen");
            } else if (sherifftmp-- > 0) {
                players.set(playerNumber, new Sheriff(players.get(playerNumber)));
                players.get(playerNumber).sendMessage(UserMessageCreator.getCreator().getMessage(this, "role-sheriff"));
                LOGGER.info(players.get(playerNumber).getUsername() + " ist Sheriff");
            } else if (jailortmp-- > 0) {
                players.set(playerNumber, new Jailor(players.get(playerNumber)));
                players.get(playerNumber).sendMessage(UserMessageCreator.getCreator().getMessage(this, "role-jailor"));
                LOGGER.info(players.get(playerNumber).getUsername() + " ist Jailor");
            } else if (bodytmp-- > 0) {
                players.set(playerNumber, new Bodyguard(players.get(playerNumber)));
                players.get(playerNumber).sendMessage(UserMessageCreator.getCreator().getMessage(this, "role-bodyguard"));
                LOGGER.info(players.get(playerNumber).getUsername() + " ist Bodyguard");
            } else if (killertmp-- > 0) {
                players.set(playerNumber, new Serialkiller(players.get(playerNumber)));
                players.get(playerNumber).sendMessage(UserMessageCreator.getCreator().getMessage(this, "role-killer"));
                LOGGER.info(players.get(playerNumber).getUsername() + " ist Serienmörder");
            } else if(prostitutestmp-- > 0){
                players.set(playerNumber, new Prostitute(players.get(playerNumber)));
                players.get(playerNumber).sendMessage(UserMessageCreator.getCreator().getMessage(this, "role-prostitute"));
                LOGGER.info(players.get(playerNumber).getUsername() + " ist eine Nutte");
            }else if(spytmp-- > 0){
                players.set((playerNumber), new Spy(players.get(playerNumber)));
                players.get(playerNumber).sendMessage(UserMessageCreator.getCreator().getMessage(this, "role-spy"));
                LOGGER.info(players.get(playerNumber).getUsername() + " ist Spion");

            }
        }

        //Create Werewolves channel
        for (int i = 0; i < playerSize; i++) {
            if (!werewolves.contains(i) && players.get(i).getCharacterType() == CharacterType.Villager) {
                players.get(i).sendMessage(UserMessageCreator.getCreator().getMessage(this, "role-villager"));
                LOGGER.info(players.get(i).getUsername() + " ist ein Dorfbewohner");
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
                    IPermissionHolder permissionHolder = new MemberImpl((GuildImpl) guild, player.getUser());
                    if (!player.getCharacterType().isCanSeeWWChannel())
                        wolfChannel.putPermissionOverride(permissionHolder).setDeny(Permission.VIEW_CHANNEL).queue();
                    else
                        wolfChannel.putPermissionOverride(permissionHolder).setAllow(Permission.VIEW_CHANNEL).queue();
                }

            } catch (Exception e) {
                channel.sendMessage(UserMessageCreator.getCreator().getMessage(this, "werewolf-channel-creation-failed")).queue();
                LOGGER.warn("Error creating werewolves channel " + e.getMessage());
                return false;
            }
        }
        return true;

    }

    public void presetCheck(int werewolfNum) {
        switch (configurations.getPreset()) {
            case 1 -> { // one of each
                configurations.setAll(new int[]{1, 1, 1, 1, 1, 1, 1, 1});
            }
            case 2 -> {
                configurations.setAll(new int[]{1, 1, 1, 1, 0, 0, 0, 0});
            }
            case 3 -> {
                configurations.setAll(new int[]{1, 0, 0, 0, 1, 2, 2, 1});
            }
            default -> {
            }
        }
    }

    public void sendToSpy(String msg) {
        for (Player player : players) {
            if(player.getCharacterType() == CharacterType.Spy && player.isAlive()){
                player.sendMessage(UserMessageCreator.getCreator().getMessage(this, "new-werewolf-msg") + "`" + msg + "`");
            }
        }
    }
}