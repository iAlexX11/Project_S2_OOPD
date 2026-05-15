package org.cryptoBros.presentation.ListenersPersistence;

import org.cryptoBros.presentation.Enum.PagesName;
import org.cryptoBros.presentation.Views.BaseView;
import org.cryptoBros.presentation.Views.Pages;

public interface Navigation {
    void navigate(PagesName name);
}
