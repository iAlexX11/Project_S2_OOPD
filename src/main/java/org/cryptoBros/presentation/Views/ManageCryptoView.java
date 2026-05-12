package org.cryptoBros.presentation.Views;

import org.cryptoBros.presentation.ButtonEnumeration;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ManageCryptoView extends Pages {

    private ManageCryptoTable manageCryptoTable;

    @Override
    protected void configureView() {
        // TODO: Make sure header is from admin
        JPanel header = setHeader();
        JPanel core = setCore();

        getContent().add(header, BorderLayout.NORTH);
        getContent().add(core, BorderLayout.CENTER);
    }

    @Override
    public void setActions(ActionListener listener) {
        // TODO: Implement the new header for the Admin
        addHeaderActions(listener);
    }

    private JPanel setCore() {
        JPanel core = new JPanel(new BorderLayout());
        core.setBackground(new Color(239, 247, 255));
        core.add(buildTableSection(), BorderLayout.CENTER);
        core.add(buildAddSection(), BorderLayout.SOUTH);
        return core;
    }

    private JPanel buildTableSection() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(new Color(239, 247, 255));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 10, 50));

        JLabel jlTitle = new JLabel("Manage Cryptocurrencies", SwingConstants.CENTER);
        jlTitle.setFont(new Font("SansSerif", Font.BOLD, 30));
        jlTitle.setForeground(Color.BLACK);

        manageCryptoTable = new ManageCryptoTable();

        panel.add(jlTitle, BorderLayout.NORTH);
        panel.add(manageCryptoTable, BorderLayout.CENTER);

        return panel;
    }

    private JPanel buildAddSection() {
        JPanel addSection = new JPanel(new FlowLayout(FlowLayout.CENTER));
        addSection.setBackground(new Color(239, 247, 255));
        addSection.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(185, 200, 245));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(150, 170, 230), 1, true),
                BorderFactory.createEmptyBorder(16, 40, 16, 40)
        ));

        JLabel addTitle = new JLabel("Add New Cryptocurrency", SwingConstants.CENTER);
        addTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        addTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel addDescription = new JLabel("Select the JSON file containing the currency information.");
        addDescription.setFont(new Font("SansSerif", Font.PLAIN, 13));
        addDescription.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton addButton = new JButton("ADD");
        addButton.setActionCommand(ButtonEnumeration.ADD_CRYPTO.name());
        addButton.setFont(new Font("SansSerif", Font.BOLD, 15));
        addButton.setBackground(new Color(99, 125, 217));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        addButton.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));
        addButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        card.add(addTitle);
        card.add(Box.createVerticalStrut(6));
        card.add(addDescription);
        card.add(Box.createVerticalStrut(12));
        card.add(addButton);

        addSection.add(card);
        return addSection;
    }

    public void setCryptoData (Object[][] data) {
        manageCryptoTable.clearRows();
        for (Object[] row : data) manageCryptoTable.addRow(row);
    }

    public JFileChooser createFileChooser() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Cryptocurrency JSON file");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("JSON files", "json"));
        return fileChooser;
    }
}
