package org.cryptoBros.presentation.Views;

import org.cryptoBros.presentation.Enum.ButtonEnumeration;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * Displays the user's portfolio and balance management interface.
 */
public class PortfolioView extends Pages{

    /** Creates a new PortfolioView. */
    public PortfolioView() {}

    /** The portfolio data table. */
    private PortfolioTable portfolioTable;
    /** The add balance input field. */
    private JTextField jTFAddBalance;
    /** The balance display label. */
    private JLabel jLBalance;
    /** The profit display label. */
    private JLabel jLProfit;
    /** The confirm balance button. */
    private JButton confirmButton;


    /**
     * Configures the view layout by setting up the header and core panels.
     */
    @Override
    protected void configureView() {
        JPanel jpHeader = setHeader();
        JPanel jpCore = setCore();

        getContent().add(jpHeader,  BorderLayout.NORTH);
        getContent().add(jpCore,  BorderLayout.CENTER);
    }

    /**
     * Registers action listeners for the view's interactive components.
     *
     * @param listener the action listener to attach to buttons
     */
    @Override
    public void setActions(ActionListener listener) {
        addHeaderActions(listener);

        confirmButton.setActionCommand(ButtonEnumeration.CONFIRM.name());
        confirmButton.addActionListener(listener);
    }

    private JPanel setCore() {
        JPanel core = new JPanel(new BorderLayout());
        core.setBackground(new Color(239, 247, 255));
        core.add(buildPortfolioSection(), BorderLayout.CENTER);
        core.add(buildBalanceSection(), BorderLayout.SOUTH);
        return core;
    }


    /**
     * Builds and returns the portfolio section panel, including the title,
     * estimated profit label, and the portfolio table.
     *
     * @return the portfolio section JPanel
     */
    private JPanel buildPortfolioSection() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(new Color(239, 247, 255));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 10, 50));

        JLabel title = new JLabel("Portfolio Management", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 30));
        title.setForeground(Color.BLACK);

        jLProfit = new JLabel("Estimated Profit: +0.00 €", SwingConstants.CENTER);
        jLProfit.setFont(new Font("SansSerif", Font.BOLD, 18));
        jLProfit.setForeground(Color.BLACK);

        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(new Color(239, 247, 255));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        jLProfit.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(title);
        headerPanel.add(Box.createVerticalStrut(6));
        headerPanel.add(jLProfit);

        portfolioTable = new PortfolioTable();

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(portfolioTable, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Updates the displayed total profit.
     *
     * @param totalProfit the total profit value
     */
    public void updateProfit(double totalProfit) {
        String sign = totalProfit >= 0 ? "+" : "";
        jLProfit.setText(String.format("Estimated Profit: %s%.2f €", sign, totalProfit));
        jLProfit.setForeground(totalProfit >= 0 ? new Color(0, 150, 80) : new Color(200, 50, 50));
    }

    /**
     * Builds and returns the balance management section panel, including
     * the balance title, add balance input field, and confirm button.
     *
     * @return the balance section JPanel
     */
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

        confirmButton = new JButton("Confirm");
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

    /**
     * Sets the callback for sell button clicks.
     *
     * @param callback the callback to invoke when a sell button is clicked
     */
    public void setSellCallback(AbstractTable.ButtonRowCallback callback) {
        portfolioTable.setSellCallback(callback);
    }

    /**
     * Returns the crypto symbol at the given row.
     *
     * @param row the row index
     * @return the crypto symbol
     */
    public String getSymbolAt(int row) {
        return portfolioTable.getSymbolAt(row);
    }

    /**
     * Returns the number of units at the given row.
     *
     * @param row the row index
     * @return the number of units
     */
    public double getUnitsAt(int row) {
        return portfolioTable.getUnitsAt(row);
    }

    /**
     * Populates the portfolio table with position data.
     *
     * @param data the portfolio position rows
     */
    public void setPortfolioData(Object[][] data) {
        portfolioTable.clearRows();
        for (Object[] row : data) portfolioTable.addRow(row);
    }

    /**
     * Returns the amount entered for balance deposit.
     *
     * @return the deposit amount, or 0.0 if the input is invalid
     */
    public double getAddBalanceAmount() {
        try {
            String text = jTFAddBalance.getText().replace("€", "").replace(",", ".").trim();
            return Double.parseDouble(text);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    /**
     * Creates and returns a styled text field with placeholder behaviour.
     * The placeholder text is shown in grey and disappears on focus.
     *
     * @param placeholder the placeholder text to display when the field is empty
     * @return the configured JTextField
     */
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
