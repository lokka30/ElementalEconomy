package me.lokka30.elementaleconomy.storage;

import me.lokka30.elementaleconomy.ElementalEconomy;
import me.lokka30.elementaleconomy.utils.Utils;

public class StorageManager {

    private final ElementalEconomy main;
    public StorageManager(final ElementalEconomy main) { this.main = main; }

    public StorageMethod storageMethod;
    public Storage storage;

    public enum StorageMethod {
        MYSQL, SQLITE, YAML;

        public static StorageMethod fromString(String str) {
            if(str == null || str.isEmpty()) {
                Utils.logger.error("Invalid storage method in &bsettings.yml&7! Using default method of &bYAML&7, &cfix this as soon as possible&7!");
                return YAML;
            }

            switch(str.toLowerCase()) {
                case "mysql":
                    return MYSQL;
                case "sqlite":
                    return SQLITE;
                case "yaml":
                    return YAML;
                default:
                    Utils.logger.error("Invalid storage method in &bsettings.yml&7! Using default method of &bYAML&7, &cfix this as soon as possible&7!");
                    return YAML;
            }
        }
    }

    public void loadStorage() {
        if(storage != null) {
            storage.disable();
        }

        storageMethod = StorageMethod.fromString(main.settings.getConfig().getString("storage.method"));

        switch(storageMethod) {
            case YAML:
                storage = new YAMLStorage(main);
                break;
            case SQLITE:
                storage = new SQLiteStorage(main);
                break;
            case MYSQL:
                storage = new MySQLStorage(main);
                break;
            default:
                throw new IllegalStateException("Unexpected StorageMethod " + storageMethod.toString() + "!");
        }

        storage.enable();
    }
}
