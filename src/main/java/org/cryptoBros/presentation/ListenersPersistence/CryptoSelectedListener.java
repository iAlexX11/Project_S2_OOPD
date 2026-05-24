package org.cryptoBros.presentation.ListenersPersistence;

/** Notifies observers when a cryptocurrency row is selected. */
public interface CryptoSelectedListener {
    /**
     * Called when a cryptocurrency is selected.
     *
     * @param type the selected cryptocurrency type
     */
    void cryptoSelected(String type);
}
