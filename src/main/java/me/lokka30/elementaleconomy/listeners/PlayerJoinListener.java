package me.lokka30.elementaleconomy.listeners;

import me.lokka30.elementaleconomy.ElementalEconomy;
import me.lokka30.microlib.MicroUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class PlayerJoinListener implements Listener {

    private final ElementalEconomy main;

    public PlayerJoinListener(final ElementalEconomy main) {
        this.main = main;
    }

    /**
     * This method is called by Bukkit when a player joins the server.
     * <p>
     * ignoreCancelled = true ~ this means the method will not be called if another plugin has cancelled this instance of PlayerJoinEvent.
     * priority = EventPriority.MONITOR ~ this means that Bukkit will treat this event with the lowest priority. This is used since ElemEcon doesn't need to adapt the event.
     *
     * @param event PlayerJoinEvent
     */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onJoin(final PlayerJoinEvent event) {
        loadAccount(event.getPlayer().getUniqueId());
        checkUpdateResult(event.getPlayer());
    }

    /**
     * Creates the player's account if it doesn't exist.
     * Loads their account into the cache.
     *
     * @param uuid uuid of the player to load
     */
    private void loadAccount(UUID uuid) {
        if (!main.accountManager.accountExists(uuid)) {
            main.accountManager.createOrResetAccount(uuid);
        }
        main.accountManager.cachedAccounts.put(uuid, main.storageManager.storage.getAccount(uuid));
    }

    /**
     * Notify the player of an available update if the update checker detects one.
     * Player also requires permission to be notified.
     *
     * @param player player to send the message to, if update is available & permission is met
     */
    private void checkUpdateResult(Player player) {
        if (main.companion.updateCheckerResult != null && player.hasPermission("elementaleconomy.update-notifications")) {
            main.companion.updateCheckerResult.forEach(message -> player.sendMessage(MicroUtils.colorize(message)));
        }
    }
}
