package org.cryptoBros.presentation.ListenersPersistence;

import org.cryptoBros.presentation.Enum.PagesName;

/**
 * Defines navigation operations between application pages.
 */
public interface Navigation {
    /**
     * Navigates to the specified page.
     *
     * @param name the target page
     */
    void navigate(PagesName name);

    /**
     * Navigates to the crypto detail page for the given symbol.
     *
     * @param name the crypto symbol to display
     */
    void navigateToCryptoDetail(String name);
}
