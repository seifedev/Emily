package tech.seife.emily.utils;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;

/**
 * It holds various utilities method
 */
public class Utils {


    /**
     * @param member The member to check for permission
     * @return Checks if the member has admin permissions
     */
    public static boolean hasAdmin(Member member) {
        if (member != null) {
            return member.hasPermission(Permission.ADMINISTRATOR);
        }
        return false;
    }

    /**
     * @param member The member to check for permission
     * @return Checks if the member has permissions to manager the channel.
     */
    public static boolean hasManageChannel(Member member) {
        if (member != null) {
            return member.hasPermission(Permission.MANAGE_CHANNEL);
        }
        return false;
    }


    /**
     *
     * @param content The string that contains the ID of the user alongside junk data.
     * @return it returns the user ID or -1, it doesn't verify if the ID is true or not. it just trims the data.
     */
    public static Long getUserId(String content) {

        content = content.replace("<", "");
        content = content.replace(">", "");
        content = content.replace("@", "");
        content = content.replace("!", "");

        try {
            return Long.parseLong(content);
        } catch (NumberFormatException e) {
            return -1L;
        }
    }

    /**
     * @return -1L if it can't be parsed, otherwise returns the long.
     */
    public static long parseLong(String numberToParse) {
        try {
            return Long.parseLong(numberToParse);
        } catch (NumberFormatException e) {
            return -1L;
        }
    }

}
