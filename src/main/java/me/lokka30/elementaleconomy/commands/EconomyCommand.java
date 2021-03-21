package me.lokka30.elementaleconomy.commands;

import me.lokka30.elementaleconomy.ElementalEconomy;
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
import java.util.UUID;

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
                case "DEPOSIT":
                    parseGive(sender, label, args);
                    break;
                case "TAKE":
                case "REMOVE":
                case "WITHDRAW":
                    parseTake(sender, label, args);
                    break;
                case "SET":
                    parseSet(sender, label, args);
                    break;
                case "RESET":
                    parseReset(sender, label, args);
                    break;
                default:
                    sender.sendMessage("Usage: /" + label + " <give|add|deposit / take|remove|withdraw / set / reset> <player> ...");
                    break;
            }
        } else {
            sender.sendMessage("Usage: /" + label + " <give|add|deposit / take|remove|withdraw / set / reset> ...");
        }

        return true;
    }

    void parseGive(CommandSender sender, String label, String[] args) {
        if (!sender.hasPermission("elementaleconomy.command.economy.give")) {
            sender.sendMessage("No permission");
            return;
        }

        // Usage: /eco give <player> <amount> [currency]
        // args   |    |0   |1       |2       |3
        // len    |0   |1   |2       |3       |4

        if (args.length != 3 && args.length != 4) {
            sender.sendMessage("Usage: /" + label + " " + args[0].toLowerCase() + " <player> <amount> [currency]");
            return;
        }

        /*
        check player
         */
        UUID uuid = null;

        Player player = Bukkit.getPlayer(args[1]);
        if (player.isOnline()) uuid = player.getUniqueId();

        @SuppressWarnings("deprecation") // the deprecated method is intended
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
        if (offlinePlayer.hasPlayedBefore() || offlinePlayer.isOnline()) uuid = offlinePlayer.getUniqueId();

        if (uuid == null) {
            sender.sendMessage("Player " + args[1] + " hasn't joined this server before.");
            return;
        }

        /*
        check amount
         */
        final BigDecimal amount;

        // set the amount, and make sure it is valid
        try {
            amount = new BigDecimal(args[2]);
        } catch (NumberFormatException ex) {
            sender.sendMessage("Invalid amount " + args[2] + ": unable to parse format");
            return;
        }

        // make sure the amount is greater than zero. users should use 'eco take' to withdraw amounts
        if (amount.compareTo(BigDecimal.ZERO) != 1) {
            sender.sendMessage("Invalid amount " + args[2] + ": must be greater than zero");
        }

        /*
        check currency
         */
        final int currencyId;

        // use specified currency, and if unspecified, use the default one
        if (args.length == 4) {
            if (main.currencyManager.currencyNameIdMap.containsKey(args[3])) {
                currencyId = main.currencyManager.currencyNameIdMap.get(args[3]);
            } else {
                sender.sendMessage("Invalid currency " + args[3] + ".");
                return;
            }
        } else {
            currencyId = main.currencyManager.getDefaultCurrency().id;
        }

        /*
        operation
         */
        // set balance
        main.accountManager.getAccount(uuid).deposit(currencyId, amount);

        // send message
        String formattedAmount = main.currencyManager.getCurrency(currencyId).getFormattedAmount(amount);

        player.sendMessage("Added " + formattedAmount + " to " + args[1] + "'s balance.");
    }

    void parseTake(CommandSender sender, String label, String[] args) {
        if (!sender.hasPermission("elementaleconomy.command.economy.take")) {
            sender.sendMessage("No permission");
            return;
        }

        // Usage: /eco take <player> <amount> [currency]
        // args   |    |0   |1       |2       |3
        // len    |0   |1   |2       |3       |4

        if (args.length != 3 && args.length != 4) {
            sender.sendMessage("Usage: /" + label + " " + args[0].toLowerCase() + " <player> <amount> [currency]");
            return;
        }

        /*
        check player
         */
        UUID uuid = null;

        Player player = Bukkit.getPlayer(args[1]);
        if (player.isOnline()) uuid = player.getUniqueId();

        @SuppressWarnings("deprecation") // the deprecated method is intended
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
        if (offlinePlayer.hasPlayedBefore() || offlinePlayer.isOnline()) uuid = offlinePlayer.getUniqueId();

        if (uuid == null) {
            sender.sendMessage("Player " + args[1] + " hasn't joined this server before.");
            return;
        }

        /*
        check amount
         */
        final BigDecimal amount;

        // set the amount, and make sure it is valid
        try {
            amount = new BigDecimal(args[2]);
        } catch (NumberFormatException ex) {
            sender.sendMessage("Invalid amount " + args[2] + ": unable to parse format");
            return;
        }

        // make sure the amount is greater than zero. users should use 'eco take' to withdraw amounts
        if (amount.compareTo(BigDecimal.ZERO) != 1) {
            sender.sendMessage("Invalid amount " + args[2] + ": must be greater than zero");
        }

        /*
        check currency
         */
        final int currencyId;

        // use specified currency, and if unspecified, use the default one
        if (args.length == 4) {
            if (main.currencyManager.currencyNameIdMap.containsKey(args[3])) {
                currencyId = main.currencyManager.currencyNameIdMap.get(args[3]);
            } else {
                sender.sendMessage("Invalid currency " + args[3] + ".");
                return;
            }
        } else {
            currencyId = main.currencyManager.getDefaultCurrency().id;
        }

        // formatted amount from currency
        String formattedAmount = main.currencyManager.getCurrency(currencyId).getFormattedAmount(amount);

        /*
        check if target has enough funds to be removed
         */
        if (!main.accountManager.getAccount(uuid).has(currencyId, amount)) {
            sender.sendMessage(args[1] + "'s balance does not exceed and is not equal to " + formattedAmount + ".");
            return;
        }

        /*
        operation
         */
        // set balance
        main.accountManager.getAccount(uuid).withdraw(currencyId, amount);

        // send message
        player.sendMessage("Withdrew " + formattedAmount + " from " + args[1] + "'s balance.");
    }

    void parseSet(CommandSender sender, String label, String[] args) {
        sender.sendMessage("Command is incomplete. (SET)");
    }

    void parseReset(CommandSender sender, String label, String[] args) {
        sender.sendMessage("Command is incomplete. (RESET)");
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
        } else {
            sender.sendMessage("No permission");
        }

        return suggestions;
    }
}
