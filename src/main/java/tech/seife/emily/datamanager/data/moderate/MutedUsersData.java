package tech.seife.emily.datamanager.data.moderate;

import java.time.LocalDateTime;

public record MutedUsersData (String memberId, String serverId, LocalDateTime mutedDate, LocalDateTime releaseDate, String reason){}
