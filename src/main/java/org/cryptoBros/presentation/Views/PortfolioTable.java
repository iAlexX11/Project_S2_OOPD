package org.cryptoBros.presentation.Views;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * Table component for displaying portfolio positions with a sell button.
 */
public class PortfolioTable extends AbstractTable {

    private static final String[] COLUMNS = {"Cryptocurrency", "Units", "Buy Price", "Balance", ""};
    private static final int[] WIDTHS = {160, 60, 130, 150, 80};



    /** Creates a new PortfolioTable. */
    public PortfolioTable() {
        super(COLUMNS, WIDTHS);
    }

    /**
     * Sets the callback for sell button clicks.
     *
     * @param callback the callback to invoke when a sell button is clicked
     */
    public void setSellCallback(ButtonRowCallback callback) {
        setButtonColumn(4, "Sell", new Color(100, 125, 220), callback);
    }

    /**
     * Returns the crypto symbol at the given row.
     *
     * @param row the row index
     * @return the crypto symbol
     */
    public String getSymbolAt(int row) {
        return (String) model.getValueAt(row, 0);
    }

    /**
     * Returns the number of units at the given row.
     *
     * @param row the row index
     * @return the number of units
     */
    public double getUnitsAt(int row) {
        Object val = model.getValueAt(row, 1);
        if (val instanceof Double) return (Double) val;
        return Double.parseDouble(val.toString());
    }

    @Override
    protected boolean isColumnEditable(int col) {
        return col == 4;
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
                        setForeground(Color.BLACK);
                        setFont(new Font("SansSerif", Font.BOLD, 14));
                        setHorizontalAlignment(LEFT);  }
                    case 1, 2 -> {
                        setForeground(Color.BLACK);
                        setFont(new Font("SansSerif", Font.PLAIN, 14));
                        setHorizontalAlignment(RIGHT); }
                    case 3 -> {
                        setForeground(s.startsWith("+") ? new Color(0, 150, 80) : new Color(200, 50, 50));
                        setFont(new Font("SansSerif", Font.BOLD, 14));
                        setHorizontalAlignment(RIGHT);
                    }
                }
                return this;
            }
        };

        for (int i = 0; i < COLUMNS.length - 1; i++)
            table.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);

        setButtonColumn(4, "Sell", new Color(100, 125, 220),
                row -> {});
    }
}
