package org.cryptoBros.presentation.ListenersPersistence;

import org.cryptoBros.presentation.Enum.PagesName;

public interface Navigation {
    void navigate(PagesName name);

    void navigateToCryptoDetail(String name);
}
