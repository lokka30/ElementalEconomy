package me.lokka30.elementaleconomy.accounts;

import me.lokka30.elementaleconomy.ElementalEconomy;
import java.util.HashMap;
import java.util.UUID;

public class AccountManager {

    private final ElementalEconomy main;
    public AccountManager(ElementalEconomy main) { this.main = main; }

    public HashMap<UUID, Account> cachedAccounts = new HashMap<>();

    public Account getAccount(UUID uuid) {
        return cachedAccounts.getOrDefault(uuid, main.storageManager.storage.getAccount(uuid));
    }

    public boolean accountExists(UUID uuid) {
        return main.storageManager.storage.accountExists(uuid);
    }

    public void createOrResetAccount(UUID uuid) {
        main.storageManager.storage.saveAccount(new Account(main.storageManager.storage.getNextId(), uuid, new HashMap<>()));
    }
}
