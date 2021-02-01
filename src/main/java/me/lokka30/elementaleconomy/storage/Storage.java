package me.lokka30.elementaleconomy.storage;

import me.lokka30.elementaleconomy.accounts.Account;
import me.lokka30.elementaleconomy.currencies.Currency;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Methods accessed by the ElementalEconomy databases.
 *
 * Only access methods such as getBalance, setBalance, accountExists, etc, if you are sure they need to be retrieved from the database rather than the AccountManager cache.
 */
public interface Storage {

    void enable();

    void disable();

    Account getAccount(UUID uuid);

    boolean accountExists(UUID uuid);

    void saveAccount(Account account);

    int getNextId();

    void setBalance(int id, Currency currency, BigDecimal balance);

    BigDecimal getBalance(int id, Currency currency);
}
