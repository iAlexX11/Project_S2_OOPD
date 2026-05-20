package org.cryptoBros.presentation.Views;

import javax.swing.*;
import java.awt.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class PriceChart extends JPanel {

    private final List<Double> prices;
    private final List<Timestamp> times;

    private PriceChart () {
        prices = new ArrayList<>();
        times = new ArrayList<>();

        setBackground(new Color(20, 20, 35));
        setPreferredSize(new Dimension(600, 220));
    }

    public void addPoint (double price, Timestamp time) {
        prices.add(price);
        times.add(time);
        repaint();
    }

    public void loadData (List<Double> prices, List<Timestamp> times) {
        this.prices.clear();
        this.times.clear();
        this.prices.addAll(prices);
        this.times.addAll(times);
        repaint();

    }

    public void clear () {
        prices.clear();
        times.clear();
    }

    public void paintChart () {

    }

}
