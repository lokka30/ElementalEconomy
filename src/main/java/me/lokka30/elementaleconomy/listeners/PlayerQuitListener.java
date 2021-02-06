package me.lokka30.elementaleconomy.listeners;

import me.lokka30.elementaleconomy.ElementalEconomy;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class PlayerQuitListener implements Listener {

    private final ElementalEconomy main;

    public PlayerQuitListener(final ElementalEconomy main) {
        this.main = main;
    }

    /**
     * This method is called by Bukkit when a player leaves the server.
     * <p>
     * ignoreCancelled = true ~ this means the method will not be called if another plugin has cancelled this instance of PlayerQuitEvent.
     * priority = EventPriority.MONITOR ~ this means that Bukkit will treat this event with the lowest priority. This is used since ElemEcon doesn't need to adapt the event.
     *
     * @param event PlayerQuitEvent
     */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onQuit(final PlayerQuitEvent event) {
        saveAccountBalancesToStorage(event.getPlayer().getUniqueId());
        removeAccountFromCache(event.getPlayer().getUniqueId());
    }

    /**
     * This grabs the cached balances of the player's account and saves it to the storage system.
     *
     * @param uuid player to save cached balances of
     */
    private void saveAccountBalancesToStorage(UUID uuid) {
        main.accountManager.getAccount(uuid).saveBalancesToStorage();
    }

    /**
     * This removes the player's account from the cache, saving memory since the player is unlikely to have their balances get/set.
     *
     * @param uuid player to remove teh cached balance of
     */
    private void removeAccountFromCache(UUID uuid) {
        main.accountManager.cachedAccounts.remove(uuid);
    }
}
