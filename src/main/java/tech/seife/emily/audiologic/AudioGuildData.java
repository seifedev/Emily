package tech.seife.emily.audiologic;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;

public record AudioGuildData (String serverId, AudioPlayer audioPlayer, AudioEventAdapter audioEventAdapter){

}
