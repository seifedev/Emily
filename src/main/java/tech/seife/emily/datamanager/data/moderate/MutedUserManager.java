package tech.seife.emily.datamanager.data.moderate;

import javax.annotation.Nullable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class MutedUserManager {

    private final MuteManager muteManager;
    private Set<MutedUsersData> mutedUsersDataSet;

    public MutedUserManager(MuteManager muteManager) {
        mutedUsersDataSet = new HashSet<>();
        this.muteManager = muteManager;
    }


    /**
     * Loads all the mute requests from the Json file
     *
     * @param moderationData We need this to fetch the data from either the Json file or the database.
     */
    public void loadMutedUsers(ModerationData moderationData) {
        mutedUsersDataSet = moderationData.getMutedData();
    }


    public void unMute(String user, String serverId, String reasonToUnmute) {
        MutedUsersData mutedUsersData = getMutedUsersDataSetForServer(user);

        mutedUsersDataSet.remove(mutedUsersData);

        muteManager.moveMuteToHistory(mutedUsersData, reasonToUnmute);
        muteManager.deleteCurrentMute(mutedUsersData.memberId(), serverId);
    }

    public boolean canUnmuteUser(String serverId, String memberId) {
        MutedUsersData mutedUsersData = getMutedUsersDataSetForServer(serverId);

        if (mutedUsersData != null && mutedUsersData.memberId().equals(memberId)) {
            return mutedUsersData.releaseDate().isBefore(LocalDateTime.now());
        }
        return false;
    }


    public void muteUser(String memberId, String serverId, LocalDateTime mutedDate, LocalDateTime releaseDate, String reason) {
        if (!muteManager.isUserMuted(serverId, memberId)) {
            MutedUsersData mutedUsersData = new MutedUsersData(memberId, serverId, mutedDate, releaseDate, reason);

            mutedUsersDataSet.add(mutedUsersData);
            muteManager.saveMuted(mutedUsersData);

        }
    }


    @Nullable
    public MutedUsersData getMutedUsersDataSetForServer(String serverId) {
        return mutedUsersDataSet.stream().filter(mutedUsersData -> mutedUsersData.serverId().equals(serverId)).findAny().orElse(null);
    }
}
