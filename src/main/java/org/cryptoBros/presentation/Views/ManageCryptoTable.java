package org.cryptoBros.presentation.Views;

import java.awt.*;

/**
 * Table component for managing cryptocurrencies with edit and delete buttons.
 */
public class ManageCryptoTable extends AbstractTable {
    private static final String[] COLUMNS = {"Cryptocurrency", "", ""};
    private static final int[] WIDTHS = {250, 100, 100};

    /**
     * Creates a new ManageCryptoTable.
     */
    public ManageCryptoTable () {
        super(COLUMNS, WIDTHS);
    }

    @Override
    protected boolean isColumnEditable(int col) {
        return col == 1 || col == 2;
    }

    @Override
    protected void configureColumns() {
        table.getColumnModel().getColumn(0).setCellRenderer(
                new javax.swing.table.DefaultTableCellRenderer() {
                    @Override
                    public java.awt.Component getTableCellRendererComponent(
                            javax.swing.JTable t, Object val, boolean sel, boolean foc, int row, int col) {
                        super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                        setOpaque(true);
                        setBackground(row % 2 == 0 ? new Color(180, 198, 242) : new Color(160, 180, 230));
                        setForeground(Color.BLACK);
                        setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 14));
                        setHorizontalAlignment(LEFT);
                        setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 8, 0, 0));
                        return this;
                    }
                }
        );

        setButtonColumn(1, "Edit", new Color(99, 125, 217),
                row ->{});

        setButtonColumn(2, "Delete", new Color(200, 50, 50),
                row -> {});
    }

	/**
	 * Sets the callback for edit button clicks.
	 *
	 * @param callback the callback to invoke when edit is clicked
	 */
	public void setEditCallback(ButtonRowCallback callback) {
		setButtonColumn(1, "Edit", new Color(125, 200, 50), callback);
	}

    /**
     * Sets the callback for delete button clicks.
     *
     * @param callback the callback to invoke when delete is clicked
     */
    public void setDeleteCallback(ButtonRowCallback callback) {
        setButtonColumn(2, "Delete", new Color(200, 50, 50), callback);
    }
}
