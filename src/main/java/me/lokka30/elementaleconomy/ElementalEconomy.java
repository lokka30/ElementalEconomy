package me.lokka30.elementaleconomy;

import me.lokka30.elementaleconomy.accounts.AccountManager;
import me.lokka30.elementaleconomy.currencies.BaltopManager;
import me.lokka30.elementaleconomy.currencies.CurrencyManager;
import me.lokka30.elementaleconomy.hooks.VaultHook;
import me.lokka30.elementaleconomy.storage.StorageManager;
import me.lokka30.elementaleconomy.utils.Utils;
import me.lokka30.microlib.QuickTimer;
import me.lokka30.microlib.YamlConfigFile;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;

public class ElementalEconomy extends JavaPlugin {

    // Static instance is only used by the Account class. Please use dep injection wherever else feasible.
    private static ElementalEconomy instance;
    public static ElementalEconomy getInstance() {
        return instance;
    }

    // This class handles startup & shutdown methods, keeps the main class looking cleaner.
    public final Companion companion = new Companion(this);

    // Manager classes contain methods in relation to what the class is named.
    public final AccountManager accountManager = new AccountManager(this);
    public final CurrencyManager currencyManager = new CurrencyManager(this);
    public final StorageManager storageManager = new StorageManager(this);
    public final BaltopManager baltopManager = new BaltopManager(this);

    // Here are the configuration files. These can optionally be edited by the server owner to adapt the plugin to their liking.
    // Using the 'YamlConfigFile' class from MicroLib. To get the actual config, just run YamlConfigFile#getConfig
    public final YamlConfigFile settings = new YamlConfigFile(this, new File(getDataFolder(), "settings.yml"));
    public final YamlConfigFile messages = new YamlConfigFile(this, new File(getDataFolder(), "messages.yml"));
    public final YamlConfigFile currencies = new YamlConfigFile(this, new File(getDataFolder(), "currencies.yml"));

    // Hooks with external plugins
    public final Economy vaultHoook = new VaultHook(this);

    @Override
    public void onEnable() {
        Utils.logger.info("&f~ Initiating start-up procedure ~");
        QuickTimer timer = new QuickTimer();

        instance = this;

        companion.checkCompatibility();
        companion.loadFiles();
        companion.loadStorage();
        companion.loadCurrencies();
        companion.loadBaltop();
        companion.registerListeners();
        companion.registerCommands();
        companion.hookExternalPlugins();

        Utils.logger.info("&f(Startup) &7Running misc procedures...");
        companion.startMetrics();
        companion.checkForUpdates();

        Utils.logger.info("&f~ Start-up complete, took &b" + timer.getTimer() + "ms&f ~");
    }

    @Override
    public void onDisable() {
        Utils.logger.info("&f~ Initiating shut-down procedure ~");
        QuickTimer timer = new QuickTimer();

        companion.unhookExternalPlugins();
        companion.disableBaltopTask();
        companion.disableStorage();

        Utils.logger.info("&f~ Shut-down complete, took &b" + timer.getTimer() + "ms&f ~");
    }

    /*
    These variables are used by ElementalEconomyCommand.java but are
    put here just so that they are easy to access and adapt in case
    a contributor needs to edit its values.
     */
    public final HashSet<String> developers = new HashSet<>(Collections.singletonList("lokka30"));
    public final HashSet<String> contributors = new HashSet<>(Collections.singletonList("N/A"));
    public final String supportedVersions = "1.6.x - 1.16.x";
}
