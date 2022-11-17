package tech.seife.emily;

import com.google.gson.JsonObject;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;
import tech.seife.emily.datamanager.files.FileManager;

import java.awt.*;
import java.time.LocalDateTime;

public class Messenger {

    private final JDA jda;
    private final FileManager fileManager;
    private final JsonObject jsonObject;

    public Messenger(JDA jda, FileManager fileManager) {
        this.jda = jda;
        this.fileManager = fileManager;
        jsonObject = fileManager.getGson().fromJson(fileManager.getGson().toJson(fileManager.getMessagesGson()), JsonObject.class);
    }

    public MessageEmbed getMessageEmbed(String path) {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        if (jsonObject.has(path)) {
            JsonObject details = jsonObject.getAsJsonObject(path);
            embedBuilder.setTitle(details.get("title").getAsString());
            embedBuilder.setDescription(details.get("description").getAsString());
        }

        embedBuilder.setFooter("Created by Emily.", jda.getSelfUser().getAvatarUrl());
        embedBuilder.setAuthor(jda.getSelfUser().getName() + " Says that you are a bad boy/girl");
        embedBuilder.setTimestamp(LocalDateTime.now());
        embedBuilder.setColor(Color.RED);

        return embedBuilder.build();
    }

    public MessageEmbed sendPlayingSong(String title, String duration, String url) {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        if (jsonObject.has("currentSong")) {
            JsonObject details = jsonObject.getAsJsonObject("currentSong");

            String embedTitle = replaceSongPlaceHolders(details.get("title").getAsString(), title, url, duration);
            String embedDescription = replaceSongPlaceHolders(details.get("description").getAsString(), title, url, duration);

            embedBuilder.setTitle(embedTitle);
            embedBuilder.setDescription(embedDescription);
        }

        embedBuilder.setFooter("Created by Emily.", jda.getSelfUser().getAvatarUrl());
        embedBuilder.setAuthor(jda.getSelfUser().getName() + " Says that you are a bad boy/girl");
        embedBuilder.setTimestamp(LocalDateTime.now());
        embedBuilder.setColor(Color.RED);

        return embedBuilder.build();
    }

    public MessageEmbed addedToQueue(String title, String duration, String url) {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        if (jsonObject.has("addedToQueue")) {
            JsonObject details = jsonObject.getAsJsonObject("addedToQueue");

            String embedTitle = replaceSongPlaceHolders(details.get("title").getAsString(), title, url, duration);
            String embedDescription = replaceSongPlaceHolders(details.get("description").getAsString(), title, url, duration);

            embedBuilder.setTitle(embedTitle);
            embedBuilder.setDescription(embedDescription);
        }

        embedBuilder.setFooter("Created by Emily.", jda.getSelfUser().getAvatarUrl());
        embedBuilder.setAuthor(jda.getSelfUser().getName() + " Says that you are a bad boy/girl");
        embedBuilder.setTimestamp(LocalDateTime.now());
        embedBuilder.setColor(Color.RED);

        return embedBuilder.build();
    }

    private String replaceSongPlaceHolders(String baseString, String title, String url, String duration) {
        baseString = baseString.replaceAll("%title%", title);
        baseString = baseString.replaceAll("%url%", url);
        baseString = baseString.replaceAll("%duration%", duration);

        return baseString;
    }

}
