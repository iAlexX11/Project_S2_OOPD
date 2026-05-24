package org.cryptoBros.business.Liseners;

/**
 * Notifies observers when a user's balance changes.
 */
public interface BalanceListener {
    /**
     * Called when the user's balance has changed.
     *
     * @param balance the updated balance
     */
    void balanceChanged(double balance);
}
