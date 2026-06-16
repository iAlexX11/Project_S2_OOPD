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

	private static final Color SELL_BUTTON_COLOR = new Color(100, 125, 220);
	private static final Color GREEN = new Color(0, 150, 80);
	private static final Color RED   = new Color(200, 50, 50);

	/** Creates a new PortfolioTable. */
	public PortfolioTable() {
		super(COLUMNS, WIDTHS);
	}

	/**
	 * Sets the callback for sale button clicks.
	 *
	 * @param callback the callback to invoke when a sell button is clicked
	 */
	public void setSellCallback(ButtonRowCallback callback) {
		setButtonColumn(4, "Sell", SELL_BUTTON_COLOR, callback);
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
	protected Color getCellForeground(int row, int col, String value) {
		return col == 3 ? (value.startsWith("+") ? GREEN : RED) : Color.BLACK;
	}

	@Override
	protected Font getCellFont(int row, int col) {
		return new Font("SansSerif", col == 1 || col == 2 ? Font.PLAIN : Font.BOLD, 14);
	}

	@Override
	protected int getCellAlignment(int row, int col) {
		return col == 0 ? SwingConstants.LEFT : SwingConstants.RIGHT;
	}

	/** Only the Sell button column (4) is editable. */
	@Override
	protected boolean isColumnEditable(int col) {
		return col == 4;
	}

	/** Applies the shared renderer to data columns; Sell button registered via {@link #setSellCallback}. */
	@Override
	protected void configureColumns() {
		DefaultTableCellRenderer r = buildBaseRenderer();
		for (int i = 0; i < COLUMNS.length - 1; i++)
			table.getColumnModel().getColumn(i).setCellRenderer(r);
	}
}