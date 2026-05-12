package org.cryptoBros.presentation.Views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ManageCryptoView extends Pages {

    private final List<String> cryptoNames = new ArrayList<>();

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
        JPanel jpPanel = new JPanel(new BorderLayout(0, 10));
        jpPanel.setBackground(new Color(239, 247, 255));
        jpPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 10, 50));

        JLabel jlTitle = new JLabel("Manage Cryptocurrencies", SwingConstants.CENTER);
        jlTitle.setFont(new Font("SansSerif", Font.BOLD, 30));
        jlTitle.setForeground(Color.BLACK);

        jpPanel.add(jlTitle, BorderLayout.NORTH);
        jpPanel.add(buildTable(), BorderLayout.CENTER);

        return jpPanel;
    }

    private JPanel buildTable () {
        String[] columns = {"Cryptocurrency", "Edit", "Delete"};

        Object[][] data = new Object[columns.length][3];

        for (int i = 0; i < c; i++) {}


    }

    private JPanel buildAddSection() {
        JPanel addSection = new JPanel(new GridBagLayout());
    }

    public void setCryptoDate (List<String> names) {
        cryptoNames.clear();
        cryptoNames.addAll(names);
    }
}
