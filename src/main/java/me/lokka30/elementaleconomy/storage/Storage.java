package me.lokka30.elementaleconomy.storage;

import me.lokka30.elementaleconomy.accounts.Account;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Methods accessed by the ElementalEconomy databases.
 * <p>
 * It is highly unlikely you need to use any methods here. See AccountManager instead.
 */
public interface Storage {

    /**
     * Start-up the storage system.
     */
    void enable();

    /**
     * Shut-down the storage system.
     */
    void disable();

    /**
     * @param uuid uuid of the player
     * @return their account
     */
    Account getAccount(UUID uuid);

    /**
     * @param uuid uuid to check
     * @return if their account is stored in EE's storage system - i.e. if EE has any data on them.
     */
    boolean accountExists(UUID uuid);

    /**
     * @param account account to save to the storage system
     */
    void saveAccount(Account account);

    /**
     * @return the next available account id
     */
    int getNextId();

    /**
     * Set the account's balance of certain currency to a value
     *
     * @param accountId account id
     * @param currency  currency
     * @param balance   amount
     */
    void setBalance(int accountId, int currencyId, BigDecimal balance);

    /**
     * This method is unused since balances are cached when an account is loaded via getAccount(UUID).
     *
     * @param accountId account id
     * @param currency  currency
     * @return balance
     */
    @SuppressWarnings("unused")
    BigDecimal getBalance(int accountId, int currencyId);
}
