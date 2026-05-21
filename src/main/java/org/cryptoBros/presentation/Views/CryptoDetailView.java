package org.cryptoBros.presentation.Views;

import org.cryptoBros.presentation.Enum.ButtonEnumeration;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.List;

public class CryptoDetailView extends Pages {

    private JButton jBConfirm;
    private JLabel jLCryptoName;
    private JLabel jLCurrentPrice;
    private JLabel jLOwnedCrypto;
    private JTextField jTFQuantity;
    private PriceChart priceChart;

    @Override
    protected void configureView() {
        getContent().add(setCore(), BorderLayout.CENTER);
    }

    @Override
    public void setActions(ActionListener listener) {
        getContent().add(setHeader(), BorderLayout.NORTH);
        addHeaderActions(listener);

        jBConfirm.setActionCommand(ButtonEnumeration.CONFIRM_PURCHASE.name());
        jBConfirm.addActionListener(listener);

        getContent().revalidate();
    }


    private JPanel setCore() {
        JPanel core = new JPanel(new BorderLayout(0, 15));
        core.setBackground(new Color(239, 247, 255));
        core.setOpaque(true);
        core.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        core.add(topPanel(), BorderLayout.NORTH);
        core.add(chartWrapper(), BorderLayout.CENTER);
        core.add(buyPanel(), BorderLayout.SOUTH);

        return core;
    }

    private JPanel chartWrapper() {
        priceChart = new PriceChart();

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(new Color(239, 247, 255));
        wrapper.add(priceChart, BorderLayout.CENTER);
        wrapper.add(priceChart);

        return wrapper;
    }

    private JPanel topPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setOpaque(false);

        jLCryptoName = new JLabel("**<Crypto Name>**", SwingConstants.CENTER);
        jLCryptoName.setFont(new Font("Apple Casual", Font.BOLD, 32));
        jLCryptoName.setForeground(Color.BLACK);

        JPanel jPCryptoInfo = new JPanel(new BorderLayout());
        jPCryptoInfo.setOpaque(false);

        jLCurrentPrice = new JLabel("Current price : 00.000,00 €");
        jLCurrentPrice.setFont(new Font("Apple Casual", Font.BOLD, 16));
        jLCurrentPrice.setForeground(Color.BLACK);

        jLOwnedCrypto = new JLabel("Current owned crypto : 0 <CRYPTO>", SwingConstants.RIGHT);
        jLOwnedCrypto.setFont(new Font("Apple Casual", Font.BOLD, 16));
        jLOwnedCrypto.setForeground(Color.BLACK);

        jPCryptoInfo.add(Box.createVerticalStrut(8));
        jPCryptoInfo.add(jLCurrentPrice, BorderLayout.WEST);
        jPCryptoInfo.add(jLOwnedCrypto, BorderLayout.EAST);

        panel.add(jLCryptoName, BorderLayout.NORTH);
        panel.add(jPCryptoInfo, BorderLayout.CENTER);


        return panel;
    }

    private JPanel buyPanel() {
        JPanel jPBlock = new JPanel(new FlowLayout(FlowLayout.CENTER));
        jPBlock.setOpaque(false);

        JPanel buyBox = new JPanel();
        buyBox.setLayout(new BoxLayout(buyBox, BoxLayout.Y_AXIS));
        buyBox.setBackground(new Color(190, 210, 255));
        buyBox.setBorder(new EmptyBorder(8, 20, 8, 20));

        JLabel buyTitle = new JLabel("Buy Crypto", SwingConstants.CENTER);
        buyTitle.setFont(new Font("Apple Casual", Font.BOLD, 14));
        buyTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel quantityRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 3));
        quantityRow.setOpaque(false);

        JLabel quantityLabel = new JLabel("Quantity:");
        quantityLabel.setFont(new Font("Apple Casual", Font.BOLD, 13));

        jTFQuantity = new JTextField();
        jTFQuantity.setPreferredSize(new Dimension(80, 25));
        jTFQuantity.addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {
                if (jTFQuantity.getText().equals("0.00")) {
                    jTFQuantity.setText("");
                    jTFQuantity.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (jTFQuantity.getText().isEmpty()) {
                    jTFQuantity.setForeground(Color.GRAY);
                    jTFQuantity.setText("0.00");
                }
            }
        });
        jTFQuantity.setFont(new Font("SansSerif", Font.PLAIN, 14));
        jTFQuantity.setHorizontalAlignment(JTextField.LEFT);

        quantityRow.add(quantityLabel);
        quantityRow.add(jTFQuantity);

        jBConfirm = new JButton("Confirm");
        jBConfirm.setAlignmentX(Component.CENTER_ALIGNMENT);
        jBConfirm.setBackground(new Color(83, 136, 252));
        jBConfirm.setForeground(Color.WHITE);
        jBConfirm.setFont(new Font("Apple Casual", Font.BOLD, 13));
        jBConfirm.setFocusPainted(false);
        jBConfirm.setBorderPainted(false);
        jBConfirm.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jBConfirm.setPreferredSize(new Dimension(100, 28));
        jBConfirm.setMaximumSize(new Dimension(100, 28));

        buyBox.add(buyTitle);
        buyBox.add(Box.createVerticalStrut(5));
        buyBox.add(quantityRow);
        buyBox.add(Box.createVerticalStrut(5));
        buyBox.add(jBConfirm);

        jPBlock.add(buyBox);
        return jPBlock;
    }

    public void setCryptoName(String name) {
        jLCryptoName.setText(name);
    }

    public void setCurrentPrice(double price) {
        jLCurrentPrice.setText("Current price : " + String.format("%,.2f", price) + " €");
    }

    public void setOwnedCrypto(double units, String symbol) {
        jLOwnedCrypto.setText("Current owned crypto : " + String.format("%.0f", units) + " " + symbol);
    }

    public void loadAllHistory(List<Double> prices, List<String> dates) {
        priceChart.loadData(prices, dates);
    }
}
