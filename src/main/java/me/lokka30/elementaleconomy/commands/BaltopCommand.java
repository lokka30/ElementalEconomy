package me.lokka30.elementaleconomy.commands;

import me.lokka30.elementaleconomy.ElementalEconomy;
import me.lokka30.elementaleconomy.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

public class BaltopCommand implements TabExecutor {

    private final ElementalEconomy main;

    public BaltopCommand(final ElementalEconomy main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("elementaleconomy.command.baltop")) {
            sender.sendMessage("No permission.");
            return true;
        }

        if (args.length > 2) {
            sender.sendMessage("Usage: /" + label + " [currency] [page]");
            return true;
        }

        if (!main.settings.getConfig().getBoolean("baltop.enabled", true)) {
            sender.sendMessage("Baltop is disabled on this server.");
            return true;
        }

        int currencyId = main.currencyManager.getDefaultCurrency().id;
        if (args.length > 0) {
            if (main.currencyManager.currencyNameIdMap.containsKey(args[0].toLowerCase())) {
                currencyId = main.currencyManager.currencyNameIdMap.get(args[0].toLowerCase());
            } else {
                sender.sendMessage("Invalid currency %currencyName%".replace("%currencyName%", args[0]));
                return true;
            }
        }

        int page = 1;
        if (args.length > 1) {
            if (Utils.isInteger(args[1])) {
                page = Integer.parseInt(args[1]);
                if (page < 1) {
                    sender.sendMessage("%value% is not a valid page: must be a positive integer".replace("%value%", args[1]));
                    return true;
                }
            } else {
                sender.sendMessage("%value% is not a valid page: must be a positive integer".replace("%value%", args[1]));
                return true;
            }
        }

        SortedMap<Integer, BigDecimal> topBalances = main.baltopManager.topBalances.get(currencyId);
        int usersPerPage = main.settings.getConfig().getInt("baltop.users-per-page", 10);
        int index = 1;
        int minIndex = ((page - 1) * usersPerPage) + 1;
        int maxIndex = minIndex + usersPerPage - 1;
        String currencyName = main.currencyManager.getCurrency(currencyId).name;

        if (minIndex > topBalances.size()) {
            sender.sendMessage("No users to display on this page.");
            return true;
        }

        sender.sendMessage("Balances for currency %currencyName%, page %page%:"
                .replace("%currencyName%", currencyName)
                .replace("%page%", page + ""));
        sender.sendMessage("+------------------------------+");

        for (int accountId : topBalances.keySet()) {
            // min index
            if (index < minIndex) {
                index++;
                continue;
            }

            // max index
            if (index > maxIndex) {
                sender.sendMessage("+------------------------------+");
                sender.sendMessage("... Run /" + label + " " + currencyName + " " + (page + 1) + " to view the next page.");
                break;
            }

            String username = main.storageManager.storage.getUsernameFromAccountId(accountId);
            String formattedBalance = main.currencyManager.getCurrency(currencyId).getFormattedAmount(topBalances.get(accountId));

            sender.sendMessage(index + ". " + username + ": " + formattedBalance);

            index++;
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> suggestions = new ArrayList<>();

        if (sender.hasPermission("elementaleconomy.command.baltop")) {
            if (args.length == 0) {
                suggestions.addAll(main.currencyManager.currencyNameIdMap.keySet());
            } else if (args.length == 1) {
                suggestions.addAll(Utils.oneThruNine);
            }
        }

        return suggestions;
    }
}
