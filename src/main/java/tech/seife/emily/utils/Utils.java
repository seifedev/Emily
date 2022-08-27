package tech.seife.emily.utils;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;

public class Utils {

    public static boolean hasAdmin(Member member) {
        if (member != null) {
            return member.hasPermission(Permission.ADMINISTRATOR);
        }
        return false;
    }

    public static boolean hasManageChannel(Member member) {
        if (member != null) {
            return member.hasPermission(Permission.MANAGE_CHANNEL);
        }
        return false;
    }


    public static Long getUserId(String message) {

        message = message.replace("<", "");
        message = message.replace(">", "");
        message = message.replace("@", "");
        message = message.replace("!", "");

        try {
            return Long.parseLong(message);
        } catch (NumberFormatException e) {
            return -1L;
        }
    }

    public static long parseLong(String numberToParse) {
        try {
            return Long.parseLong(numberToParse);
        } catch (NumberFormatException e) {
            return -1L;
        }
    }

}
