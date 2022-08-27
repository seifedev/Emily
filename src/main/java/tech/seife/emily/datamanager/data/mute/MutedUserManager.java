package tech.seife.emily.datamanager.data.mute;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import tech.seife.emily.datamanager.data.system.MuteManager;
import tech.seife.emily.datamanager.files.FileManager;

import javax.annotation.Nullable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MutedUserManager {

    private final MuteManager muteManager;
    private final Set<MutedUsersData> mutedUsersDataSet;

    public MutedUserManager(MuteManager muteManager) {
        this.muteManager = muteManager;
        mutedUsersDataSet = new HashSet<>();
    }

    public void loadMutedUsers(FileManager fileManager) {
        Gson gson = fileManager.getGson();
        JsonObject mutedHistory = gson.fromJson(gson.toJson(fileManager.getGsonMuted()), JsonObject.class);

        for (Map.Entry<String, JsonElement> entry : mutedHistory.entrySet()) {
            JsonObject details = entry.getValue().getAsJsonObject();
            mutedUsersDataSet.add(new MutedUsersData(entry.getKey(), LocalDateTime.parse(details.get("releaseDate").getAsString()), details.get("reason").getAsString()));
        }
    }


    public void unMute(String user, String reasonToUnmute) {
        MutedUsersData mutedUsersData = getMutedUsersDataSet(user);

        mutedUsersDataSet.remove(mutedUsersData);

        muteManager.moveMuteToHistory(mutedUsersData, reasonToUnmute);
        muteManager.deleteCurrentMute(mutedUsersData.getUserId());
    }

    public boolean canUnmuteUser(String user) {
        MutedUsersData mutedUsersData = getMutedUsersDataSet(user);

        if (mutedUsersData != null) {
            return mutedUsersData.getReleaseDate().isBefore(LocalDateTime.now());
        }
        return false;
    }


    public void muteUser(String user, LocalDateTime localDateTime, String reason) {
        if (!muteManager.isUserMuted(user)) {
            MutedUsersData mutedUsersData = new MutedUsersData(user, localDateTime, reason);

            mutedUsersDataSet.add(mutedUsersData);
            muteManager.saveMuted(mutedUsersData);

        }
    }

    @Nullable
    public MutedUsersData getMutedUsersDataSet(String id) {
        return mutedUsersDataSet
                .stream()
                .filter(mutedUsersData -> mutedUsersData.getUserId().equals(id))
                .findFirst()
                .orElse(null);
    }
}
