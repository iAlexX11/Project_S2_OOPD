package org.cryptoBros.presentation.Views;

import org.cryptoBros.presentation.ListenersPersistence.CryptoSelectedListener;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Live-updating table component for displaying cryptocurrency market data.
 */
public class DynamicTable extends AbstractTable {

	private static final String[] COLUMNS = {"Cryptocurrency", "Price (€)", "Change (€)", "% Change"};
	private static final int[] WIDTHS = {160, 170, 150, 130};

	private static final Color GREEN = new Color(0, 150, 80);
	private static final Color RED   = new Color(200, 50, 50);
	private static final Color MUTED = new Color(100, 100, 100);

	/** Maps crypto symbols to their table row indices. */
	private Map<String, Integer> cryptos;
	/** The listener for row selection events. */
	private CryptoSelectedListener onRowClick;

	/** Creates a new DynamicTable with click support. */
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

	@Override
	protected Color getCellForeground(int row, int col, String value) {
		if (col == 0) return MUTED;
		if (col == 3) return value.startsWith("+") ? GREEN : RED;
		return Color.BLACK;
	}

	@Override
	protected Font getCellFont(int row, int col) {
		boolean bold = col == 1 || col == 3;
		int size = col == 0 ? 13 : 14;
		return new Font("SansSerif", bold ? Font.BOLD : Font.PLAIN, size);
	}

	@Override
	protected int getCellAlignment(int row, int col) {
		return switch (col) {
			case 0  -> SwingConstants.CENTER;
			case 1  -> SwingConstants.LEFT;
			default -> SwingConstants.RIGHT;
		};
	}

	/** All columns in this table are read-only. */
	@Override
	protected boolean isColumnEditable(int col) {
		return false;
	}

	@Override
	protected void configureColumns() {
		DefaultTableCellRenderer r = buildBaseRenderer();
		for (int c = 0; c < COLUMNS.length; c++)
			table.getColumnModel().getColumn(c).setCellRenderer(r);
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