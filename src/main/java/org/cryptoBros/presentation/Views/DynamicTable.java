package org.cryptoBros.presentation.Views;

import org.cryptoBros.presentation.ListenersPersistence.CryptoSelectedListener;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Live-updating table component for displaying cryptocurrency market data.
 */
public class DynamicTable extends AbstractTable {

    private static final String[] COLUMNS = {"Cryptocurrency", "Price (€)", "Change (€)", "% Change"};
    private static final int[] WIDTHS = {160, 170, 150, 130};

    /** Maps crypto symbols to their table row indices. */
    private Map<String, Integer> cryptos;
    /** The listener for row selection events. */
    private CryptoSelectedListener onRowClick;

	/**
	 * Creates a new DynamicTable with click support.
	 */
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

    /**
     * Updates or adds a cryptocurrency row.
     *
     * @param symbol     the crypto symbol identifier
     * @param name       the display name
     * @param price      the current price in euros
     * @param change     the price change in euros
     * @param percentage the percentage change
     */
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

    /**
     * Returns whether the specified column is editable. All columns in this table are read-only.
     *
     * @param col the column index
     * @return false for all columns
     */
    @Override
    protected boolean isColumnEditable(int col) {
        return false;
    }

    /**
     * Configures column renderers with alternating row colors and per-column formatting for name, price, change, and percentage.
     */
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

    /**
     * Removes all rows from the table and clears the symbol-to-row mapping.
     */
    @Override
    public void clearRows() {
        super.clearRows();
        cryptos.clear();
    }

    /**
     * Registers a listener for row selection events.
     *
     * @param listener the listener to notify on row click
     */
    public void setOnRowClick(CryptoSelectedListener listener) {
        onRowClick = listener;
    }
}