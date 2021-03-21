package me.lokka30.elementaleconomy.currencies;

import me.lokka30.elementaleconomy.ElementalEconomy;
import me.lokka30.elementaleconomy.utils.Utils;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;

public class BaltopManager {

    private final ElementalEconomy main;

    public BaltopManager(final ElementalEconomy main) {
        this.main = main;
    }

    /**
     * CurrencyID, <AccountID, Balance>
     */
    public Map<Integer, SortedMap<Integer, BigDecimal>> topBalances = new HashMap<>();

    /**
     * Asynchronously (re)sets the balance cache
     */
    public void resetCache() {
        Utils.sendDebugMessage(Utils.DebugMessageType.BALTOP_CACHING, "Resetting baltop cache");

        if (main.settings.getConfig().getBoolean("baltop.enabled", true)) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    topBalances = main.storageManager.storage.getTopBalances();
                }
            }.runTaskAsynchronously(main);
        } else {
            Utils.sendDebugMessage(Utils.DebugMessageType.BALTOP_CACHING, "Skipping resetCache, baltop disabled in settings");
        }
    }

    public void startAutoCacheTask() {
        if (!main.settings.getConfig().getBoolean("baltop.enabled", true)) {
            Utils.sendDebugMessage(Utils.DebugMessageType.BALTOP_CACHING, "Skipping startAutoCacheTask, baltop disabled in settings");
            return;
        }

        int minutes = main.settings.getConfig().getInt("baltop.auto-cache-task-period", 15);

        if (minutes <= 0) {
            Utils.sendDebugMessage(Utils.DebugMessageType.BALTOP_CACHING, "Auto cache task disabled in settings.");
            return;
        }

        long ticks = 20L * 60 * minutes;

        if (autoCacheTask != null) autoCacheTask.cancel();

        autoCacheTask = new BukkitRunnable() {
            @Override
            public void run() {
                resetCache();
            }
        }.runTaskTimer(main, ticks, ticks);
    }

    public BukkitTask autoCacheTask = null;
}
