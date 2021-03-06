package me.lokka30.elementaleconomy;

import me.lokka30.elementaleconomy.commands.*;
import me.lokka30.elementaleconomy.listeners.PlayerJoinListener;
import me.lokka30.elementaleconomy.listeners.PlayerQuitListener;
import me.lokka30.elementaleconomy.misc.PossibleIncompatibility;
import me.lokka30.elementaleconomy.utils.Utils;
import me.lokka30.microlib.UpdateChecker;
import net.milkbowl.vault.economy.Economy;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicePriority;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

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
        for (String supportedServerVersion : Utils.supportedServerVersions) {
            if (Bukkit.getVersion().contains(supportedServerVersion)) isSupported = true;
            break;
        }
        if (!isSupported) possibleIncompatibilities.add(PossibleIncompatibility.UNSUPPORTED_SERVER_VERSION);

        // Valid economy API not installed?
        if (pluginManager.getPlugin("Vault") == null) {
            possibleIncompatibilities.add(PossibleIncompatibility.NO_VALID_ECONOMY_API);
        }

        // Check if Towny is installed. It doesn't work with EE since they incorrectly use deprecated Vault methods. Contact lokka30 if confused.
        if (pluginManager.getPlugin("Towny") != null) {
            possibleIncompatibilities.add(PossibleIncompatibility.TOWNY_INSTALLED);
        }

        // List all possible incompatibilities.
        possibleIncompatibilitiesAmount = possibleIncompatibilities.size();
        if (possibleIncompatibilitiesAmount == 0) {
            Utils.logger.info("&f(Compatibility Checker) &7No possible incompatibilities were found.");
        } else {
            Utils.logger.warning("&f(Compatibility Checker) &b" + possibleIncompatibilitiesAmount + "&7 possible incompatibilities were found.");

            for (PossibleIncompatibility possibleIncompatibility : possibleIncompatibilities) {
                switch (possibleIncompatibility) {
                    case UNSUPPORTED_SERVER_VERSION:
                        Utils.logger.warning("&f(Compatibility Checker) &7Your server version &b" + Bukkit.getVersion() + "&7 is unsupported by &bElementalEconomy v" + main.getDescription().getVersion() + "&7.");
                        break;
                    case NO_VALID_ECONOMY_API:
                        Utils.logger.warning("&f(Compatibility Checker) &7You don't have a valid economy API installed alongside ElementalEconomy. Consider installing the plugin &bVault&7 to allow a plethora of other plugins to utilise ElementalEconomy.");
                        break;
                    case TOWNY_INSTALLED:
                        Utils.logger.warning("&f(Compatibility Checker) &7The plugin &bTowny&7 is installed, which incorrectly uses Vault methods (which have also been deprecated for &oyears&7 now). It is unlikely the developer will fix this. ElementalEconomy will not support this.");
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
        Utils.logger.info("&f(Storage) &7Fetching configured storage system...");
        main.storageManager.loadStorage();
    }

    public void loadCurrencies() {
        Utils.logger.info("&f(Currencies) &7Loading currencies...");
        main.currencyManager.loadCurrencies();
    }

    public void cacheBaltop() {
        Utils.logger.info("&f(Baltop) &7Caching baltop...");
        main.baltopManager.resetCache();
    }

    public void registerListeners() {
        Utils.logger.info("&f(Listeners) &7Registering listeners...");
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(main), main);
        Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(main), main);
    }

    public void registerCommands() {
        Utils.logger.info("&f(Listeners) &7Registering commands...");
        Objects.requireNonNull(main.getCommand("balance")).setExecutor(new BalanceCommand(main));
        Objects.requireNonNull(main.getCommand("baltop")).setExecutor(new BaltopCommand(main));
        Objects.requireNonNull(main.getCommand("economy")).setExecutor(new EconomyCommand(main));
        Objects.requireNonNull(main.getCommand("elementaleconomy")).setExecutor(new ElementalEconomyCommand(main));
        Objects.requireNonNull(main.getCommand("pay")).setExecutor(new PayCommand(main));
    }

    public void hookExternalPlugins() {
        Utils.logger.info("&f(Hook) &7Hooking to external plugins...");

        if (Bukkit.getPluginManager().getPlugin("Vault") != null) {
            Utils.logger.info("&f(Hook) &bVault&7 found, hooking...");
            Bukkit.getServicesManager().register(Economy.class, main.vaultHoook, main, ServicePriority.Highest);
        }
    }

    public void startMetrics() {
        new Metrics(main, 10198);
    }

    public List<String> updateCheckerResult = null;

    public void checkForUpdates() {
        if (main.settings.getConfig().getBoolean("check-for-updates", false)) {
            Utils.logger.info("&f(Update Checker) &7Checking for updates...");

            try {
                final UpdateChecker updateChecker = new UpdateChecker(main, 12345); //TODO change id
                updateChecker.getLatestVersion(latestVersion -> {
                    if (!updateChecker.getCurrentVersion().equals(latestVersion)) {
                        updateCheckerResult = main.messages.getConfig().getStringList("update-checker");

                        // iterate through list, replace placeholders
                        for (int i = 0; i < updateCheckerResult.size(); i++) {
                            updateCheckerResult.set(i, updateCheckerResult.get(i)
                                    .replace("%latestVersion%", latestVersion)
                                    .replace("%currentVersion%", updateChecker.getCurrentVersion())
                            );
                        }

                        // send warning to console
                        updateCheckerResult.forEach(Utils.logger::warning);
                    }
                });
            } catch (NoSuchMethodError err) {
                Utils.logger.error("&f(Update Checker) &7The update checker only supports &bMC 1.11+&7. Your server's version is &b" + Bukkit.getVersion() + "&7. Please disable '&bcheck-for-updates&7' in &bsettings.yml&7.");
            }
        }
    }

    public void unhookExternalPlugins() {
        Utils.logger.info("&f(Hook) &7Un-hooking from external plugins...");

        if (Bukkit.getPluginManager().getPlugin("Vault") != null) {
            Utils.logger.info("&f(Hook) &bVault&7 found, un-hooking...");
            Bukkit.getServicesManager().unregister(Economy.class, main.vaultHoook);
        }
    }

    public void disableStorage() {
        Utils.logger.info("&f(Storage) &7Disabling the storage system...");
        main.storageManager.storage.disable();
    }
}
