package tech.seife.emily.datamanager.data.system;

import org.jetbrains.annotations.Nullable;

public interface SystemData {



    boolean hasMusicChannel(String serverId);

    /**
     *
     * @param serverId The ID of the server
     * @return The ID of the channel were the bot can play music.
     */
    String getMusicChannel(String serverId);


    /**
     *
     * @param serverId The ID of server to set a new music channel.
     * @param channelId The ID of the channel were the bot will be playing music from now on.
     * @return True if it was able to save it otherwise false.
     */
    boolean setMusicChannel(String serverId, String channelId);

    /**
     * @param serverId The ID of the server.
     * @return the channel that logs the private chat between the bot and a member.
     */
    String getDmChannel(String serverId);


    /**
     * @param serverId The id of the server.
     * @return true if the server has a log channel for the private chat between the bot and a member.
     */
    boolean hasDmChannel(String serverId);


    /**
     * @param serverId  The ID of the server.
     * @param channelId The id of the channel to log the chat between the bot and a member.
     */
    boolean setDmChannel(String serverId, String channelId);

    /**
     * @param serverId The ID of the server.
     * @return the prefix for commands for the said server.
     */
    String getCommandPrefix(String serverId);

    /**
     * @param serverId The ID of the server.
     * @return the ID of the member with the highest permissions for the said server.
     */
    String getOwner(String serverId);

    /**
     * @param serverId The ID of the server.
     * @param memberId The Id of the member that will have the highest permission on the server.
     */
    boolean setOwner(String serverId, String memberId);

    /**
     * @return The token for this bot.
     */
    String getDiscordBotToken();

    /**
     * @param serverId The ID of the server.
     * @return the YouTube token that was set.
     */
    String getYoutubeToken(String serverId);

    /**
     * @param serverId  The ID of the server.
     * @param newPrefix
     */
    boolean setNewPrefix(String serverId, String newPrefix);

    /**
     * Enable/Disable the automatic deletion of the commands from the chat.
     *
     * @param serverId The ID of the server.
     */
    boolean changeSelfDestruct(String serverId);

    /**
     * @param serverId The ID of the server.
     * @return true if the commands will be deleted automatically from the chat, otherwise false.
     */
    boolean getSelfDestructValue(String serverId);

    /**
     * @param serverId The ID of the server.
     * @return How long it will take for the messages to be deleted from the chat. (if this setting is enabled.)
     */
    long getSelfDestructDelay(String serverId);

    /**
     * @param serverId The ID of the server.
     * @param amount   Every when should the commands from the chat be deleted. (if it has been enabled.)
     */
    boolean setSelfDestructDelay(String serverId, long amount);

}
