package tech.seife.emily.datamanager.data.moderate;

import java.util.Set;

public interface ModerationData {
    /**
     *
     * @return A set with all the information for the muted users bot-wide.
     */
    Set<MutedUsersData> getMutedData();

    /**
     *
     * @param mutedUsersData The data that needs to be saved for a more long-term saving
     * @return If the BOT was able to successfully save the mute of the member.
     */
    boolean saveMute(MutedUsersData mutedUsersData);

    /**
     *
     * @param serverId The server ID that the mute will be removed for the said member.
     * @param memberId The ID of the member
     * @return True if the mute was removed, false if it failed.
     */
    boolean removeMute(String serverId, String memberId);


    /**
     *
     * @param mutedUsersData The data of the mute.
     * @param reason The reason as to why the mute was removed.
     * @return True if the data was successfully in the history, otherwise false.
     */
    boolean moveMuteToHistory(MutedUsersData mutedUsersData, String reason);

    /**
     *
     * @param serverId The ID of the server were we need to check if the member is muted.
     * @param id The id of the member
     * @return True if the member is muted, otherwise false.
     */
    boolean isMemberMuted(String serverId, String id);

}
