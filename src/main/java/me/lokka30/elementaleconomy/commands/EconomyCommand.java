package me.lokka30.elementaleconomy.commands;

import me.lokka30.elementaleconomy.ElementalEconomy;
import me.lokka30.elementaleconomy.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class EconomyCommand implements TabExecutor {

    private final ElementalEconomy main;

    public EconomyCommand(final ElementalEconomy main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!sender.hasPermission("elementaleconomy.command.economy")) {
            sender.sendMessage("No permission.");
            return true;
        }

        if (args.length == 1) {
            switch (args[0].toUpperCase()) {
                case "GIVE":
                case "ADD":
                    sender.sendMessage("Command is incomplete. (GIVE)");
                    break;
                case "TAKE":
                case "REMOVE":
                    sender.sendMessage("Command is incomplete. (TAKE)");
                    break;
                case "SET":
                    sender.sendMessage("Command is incomplete. (SET)");
                    break;
                case "RESET":
                    sender.sendMessage("Command is incomplete. (RESET)");
                    break;
                default:
                    sender.sendMessage("Usage: /" + label + " <give|add/take|remove/set/reset> <player> ...");
                    break;
            }
        } else {
            sender.sendMessage("Usage: /" + label + " <give|add/take|remove/set/reset> <player> ...");
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> suggestions = new ArrayList<>();

        if (sender.hasPermission("elementaleconomy.command.economy")) {
            if (args.length == 0) {
                if (sender.hasPermission("elementaleconomy.command.economy.give")) suggestions.add("give");
                if (sender.hasPermission("elementaleconomy.command.economy.take")) suggestions.add("take");
                if (sender.hasPermission("elementaleconomy.command.economy.reset")) suggestions.add("reset");
                if (sender.hasPermission("elementaleconomy.command.economy.set")) suggestions.add("set");
            } else {
                switch (args[0].toUpperCase()) {
                    // give|add, take|remove & set all share the same args, which is why there is no definite 'break' except for under 'SET'.
                    case "GIVE":
                    case "ADD":
                        if (!sender.hasPermission("elementaleconomy.command.economy.give")) break;
                    case "TAKE":
                    case "REMOVE":
                        if (!sender.hasPermission("elementaleconomy.command.economy.take")) break;
                    case "SET":
                        if (!sender.hasPermission("elementaleconomy.command.economy.set")) break;

                        if (args.length == 1) {
                            if (sender instanceof Player) {
                                suggestions.addAll(Utils.getUsernamesOfVisibleOnlinePlayers((Player) sender));
                            } else {
                                suggestions.addAll(Utils.getUsernamesOfOnlinePlayers());
                            }
                        } else if (args.length == 2) {
                            suggestions.addAll(Utils.oneThruNine);
                        } else if (args.length == 3) {
                            suggestions.addAll(main.currencyManager.currencyNameIdMap.keySet());
                        }
                        break;
                    case "RESET":
                        if (sender.hasPermission("elementaleconomy.command.economy.reset")) {
                            if (args.length == 1) {
                                if (sender instanceof Player) {
                                    suggestions.addAll(Utils.getUsernamesOfVisibleOnlinePlayers((Player) sender));
                                } else {
                                    suggestions.addAll(Utils.getUsernamesOfOnlinePlayers());
                                }
                            } else if (args.length == 2) {
                                suggestions.addAll(main.currencyManager.currencyNameIdMap.keySet());
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
        }

        return suggestions;
    }
}
