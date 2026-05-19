package org.cryptoBros.presentation.Views;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class PortfolioTable extends AbstractTable {

    private static final String[] COLUMNS = {"Cryptocurrency", "Units", "Buy Price", "Balance", ""};
    private static final int[] WIDTHS = {160, 60, 130, 150, 80};



    public PortfolioTable() {
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

        // TODO: Sell button action
        setButtonColumn(4, "Sell", new Color(100, 125, 220),
                row -> System.out.println("Sell clicked on row: " + row));
    }
}
