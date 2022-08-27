package tech.seife.emily.datamanager.data.system;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import tech.seife.emily.datamanager.data.mute.MutedUsersData;
import tech.seife.emily.datamanager.files.FileManager;

import java.util.Map;

public class MuteManager {

    private final FileManager fileManager;
    private final Gson gson;

    public MuteManager(FileManager fileManager) {
        this.fileManager = fileManager;
        gson = fileManager.getGson();
    }


    public void moveMuteToHistory(MutedUsersData mutedUsersData, String reasonToUnmute) {
        JsonObject mutedHistory = gson.fromJson(gson.toJson(fileManager.getGsonMutedHistory()), JsonObject.class);

        long id = mutedHistory.get("id").getAsLong() + 1;

        JsonObject history = new JsonObject();

        history.addProperty("userId", mutedUsersData.getUserId());
        history.addProperty("mutedDate", mutedUsersData.getMutedDate().toString());
        history.addProperty("releaseDate", mutedUsersData.getReleaseDate().toString());
        history.addProperty("reason", mutedUsersData.getReason());
        history.addProperty("reasonToUnmute", reasonToUnmute);

        mutedHistory.add(String.valueOf(id), history);
        mutedHistory.addProperty("id", id++);

        fileManager.saveMutedHistoryFile(gson.fromJson(mutedHistory, Map.class));
    }

    public void deleteCurrentMute(String userId) {
        JsonObject currentMutes = gson.fromJson(gson.toJson(fileManager.getGsonMuted()), JsonObject.class);

        currentMutes.remove(userId);

        fileManager.saveMutesFile(gson.fromJson(currentMutes, Map.class));
    }

    public boolean isUserMuted(String id) {
        JsonObject jsonObject = gson.fromJson(gson.toJson(fileManager.getGsonMuted()), JsonObject.class);

        return jsonObject.has(id);
    }

    public void saveMuted(MutedUsersData mutedUsersData) {
        JsonObject jsonObject = gson.fromJson(gson.toJson(fileManager.getGsonMuted()), JsonObject.class);

        JsonObject user = new JsonObject();

        user.addProperty("mutedDate", mutedUsersData.getMutedDate().toString());
        user.addProperty("releaseDate", mutedUsersData.getReleaseDate().toString());
        user.addProperty("reason", mutedUsersData.getReason());

        jsonObject.add(mutedUsersData.getUserId(), user);

        fileManager.saveMutesFile(gson.fromJson(jsonObject, Map.class));
    }
}
