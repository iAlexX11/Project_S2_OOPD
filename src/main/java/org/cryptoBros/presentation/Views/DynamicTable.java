package org.cryptoBros.presentation.Views;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class DynamicTable extends AbstractTable {

	private static final String[] COLUMNS = { "#", "Cryptocurrency", "Price (€)", "Change (€)", "% Change"};
    private static final int[] WIDTHS = {40, 160, 170, 150, 130};

    private final Map<String, Integer> cryptos = new HashMap<>();

	public DynamicTable() {
        super(COLUMNS, WIDTHS);
    }

    @Override
    protected boolean isColumnEditable(int col) {
        return false;
    }

    @Override
    protected void configureColumns() {
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable t, Object val, boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                setOpaque(true);
                setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 200, 200)));

                if (!sel)
                    setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 245, 245));

                String s = val == null ? "" : val.toString();

                switch (col) {
                    case 0 -> {
                        setForeground(new Color(100, 100, 100));
                        setFont(new Font("SansSerif", Font.PLAIN, 13));
                        setHorizontalAlignment(CENTER); }
                    case 1 -> {
                        setForeground(Color.BLACK);
                        setFont(new Font("SansSerif", Font.BOLD, 14));
                        setHorizontalAlignment(LEFT);  }
                    case 2 -> {
                        setForeground(Color.BLACK);
                        setFont(new Font("SansSerif", Font.PLAIN, 14));
                        setHorizontalAlignment(RIGHT); }
                    case 3, 4 -> {
                        setForeground(s.startsWith("+") ? new Color(0, 150, 80) : new Color(200, 50, 50));
                        setFont(new Font("SansSerif", Font.BOLD, 14));
                        setHorizontalAlignment(RIGHT);
                    }
                }
                return this;
            }
        };

        for (int c = 0; c < COLUMNS.length; c++)
            table.getColumnModel().getColumn(c).setCellRenderer(cellRenderer);
    }

    public void update(String name, double price, double change, double percentage) {
        if (cryptos.containsKey(name)) {
            int row = cryptos.get(name);
            model.setValueAt(name,                                           row, 1);
            model.setValueAt("€ " + price,                                  row, 2);
            model.setValueAt((change >= 0 ? "+" : "") + "€ " + change,      row, 3);
            model.setValueAt((percentage >= 0 ? "+" : "") + percentage + "%", row, 4);
        } else {
            addCryptoRow(name, price, change, percentage);
        }
    }

    private void addCryptoRow(String name, double price, double change, double percentage) {
        int rowNum = model.getRowCount() + 1;
        cryptos.put(name, rowNum);
        model.addRow(new Object[]{
                rowNum,
                name,
                "€ " + price,
                (change >= 0 ? "+" : "-") + "€ " + change,
                (percentage >= 0 ? "+" : "-") + percentage + "%"
        });
    }
}