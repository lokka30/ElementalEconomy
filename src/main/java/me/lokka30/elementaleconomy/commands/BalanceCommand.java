package me.lokka30.elementaleconomy.commands;

import me.lokka30.elementaleconomy.ElementalEconomy;
import me.lokka30.elementaleconomy.currencies.Currency;
import me.lokka30.elementaleconomy.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class BalanceCommand implements TabExecutor {

    private final ElementalEconomy main;

    public BalanceCommand(final ElementalEconomy main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("elementaleconomy.command.balance")) {
            sender.sendMessage("No permission.");
            return true;
        }

        if (args.length == 0) {
            if (sender instanceof Player) {
                final Player player = (Player) sender;
                final BigDecimal balance = main.accountManager.getAccount(player.getUniqueId()).getBalance(main.currencyManager.getDefaultCurrency().id);
                sender.sendMessage(player.getName() + "'s balance is " + main.currencyManager.getDefaultCurrency().getFormattedAmount(balance));
            } else {
                sender.sendMessage("Usage (console): /" + label + " <player> [balance]");
            }
        } else if (args.length == 1 || args.length == 2) {
            if (!sender.hasPermission("elementaleconomy.command.balance.others")) {
                sender.sendMessage("No permission.");
                return true;
            }

            final OfflinePlayer offlinePlayer = Bukkit.getPlayer(args[0]);
            if (offlinePlayer == null || !(offlinePlayer.hasPlayedBefore() || offlinePlayer.isOnline())) {
                sender.sendMessage(args[0] + " has not joined this server before.");
                return true;
            }

            final Currency currency;
            if (args.length == 1) {
                currency = main.currencyManager.getDefaultCurrency();
            } else {
                if (main.currencyManager.currencyNameIdMap.containsKey(args[1].toLowerCase())) {
                    currency = main.currencyManager.getCurrency(args[1].toLowerCase());
                } else {
                    sender.sendMessage("Invalid currency '" + args[1] + "'.");
                    return true;
                }
            }

            final BigDecimal balance = main.accountManager.getAccount(offlinePlayer.getUniqueId()).getBalance(currency.id);
            sender.sendMessage(offlinePlayer.getName() + "'s balance is " + currency.getFormattedAmount(balance));
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> suggestions = new ArrayList<>();

        if (sender.hasPermission("elementaleconomy.command.balance")) {
            if (args.length == 0) {
                if (sender instanceof Player) {
                    suggestions.addAll(Utils.getUsernamesOfVisibleOnlinePlayers((Player) sender));
                } else {
                    suggestions.addAll(Utils.getUsernamesOfOnlinePlayers());
                }
            } else if (args.length == 1) {
                suggestions.addAll(main.currencyManager.currencyNameIdMap.keySet());
            }
        }

        return suggestions;
    }
}
