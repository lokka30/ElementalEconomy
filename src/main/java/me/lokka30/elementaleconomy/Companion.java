package me.lokka30.elementaleconomy;

import me.lokka30.elementaleconomy.misc.PossibleIncompatibility;
import me.lokka30.elementaleconomy.utils.Utils;
import me.lokka30.microlib.UpdateChecker;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

import java.io.IOException;
import java.util.HashSet;

public class Companion {

    private final ElementalEconomy main;
    public Companion(ElementalEconomy main) { this.main = main; }

    public int possibleIncompatibilitiesAmount = 0;
    public void checkCompatibility() {
        Utils.logger.info("&f(Compatibility Checker) &7Checking for possible incompatibilities...");

        final PluginManager pluginManager = Bukkit.getPluginManager();
        HashSet<PossibleIncompatibility> possibleIncompatibilities = new HashSet<>();

        // Server version supported?
        boolean isSupported = false;
        for(String supportedServerVersion : Utils.supportedServerVersions) {
            if(Bukkit.getVersion().contains(supportedServerVersion)) isSupported = true; break;
        }
        if(!isSupported) possibleIncompatibilities.add(PossibleIncompatibility.UNSUPPORTED_SERVER_VERSION);

        // Valid economy API not installed?
        if(pluginManager.getPlugin("Vault") == null) {
            possibleIncompatibilities.add(PossibleIncompatibility.NO_VALID_ECONOMY_API);
        }

        // List all possible incompatibilities.
        possibleIncompatibilitiesAmount = possibleIncompatibilities.size();
        if(possibleIncompatibilitiesAmount == 0) {
            Utils.logger.info("&f(Compatibility Checker) &7No possible incompatibilities were found. :)");
        } else {
            Utils.logger.warning("&f(Compatibility Checker) &b" + possibleIncompatibilitiesAmount + "&7 possible incompatibilities were found.");

            for(PossibleIncompatibility possibleIncompatibility : possibleIncompatibilities) {
                switch(possibleIncompatibility) {
                    case UNSUPPORTED_SERVER_VERSION:
                        Utils.logger.warning("&f(Compatibility Checker) &7Your server version &b" + Bukkit.getVersion() + "&7 is unsupported by &bElementalEconomy v" + main.getDescription().getVersion() + "&7.");
                        break;
                    case NO_VALID_ECONOMY_API:
                        Utils.logger.warning("&f(Compatibility Checker) &7You don't have a valid economy API installed alongside ElementalEconomy. Consider installing the plugin &bVault&7 to allow a plethora of other plugins to utilise ElementalEconomy.");
                        break;
                    default:
                        throw new IllegalStateException("(Compatibility Checker) Unexpected PossibleIncompatibility " + possibleIncompatibility.toString() + "!");
                }
            }
        }
    }

    public void loadFiles() {
        Utils.logger.info("&f(Configuration) &7Loading configuration files...");

        // Load config files
        try {
            main.settings.load();
            main.messages.load();
            main.currencies.load();
        } catch(IOException ex) {
            Utils.logger.error("&f(Configuration) &7An internal error occured whilst attempting to load the configuration files. Stack trace:");
            ex.printStackTrace();
        }

        // Save license.txt, if it already exists then it will overwrite it
        main.saveResource("license.txt", true);
    }

    public void loadStorage() {
        Utils.logger.info("&f(Storage) &7Loading the storage system...");
        main.storageManager.loadStorage();
    }

    public void loadCurrencies() {
        Utils.logger.info("&f(Currencies) &7Loading currencies...");
        main.currencyManager.loadCurrencies();
    }

    public void startMetrics() {
        new Metrics(main, 10198);
    }

    public void checkForUpdates() {
        if(main.settings.getConfig().getBoolean("check-for-updates", false)) {
            Utils.logger.info("&f(Update Checker) &7Checking for updates...");

            try {
                final UpdateChecker updateChecker = new UpdateChecker(main, 12345); //TODO change id
                updateChecker.getLatestVersion(latestVersion -> {
                    if(!updateChecker.getCurrentVersion().equals(latestVersion)) {
                        Utils.logger.warning("&f(Update Checker) &7Update found! ~ A new update is available on the SpigotMC page, &bv" + latestVersion + "&7. You're running &bv" + updateChecker.getCurrentVersion() + "&7.");
                    }
                });
            } catch(NoSuchMethodError err) {
                Utils.logger.error("&f(Update Checker) &7The update checker only supports &bMC 1.11+&7. Your server's version is &b" + Bukkit.getVersion() + "&7. Please disable '&bcheck-for-updates&7' in &bsettings.yml&7.");
            }
        }
    }

    public void disableStorage() {
        Utils.logger.info("&f(Storage) &7Disabling the storage system...");
        main.storageManager.storage.disable();
    }
}
