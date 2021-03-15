package com.werwolf.core.handler;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.werwolf.core.handler.audio.GuildAudioManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.HashMap;
import java.util.Map;

public class AudioHandler extends ListenerAdapter {
    private static AudioHandler audioHandler = new AudioHandler();

    private final AudioPlayerManager playerManager;
    private final Map<Long, GuildAudioManager> guildAudioManagers;

    private AudioHandler(){
        this.guildAudioManagers = new HashMap<>();

        this.playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);
    }

    private synchronized GuildAudioManager getGuildAudioManager(Guild guild){
        long guildId = Long.parseLong(guild.getId());
        GuildAudioManager audioManager = guildAudioManagers.get(guildId);

        if(audioManager == null){
            audioManager = new GuildAudioManager(playerManager);
            guildAudioManagers.put(guildId, audioManager);
        }

        guild.getAudioManager().setSendingHandler(audioManager.getSendHandler());
        return audioManager;
    }


    public void loadAndPlay(VoiceChannel channel, final String track, boolean isMusic, boolean forcePlay){
        GuildAudioManager audioManager = getGuildAudioManager(channel.getGuild());
        String trackUrl = isMusic ? "src/main/resources/Audio/Music/" + track : "src/main/resources/Audio/Voice/" + track;

        if(isMusic)
            audioManager.setVolume(15);
        else
            audioManager.setVolume(50);

        playerManager.loadItemOrdered(audioManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack audioTrack) {
                play(channel, audioManager, audioTrack, forcePlay);
            }

            @Override
            public void playlistLoaded(AudioPlaylist audioPlaylist) {
                AudioTrack firstTrack = audioPlaylist.getSelectedTrack();

                if(firstTrack == null){
                    firstTrack = audioPlaylist.getTracks().get(0);
                }

                play(channel, audioManager, firstTrack, forcePlay);
            }

            @Override
            public void noMatches() {
                System.out.println("AUDIO: File not found");
            }

            @Override
            public void loadFailed(FriendlyException e) {
                System.out.println("AUDIO: Failed to play " + e.getMessage());
            }
        });
    }

    private void play(VoiceChannel channel, GuildAudioManager audioManager, AudioTrack track, boolean forcePlay){
        connectToFirstVoiceChannel(channel.getGuild().getAudioManager(), channel);
        if(forcePlay) audioManager.scheduler.force(track);
        else audioManager.scheduler.queue(track);
    }

    private static void connectToFirstVoiceChannel(AudioManager audioManager, VoiceChannel channel) {
        if(!audioManager.isConnected() && !audioManager.isAttemptingToConnect()){
            audioManager.openAudioConnection(channel);
        }
    }

    public static AudioHandler getAudioHandler(){
        return audioHandler;
    }
}