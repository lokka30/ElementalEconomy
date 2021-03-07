package me.lokka30.elementaleconomy.currencies;

import me.lokka30.elementaleconomy.ElementalEconomy;
import me.lokka30.elementaleconomy.misc.DebugMessageType;
import me.lokka30.elementaleconomy.utils.Utils;
import org.bukkit.scheduler.BukkitRunnable;

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
        Utils.sendDebugMessage(DebugMessageType.BALTOP_CACHING, "Resetting baltop cache");
        new BukkitRunnable() {
            @Override
            public void run() {
                topBalances = main.storageManager.storage.getTopBalances();
            }
        }.runTaskAsynchronously(main);
    }
}
