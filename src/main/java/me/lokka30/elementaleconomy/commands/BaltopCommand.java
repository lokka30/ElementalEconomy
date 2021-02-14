package me.lokka30.elementaleconomy.commands;

import me.lokka30.elementaleconomy.ElementalEconomy;
import me.lokka30.elementaleconomy.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.ArrayList;
import java.util.List;

public class BaltopCommand implements TabExecutor {

    private final ElementalEconomy main;

    public BaltopCommand(final ElementalEconomy main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        sender.sendMessage("Command is incomplete.");
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
