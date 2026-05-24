package org.cryptoBros.presentation.Views;

import org.cryptoBros.presentation.ListenersPersistence.CryptoSelectedListener;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class DynamicTable extends AbstractTable {

    private static final String[] COLUMNS = {"Cryptocurrency", "Price (€)", "Change (€)", "% Change"};
    private static final int[] WIDTHS = {160, 170, 150, 130};

    private Map<String, Integer> cryptos;
    private CryptoSelectedListener onRowClick;

	public DynamicTable() {
        super(COLUMNS, WIDTHS);
        cryptos = new HashMap<>();
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0 && onRowClick != null) {
                    for (Map.Entry<String, Integer> entry : cryptos.entrySet()) {
                        if (entry.getValue() == row) {
                            onRowClick.cryptoSelected(entry.getKey());
                            break;
                        }
                    }
                }
            }
        });
	}

    public void update(String symbol, String name, double price, double change, double percentage) {
        Integer row = cryptos.get(symbol);

        if (row != null && row < model.getRowCount()) {
            String[] formatted = formatRow(name, price, change, percentage);
            for (int col = 0; col < formatted.length; col++)
                model.setValueAt(formatted[col], row, col);
        } else {
            model.addRow(formatRow(name, price, change, percentage));
            cryptos.put(symbol, model.getRowCount() - 1);
        }
    }

    private String[] formatRow(String name, double price, double change, double percentage) {
        return new String[]{
                name,
                String.format("€ %.2f", price),
                String.format("%s€ %.2f", change >= 0 ? "+" : "", change),
                String.format("%s%.4f%%", percentage >= 0 ? "+" : "", percentage)
        };
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
                    case 0 -> { setForeground(new Color(100, 100, 100)); setFont(new Font("SansSerif", Font.PLAIN, 13)); setHorizontalAlignment(CENTER); }
                    case 1 -> { setForeground(Color.BLACK); setFont(new Font("SansSerif", Font.BOLD, 14)); setHorizontalAlignment(LEFT); }
                    case 2 -> { setForeground(Color.BLACK); setFont(new Font("SansSerif", Font.PLAIN, 14)); setHorizontalAlignment(RIGHT); }
                    case 3 -> {
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

    public void clearRows() {
        super.clearRows();
        cryptos.clear();
    }

    public void setOnRowClick(CryptoSelectedListener listener) {
        onRowClick = listener;
    }
}