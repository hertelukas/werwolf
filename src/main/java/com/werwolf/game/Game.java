package com.werwolf.game;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.internal.entities.GuildImpl;
import net.dv8tion.jda.internal.entities.MemberImpl;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Game {
    private List<Player> players = new ArrayList<>();
    private final List<Long> bannedPlayerIds = new ArrayList<>();
    private List<Werewolf> werewolves = new ArrayList<>();
    private Player host;
    private long channelID;
    private long voiceChannelID;
    private long wolfChannelID;
    private long mainGameMessage;
    private GameController controller = new GameController(this);
    private Guild guild;
    private GameStatus status;

    private final static float WERWOLF_SPAWN_RATE = 0.5f;

    public Game(long channelID, Player host, Guild guild, Player... players) {
        this.channelID = channelID;
        this.host = host;
        if (players.length > 0) this.players = Arrays.asList(players);
        this.players.add(host);
        this.guild = guild;
        status = GameStatus.Created;
    }

    public boolean start() {
        if(status == GameStatus.Running || status == GameStatus.Stopped) {
            return false;
        }
        if(spawnWerewolves()) status = GameStatus.Running;

        return true;
    }

    public boolean stop() {
        return true; //TODO
    }

    public PlayerListStatus addPlayer(@NotNull Player player) {
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
                    //Todo if size is 0 we want to delete this game
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
            System.err.println("Failed to send message to werwolves: " + e.getMessage());
        }
    }

    public void sendToWerewolfChannel(MessageEmbed embed) {
        try {
            Objects.requireNonNull(guild.getTextChannelById(wolfChannelID)).sendMessage(embed).queue();
        } catch (Exception e) {
            System.err.println("Failed to send message to werwolves: " + e.getMessage());
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

    public GameStatus getStatus() {
        return status;
    }

    //Methods
    private boolean spawnWerewolves() {
        //Create the werewolves
        for (Player player : players) {
            Random r = new Random();
            float temp = r.nextFloat();

            if (temp < WERWOLF_SPAWN_RATE) {
                player.characterType = CharacterType.Werewolf;
                Werewolf werewolf = new Werewolf(player);
                werewolves.add(werewolf);
                player.sendMessage("You are a werewolf");
            }else{
                player.sendMessage("You are a villager");
            }
        }

        TextChannel channel = guild.getTextChannelById(channelID);
        if(channel == null) return false;
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
            if(wolfChannel == null) return false;

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
            System.out.println("Error creating werewolves channel " + e.getMessage());
            return false;
        }

        return true;
    }
}
