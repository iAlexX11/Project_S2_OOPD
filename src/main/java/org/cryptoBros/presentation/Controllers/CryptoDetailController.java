package org.cryptoBros.presentation.Controllers;

import org.cryptoBros.business.Liseners.BalanceListener;
import org.cryptoBros.business.Liseners.CryptoListener;
import org.cryptoBros.business.Liseners.GraphPriceListener;
import org.cryptoBros.persistence.Exceptions.CryptoNotFoundException;
import org.cryptoBros.persistence.Exceptions.DbConnectionException;
import org.cryptoBros.presentation.Enum.ButtonEnumeration;
import org.cryptoBros.presentation.Enum.PagesName;
import org.cryptoBros.presentation.ListenersPersistence.Navigation;
import org.cryptoBros.presentation.Views.BaseView;
import org.cryptoBros.presentation.Views.CryptoDetailView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CryptoDetailController implements ActionListener, BalanceListener, CryptoListener, GraphPriceListener {

    private final UserController userController;
    private final AdminController adminController;
    private final Navigation navigation;
    private final CryptoDetailView view;
    private String currentSymbol;

    public CryptoDetailController (UserController userController, AdminController adminController, Navigation navigation, boolean isAdmin) {
        this.userController = userController;
        this.adminController = adminController;
        this.navigation = navigation;

        this.view = new CryptoDetailView();
        view.setTypeUser(isAdmin);
        view.setActions(this);
        if (!isAdmin) {
            userController.registerBalanceListener(this);
            userController.pushCurrentBalance(this);
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ButtonEnumeration buttonEnumeration = ButtonEnumeration.valueOf(e.getActionCommand());
        switch (buttonEnumeration) {
            case SETTINGS -> navigation.navigate(PagesName.SETTING);
            case HOME -> navigation.navigate(PagesName.CRYPTO_MARKET);
            case PORTFOLIO -> navigation.navigate(PagesName.PORTFOLIO);
            case MANAGE_CRYPTO -> navigation.navigate(PagesName.MANAGE_CRYPTO);
            case CONFIRM_PURCHASE -> buyCrypto();
        }
    }

    private void buyCrypto() {
        double units = view.getUnits();
        userController.buyCrypto(currentSymbol, units);
        view.setOwnedCrypto(userController.getOwnedUnits(currentSymbol), currentSymbol);

    }

    @Override
    public void balanceChanged(double balance) {
        view.updateBalance(balance);
        view.updateEstimatedProfit(userController.getTotalProfit());
    }

    public BaseView getView() {
        return view;
    }

    public void stop() {
        userController.stopGraphWorker();
    }

    public void displayContent(String symbol) {
        this.currentSymbol = symbol;
        view.setCryptoName(userController.getCryptoName(symbol));
        view.setOwnedCrypto(userController.getOwnedUnits(symbol), symbol);
        userController.setGraphicWorker(this, symbol);
    }

    @Override
    public void updateGraph(Map<Instant, Double> cryptoHistory) {
        SwingUtilities.invokeLater(() -> {
            List<Double> prices = new ArrayList<>(cryptoHistory.values());
            List<String> times  = cryptoHistory.keySet().stream()
                    .map(instant -> LocalTime.ofInstant(instant, ZoneId.systemDefault())
                            .format(DateTimeFormatter.ofPattern("HH:mm")))
                    .toList();
            view.loadAllHistory(prices, times);
        });
    }

    @Override
    public void updateData(String symbol, String name, double currentPrice, double change, double percentage) {
        if (symbol.equals(currentSymbol)) {
            view.setCurrentPrice(currentPrice);
        }
        view.updateEstimatedProfit(userController.getTotalProfit());
    }
}
