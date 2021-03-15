package com.werwolf.core.handler;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;
import com.sedmelluq.discord.lavaplayer.track.playback.MutableAudioFrame;
import net.dv8tion.jda.api.audio.AudioSendHandler;
import org.jetbrains.annotations.Nullable;

import java.nio.Buffer;
import java.nio.ByteBuffer;

public class AudioPlayerSendHandler implements AudioSendHandler {

    private final AudioPlayer audioPlayer;
    private final ByteBuffer buffer;
    private MutableAudioFrame lastFrame;

    public AudioPlayerSendHandler(AudioPlayer player) {
        this.audioPlayer = player;
        this.buffer = ByteBuffer.allocate(1024);
        this.lastFrame = new MutableAudioFrame();
        this.lastFrame.setBuffer(buffer);
    }

    @Override
    public boolean canProvide() {
        return audioPlayer.provide(lastFrame);
    }

    @Override
    public ByteBuffer provide20MsAudio() {
        ((Buffer)buffer).flip();
        return buffer;
    }

    @Override
    public boolean isOpus() {
        return true;
    }

}
