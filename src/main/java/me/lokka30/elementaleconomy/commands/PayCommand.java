package me.lokka30.elementaleconomy.commands;

import me.lokka30.elementaleconomy.ElementalEconomy;
import me.lokka30.elementaleconomy.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PayCommand implements TabExecutor {

    private final ElementalEconomy main;

    public PayCommand(final ElementalEconomy main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("elementaleconomy.command.pay")) {
            sender.sendMessage("No permission.");
            return true;
        }

        if (args.length == 2 || args.length == 3) {
            sender.sendMessage("Command is incomplete.");
        } else {
            sender.sendMessage("Usage: /" + label + " <player> <amount> [currency]");
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> suggestions = new ArrayList<>();

        if (args.length == 0) {
            if (sender instanceof Player) {
                suggestions.addAll(Utils.getUsernamesOfVisibleOnlinePlayers((Player) sender));
            } else {
                suggestions.addAll(Utils.getUsernamesOfOnlinePlayers());
            }
        } else if (args.length == 1) {
            suggestions.addAll(Utils.oneThruNine);
        } else if (args.length == 2) {
            suggestions.addAll(main.currencyManager.currencyNameIdMap.keySet());
        }

        return suggestions;
    }
}
