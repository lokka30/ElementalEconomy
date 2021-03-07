package me.lokka30.elementaleconomy.utils;

import me.lokka30.elementaleconomy.ElementalEconomy;
import me.lokka30.elementaleconomy.misc.DebugMessageType;
import me.lokka30.elementaleconomy.misc.SuppressableWarning;
import me.lokka30.microlib.MicroLogger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class Utils {

    private Utils() {
        throw new UnsupportedOperationException("Must use static methods here.");
    }

    public static final MicroLogger logger = new MicroLogger("&b&lElementalEconomy: &7");

    public static final HashSet<String> supportedServerVersions = new HashSet<>(Arrays.asList("1.16", "1.15", "1.14", "1.13", "1.12", "1.11", "1.10", "1.9", "1.8", "1.7"));

    public static final HashSet<String> contributors = new HashSet<>(Collections.singletonList("lokka30"));

    public static final HashSet<String> oneThruNine = new HashSet<>(Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9"));

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

    public static void sendDebugMessage(DebugMessageType debugMessageType, String msg) {
        if (ElementalEconomy.getInstance().settings.getConfig().getStringList("debug").contains(debugMessageType.toString())) {
            logger.info("&8[DEBUG: " + debugMessageType.toString() + "]: &7" + msg);
        }
    }

    public static List<String> getUsernamesOfVisibleOnlinePlayers(final Player player) {
        final List<String> usernames = new ArrayList<>();
        for (Player otherPlayer : Bukkit.getOnlinePlayers()) {
            if (player.canSee(otherPlayer)) {
                usernames.add(otherPlayer.getName());
            }
        }
        return usernames;
    }

    public static List<String> getUsernamesOfOnlinePlayers() {
        final List<String> usernames = new ArrayList<>();
        for (Player otherPlayer : Bukkit.getOnlinePlayers()) {
            usernames.add(otherPlayer.getName());
        }
        return usernames;
    }

    public static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }
}
