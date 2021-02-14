package me.lokka30.elementaleconomy.commands;

import me.lokka30.elementaleconomy.ElementalEconomy;
import me.lokka30.elementaleconomy.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class ElementalEconomyCommand implements TabExecutor {

    private final ElementalEconomy main;

    public ElementalEconomyCommand(final ElementalEconomy main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        sender.sendMessage("Command is incomplete.");

        if (!sender.hasPermission("elementaleconomy.command.elementaleconomy")) {
            sender.sendMessage("No permission.");
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage("Usage: /" + label + " info/reload/backup/compatibility");
        } else {
            switch (args[0].toUpperCase()) {
                case "INFO":
                    if (sender.hasPermission("elementaleconomy.command.elementaleconomy.info")) {
                        sender.sendMessage("Running ElementalEconomy v" + main.getDescription().getVersion() + " by " + String.join(", ", Utils.contributors));
                    } else {
                        sender.sendMessage("No permission.");
                    }
                    break;
                case "RELOAD":
                    if (sender.hasPermission("elementaleconomy.command.elementaleconomy.reload")) {
                        sender.sendMessage("Reload started.");

                        sender.sendMessage("Reloading files...");
                        main.companion.loadFiles();

                        sender.sendMessage("Reloading storage syste,...");
                        main.companion.loadStorage();

                        sender.sendMessage("Reloading currencies...");
                        main.companion.loadCurrencies();

                        sender.sendMessage("Reload complete.");
                    } else {
                        sender.sendMessage("No permission.");
                    }
                    break;
                case "BACKUP":
                    if (sender.hasPermission("elementaleconomy.command.elementaleconomy.backup")) {
                        sender.sendMessage("Backing up ElementalEconomy folder...");
                        backupFiles();
                        sender.sendMessage("Backup complete.");
                    } else {
                        sender.sendMessage("No permission.");
                    }
                    break;
                case "COMPATIBILITY":
                    if (sender.hasPermission("elementaleconomy.command.elementaleconomy.compatibility")) {
                        sender.sendMessage("Running compatibility check...");
                        main.companion.checkCompatibility();
                        sender.sendMessage("Compatibility check complete, found " + main.companion.possibleIncompatibilitiesAmount + " possible incompatibilities. Check console for more information.");
                    } else {
                        sender.sendMessage("No permission.");
                    }
                    break;
                default:
                    sender.sendMessage("Invalid usage.");
                    break;
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> suggestions = new ArrayList<>();

        if (args.length == 0) {
            if (sender.hasPermission("elementaleconomy.command.elementaleconomy.info")) suggestions.add("info");
            if (sender.hasPermission("elementaleconomy.command.elementaleconomy.reload")) suggestions.add("reload");
            if (sender.hasPermission("elementaleconomy.command.elementaleconomy.backup")) suggestions.add("backup");
            if (sender.hasPermission("elementaleconomy.command.elementaleconomy.compatibility"))
                suggestions.add("compatibility");
        }

        return suggestions;
    }

    private void backupFiles() {
        try {
            Path oldFolder = main.getDataFolder().toPath();
            Path newFolder = new File(oldFolder.toString() + File.separator + System.currentTimeMillis()).toPath();
            Files.copy(oldFolder, newFolder, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            Utils.logger.error("An IO error occured whilst attempting to backup the data folder. Stack trace:");
            e.printStackTrace();
        }
    }
}
