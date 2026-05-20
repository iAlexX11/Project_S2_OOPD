package org.cryptoBros.presentation.Views;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.GeneralPath;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class PriceChart extends JPanel {


    private static final int MAX_POINTS = 120;
    private static final double Y_MARGIN = 0.05;
    private static final int PAD_LEFT = 70;
    private static final int PAD_RIGHT = 20;
    private static final int PAD_TOP = 20;
    private static final int PAD_BOTTOM = 40;
    private static final int GRID_LINES = 5;

    private final List<Double> prices;
    private final List<String> times;

    public PriceChart () {
        prices = new ArrayList<>();
        times = new ArrayList<>();

        setBackground(new Color(20, 20, 35));
        setPreferredSize(new Dimension(400, 450));
    }

    public void addPoint (double price, String time) {
        if (prices.size() >= MAX_POINTS) {
            prices.remove(0);
            times.remove(0);
        }
        prices.add(price);
        times.add(time);
        repaint();
    }

    public void loadData (List<Double> prices, List<String> times) {
        this.prices.clear();
        this.times.clear();
        this.prices.addAll(prices);
        this.times.addAll(times);
        repaint();

    }

    public void clear () {
        prices.clear();
        times.clear();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        paintChart(g);
    }

    private void paintChart(Graphics g) {
        if (prices.size() < 2) return;

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int chartW = getWidth() - PAD_LEFT - PAD_RIGHT;
        int chartH = getHeight() - PAD_TOP - PAD_BOTTOM;

        double min   = prices.stream().mapToDouble(Double::doubleValue).min().orElse(0);
        double max   = prices.stream().mapToDouble(Double::doubleValue).max().orElse(1);
        double range = max - min == 0 ? 1 : max - min;
        double margin = range * Y_MARGIN;
        double drawMin = min - margin;
        double drawMax = max + margin;
        double drawRange = drawMax - drawMin;

        Color lineColor = prices.get(prices.size() - 1) >= prices.get(0) ? new Color(0, 180, 80) :  new Color(220, 80, 80);
        Color fillTop   = new Color(lineColor.getRed(), lineColor.getGreen(), lineColor.getBlue(), 80);
        Color fillBot   = new Color(lineColor.getRed(), lineColor.getGreen(), lineColor.getBlue(), 0);

        drawGrid(g2d, chartW, chartH, drawMax, drawRange);
        drawFill(g2d, chartW, chartH, drawMax, drawRange, fillTop, fillBot);
        drawLine(g2d, chartW, chartH, drawMax, drawRange, lineColor);
        drawXLabels(g2d, chartW);

        g2d.dispose();
    }

    private void drawFill(Graphics2D g2d, int chartW, int chartH, double max, double drawRange, Color fillTop, Color fillBot) {
        GeneralPath fill = buildPath(chartW, chartH, max, drawRange);

        //Open lines to closed shapes
        fill.lineTo(PAD_LEFT + chartW, PAD_TOP + chartH);
        fill.lineTo(PAD_LEFT, PAD_TOP + chartH);
        fill.closePath();

        g2d.setPaint(new GradientPaint(0, PAD_TOP, fillTop, 0, PAD_TOP + chartH, fillBot));
        g2d.fill(fill);
    }

    private void drawLine(Graphics2D g2d, int chartW, int chartH, double max, double drawRange, Color lineColor){
        g2d.setColor(lineColor);
        g2d.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.draw(buildPath(chartW, chartH, max, drawRange));
    }

    private GeneralPath buildPath(int chartW, int chartH, double drawMax, double drawRange) {
        GeneralPath path = new GeneralPath();
        for (int i = 0; i < prices.size(); i++) {
            float x = PAD_LEFT + (float) i / (prices.size() - 1) * chartW;
            float y = PAD_TOP  + (float) ((drawMax - prices.get(i)) / drawRange) * chartH;
            if (i == 0) path.moveTo(x, y);
            else        path.lineTo(x, y);
        }
        return path;
    }

    private void drawXLabels(Graphics2D g2d, int chartW) {
        if (times.isEmpty()) return;
        int step = Math.max(1, times.size() / 6);

        g2d.setColor(new Color(180, 180, 180));
        g2d.setFont(new Font("SansSerif", Font.PLAIN, 11));
        for (int i = 0; i < times.size(); i += step) {
            float x = PAD_LEFT + (float) i / (times.size() - 1) * chartW;
            g2d.drawString(times.get(i), x - 15, getHeight() - 10);
        }
    }

    private void drawGrid(Graphics2D g2d, int chartW, int chartH, double max, double range) {
        g2d.setFont(new Font("SansSerif", Font.PLAIN, 11));

       for (int i = 0; i <= GRID_LINES; i++) {
            double fraction = (double) i / GRID_LINES;
            int y = PAD_TOP + (int) (chartH * fraction);
            double price = max - fraction * range;

            g2d.setColor(new Color(255, 255, 255, 25));
           g2d.drawLine(PAD_LEFT, y, PAD_LEFT + chartW, y);

            g2d.setColor(new Color(180, 180, 180));
            g2d.drawString(String.format("%.2f", price), 5, y + 4);
       }
    }


}
