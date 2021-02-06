package me.lokka30.elementaleconomy.listeners;

import me.lokka30.elementaleconomy.ElementalEconomy;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    private final ElementalEconomy main;
    public PlayerQuitListener(final ElementalEconomy main) { this.main = main; }

    @EventHandler
    public void onQuit(final PlayerQuitEvent event) {
        main.accountManager.getAccount(event.getPlayer().getUniqueId()).saveBalancesToStorage();
        main.accountManager.cachedAccounts.remove(event.getPlayer().getUniqueId());
    }
}
