package com.werwolf.core.handler;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEvent;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventListener;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public class AudioHandler {
    private final AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
    private AudioPlayerSendHandler audioPlayerSendHandler;

    public AudioHandler(){
        AudioSourceManagers.registerRemoteSources(playerManager);
    }

    public void play(String location){
        AudioPlayer player = playerManager.createPlayer();
        TrackScheduler trackScheduler = new TrackScheduler(player);
        player.addListener(trackScheduler);
        audioPlayerSendHandler = new AudioPlayerSendHandler(player);

        playerManager.loadItem(location, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack audioTrack) {
                trackScheduler.queue(audioTrack);
            }

            @Override
            public void playlistLoaded(AudioPlaylist audioPlaylist) {
                for (AudioTrack track : audioPlaylist.getTracks()) {
                    trackScheduler.queue(track);
                }
            }

            @Override
            public void noMatches() {
                System.out.println("Audio: No matches found");
            }

            @Override
            public void loadFailed(FriendlyException e) {
                System.out.println("Audio: Failed to load audio: " + e.getMessage());
            }
        });

    }

    public AudioPlayerSendHandler getAudioPlayerSendHandler() {
        return audioPlayerSendHandler;
    }

    private static class TrackScheduler implements AudioEventListener {

        AudioPlayer player;
        public TrackScheduler(AudioPlayer player){
            this.player = player;
        }

        @Override
        public void onEvent(AudioEvent audioEvent) {
            System.out.println("Track scheduler: " + audioEvent.player.getPlayingTrack());
        }

        public void queue(AudioTrack audioTrack) {
            player.playTrack(audioTrack);
        }
    }

}
