package me.lokka30.elementaleconomy.utils;

import com.sun.istack.internal.NotNull;
import me.lokka30.elementaleconomy.ElementalEconomy;
import me.lokka30.elementaleconomy.misc.SuppressableWarning;
import me.lokka30.microlib.MicroLogger;

import java.util.Arrays;
import java.util.HashSet;

public class Utils {

    private Utils() {
        throw new UnsupportedOperationException("Must use static methods here.");
    }

    public static final MicroLogger logger = new MicroLogger("&b&lElementalEconomy: &7");

    public static final HashSet<String> supportedServerVersions = new HashSet<>(Arrays.asList("1.16", "1.15", "1.14", "1.13", "1.12", "1.11", "1.10", "1.9", "1.8", "1.7"));

    /**
     * Send the suppressable warning to the console
     *
     * @param suppressableWarning warning type
     * @param extraInformation    any extra info for the warning log. can be empty (""), but not null
     * @param main                ElementalEconomy instance
     */
    public static void sendSuppressableWarning(@NotNull SuppressableWarning suppressableWarning, @NotNull String extraInformation, @NotNull ElementalEconomy main) {
        if (!main.settings.getConfig().getBoolean("suppressed-warnings." + suppressableWarning.toString(), false)) {
            logger.warning(suppressableWarning.warningMsg.replace("%extraInformation%", extraInformation));
        }
    }
}
