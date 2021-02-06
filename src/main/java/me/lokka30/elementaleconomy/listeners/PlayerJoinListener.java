package me.lokka30.elementaleconomy.listeners;

import me.lokka30.elementaleconomy.ElementalEconomy;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final ElementalEconomy main;
    public PlayerJoinListener(final ElementalEconomy main) { this.main = main; }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onJoin(final PlayerJoinEvent event) {
        if(!main.accountManager.accountExists(event.getPlayer().getUniqueId())) {
            main.accountManager.createOrResetAccount(event.getPlayer().getUniqueId());
        }
        main.accountManager.cachedAccounts.put(event.getPlayer().getUniqueId(), main.storageManager.storage.getAccount(event.getPlayer().getUniqueId()));
    }
}
