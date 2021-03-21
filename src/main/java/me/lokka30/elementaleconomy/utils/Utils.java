package me.lokka30.elementaleconomy.utils;

import me.lokka30.elementaleconomy.ElementalEconomy;
import me.lokka30.microlib.MicroLogger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class Utils {

    private Utils() {
        throw new UnsupportedOperationException("Must use static methods here.");
    }

    public static final MicroLogger logger = new MicroLogger("&b&lElementalEconomy: &7");

    /**
     * This variable is used to store a HashSet of numbers one to nine,
     * as Strings. This is used for tab completion.
     */
    public static final HashSet<String> oneThruNine = new HashSet<>(Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9"));

    /**
     * This enum makes it easy to send certain warnings, and
     * it allows administrators to configure if they should
     * be suppressed. It is not recommended that the warnings
     * are suppressed, rather, they should be dealt with.
     */
    public enum SuppressableWarning {
        DEPRECATED_VAULT_METHOD_CALL("A plugin (not ElementalEconomy) made a deprecated method call using the Vault API. &8(&7%extraInformation%&8)"),
        UNSUPPORTED_VAULT_METHOD_CALL("A plugin (not ElementalEconomy) made a method call to the Vault API but ElementalEconomy does not support it. &8(&7%extraInformation%&8)");

        public final String warningMsg;

        SuppressableWarning(String warningMsg) {
            this.warningMsg = warningMsg;
        }
    }


    /**
     * Send the suppressable warning to the console
     *
     * @param suppressableWarning warning type
     * @param extraInformation    any extra info for the warning log. can be empty (""), but not null
     * @param main                ElementalEconomy instance
     */
    public static void sendSuppressableWarning(SuppressableWarning suppressableWarning, String extraInformation, ElementalEconomy main) {
        if (!main.settings.getConfig().getBoolean("suppressed-warnings." + suppressableWarning.toString(), false)) {
            logger.warning(suppressableWarning.warningMsg.replace("%extraInformation%", extraInformation));
        }
    }

    /**
     * Debug Message Type - used to denote a type of debug message
     */
    public enum DebugMessageType {
        BALANCE_METHODS, BALTOP_CACHING
    }

    /**
     * Sends a debug message to console, if the DebugMessageType is enabled in the settings config file
     *
     * @param debugMessageType type of debug message being sent
     * @param msg              contents of the message
     */
    public static void sendDebugMessage(DebugMessageType debugMessageType, String msg) {
        if (ElementalEconomy.getInstance().settings.getConfig().getStringList("debug").contains(debugMessageType.toString())) {
            logger.info("&8[DEBUG: " + debugMessageType.toString() + "]: &7" + msg);
        }
    }

    /**
     * This method returns a list of online players' usernames,
     * but only if the player can see them (most vanish plugins
     * utilise Bukkit#hidePlayer).
     *
     * @param player the player that is being observed for which players they can see
     * @return a list of all online players' usernames, so long the specified player can 'see' them.
     */
    public static List<String> getUsernamesOfVisibleOnlinePlayers(final Player player) {
        final List<String> usernames = new ArrayList<>();
        for (Player otherPlayer : Bukkit.getOnlinePlayers()) {
            if (player.canSee(otherPlayer)) {
                usernames.add(otherPlayer.getName());
            }
        }
        return usernames;
    }

    /**
     * This method returns a list of all online players' usernames.
     * Useful for adding tab completion terms.
     *
     * @return a list of all online players' usernames
     */
    public static List<String> getUsernamesOfOnlinePlayers() {
        final List<String> usernames = new ArrayList<>();
        for (Player otherPlayer : Bukkit.getOnlinePlayers()) {
            usernames.add(otherPlayer.getName());
        }
        return usernames;
    }

    /**
     * Check if str is an integer
     *
     * @param str str to check
     * @return if str is an integer
     */
    public static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }
}
