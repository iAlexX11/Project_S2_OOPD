package org.cryptoBros.presentation.Views;

import org.cryptoBros.presentation.Enum.ButtonEnumeration;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class PortfolioView extends Pages{

    private PortfolioTable portfolioTable;
    private JTextField jTFAddBalance;
    private JLabel jLBalance;
    private JLabel jLProfit;

    @Override
    protected void configureView() {
        JPanel jpHeader = setHeader();
        JPanel jpCore = setCore();

        getContent().add(jpHeader,  BorderLayout.NORTH);
        getContent().add(jpCore,  BorderLayout.CENTER);
    }

    @Override
    public void setActions(ActionListener listener) {
        addHeaderActions(listener);
    }

    private JPanel setCore() {
        JPanel core = new JPanel(new BorderLayout());
        core.setBackground(new Color(239, 247, 255));
        core.add(buildPortfolioSection(), BorderLayout.CENTER);
        core.add(buildBalanceSection(), BorderLayout.SOUTH);
        return core;
    }

    private JPanel buildPortfolioSection() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(new Color(239, 247, 255));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 10, 50));

        JLabel title = new JLabel("Portfolio Management", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 30));
        title.setForeground(Color.BLACK);

        portfolioTable = new PortfolioTable();

        panel.add(title, BorderLayout.NORTH);
        panel.add(portfolioTable, BorderLayout.CENTER);

        return panel;
    }

    private JPanel buildBalanceSection() {
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        wrapper.setBackground(new Color(239, 247, 255));
        wrapper.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(209, 220, 255));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(150, 170, 230), 1, true),
                BorderFactory.createEmptyBorder(16, 30, 16, 30)
        ));

        JLabel balanceTitle = new JLabel("Balance", SwingConstants.CENTER);
        balanceTitle.setFont(new Font("SansSerif", Font.BOLD, 26));
        balanceTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel addBalanceCard = new JPanel();
        addBalanceCard.setLayout(new BoxLayout(addBalanceCard, BoxLayout.Y_AXIS));
        addBalanceCard.setBackground(new Color(185, 200, 245));
        addBalanceCard.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        JLabel addBalanceLabel = new JLabel("Add balance", SwingConstants.CENTER);
        addBalanceLabel.setFont(new Font("SansSerif", Font.BOLD, 15));
        addBalanceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel inputRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 4));
        inputRow.setOpaque(false);
        JLabel quantityLabel = new JLabel("Quantity:");
        quantityLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        jTFAddBalance = setTextField("00.00 $");
        jTFAddBalance.setFont(new Font("SansSerif", Font.PLAIN, 13));
        inputRow.add(quantityLabel);
        inputRow.add(jTFAddBalance);

        JButton confirmButton = new JButton("Confirm");
        confirmButton.setActionCommand(ButtonEnumeration.CONFIRM_BALANCE.name());
        confirmButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        confirmButton.setBackground(new Color(99, 125, 217));
        confirmButton.setForeground(Color.WHITE);
        confirmButton.setFocusPainted(false);
        confirmButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        confirmButton.setBorder(BorderFactory.createEmptyBorder(8, 30, 8, 30));
        confirmButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        addBalanceCard.add(addBalanceLabel);
        addBalanceCard.add(inputRow);
        addBalanceCard.add(Box.createVerticalStrut(6));
        addBalanceCard.add(confirmButton);

        card.add(balanceTitle);
        card.add(Box.createVerticalStrut(4));
        card.add(Box.createVerticalStrut(10));
        card.add(addBalanceCard);

        wrapper.add(card);
        return wrapper;
    }

    public void setPortfolioData(Object[][] data) {
        portfolioTable.clearRows();
        for (Object[] row : data) portfolioTable.addRow(row);
    }

    public double getAddBalanceAmount() {
        try {
            String text = jTFAddBalance.getText().replace("€", "").replace(",", ".").trim();
            return Double.parseDouble(text);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private JTextField setTextField(String placeholder) {
        JTextField jTextField = new JTextField();
        jTextField.setPreferredSize(new Dimension(100, 25));
        jTextField.setMaximumSize(new Dimension(100, 25));
        jTextField.setBorder(BorderFactory.createLineBorder(new Color(200, 210, 220)));
        jTextField.setText(placeholder);
        jTextField.setForeground(Color.GRAY);
        jTextField.addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {
                if (jTextField.getText().equals(placeholder)) {
                    jTextField.setText("");
                    jTextField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (jTextField.getText().isEmpty()) {
                    jTextField.setForeground(Color.GRAY);
                    jTextField.setText(placeholder);
                }
            }
        });

        return jTextField;
    }
}
