package tech.seife.emily.datamanager.data.mute;

import java.time.LocalDateTime;

public class MutedUsersData {

    private final String userId;
    private final LocalDateTime mutedDate;
    private final LocalDateTime releaseDate;
    private final String reason;

    public MutedUsersData(String userId, LocalDateTime releaseDate, String reason) {
        this.userId = userId;
        this.releaseDate = releaseDate;
        this.reason = reason;

        mutedDate = LocalDateTime.now();
    }

    public String getUserId() {
        return userId;
    }

    public LocalDateTime getMutedDate() {
        return mutedDate;
    }

    public LocalDateTime getReleaseDate() {
        return releaseDate;
    }

    public String getReason() {
        return reason;
    }
}
