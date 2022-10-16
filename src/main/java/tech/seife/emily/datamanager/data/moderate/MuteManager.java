package tech.seife.emily.datamanager.data.moderate;

public class MuteManager {

    private final ModerationData moderationData;

    public MuteManager(ModerationData moderationData) {
        this.moderationData = moderationData;
    }

    public void moveMuteToHistory(MutedUsersData mutedUsersData, String reason) {
        moderationData.moveMuteToHistory(mutedUsersData, reason);
    }

    public void deleteCurrentMute(String serverId, String memberId) {
        moderationData.removeMute(serverId, memberId);
    }

    public boolean isUserMuted(String serverId, String id) {
        return moderationData.isMemberMuted(serverId, id);
    }


    public void saveMuted(MutedUsersData mutedUsersData) {
        moderationData.saveMute(mutedUsersData);
    }
}
