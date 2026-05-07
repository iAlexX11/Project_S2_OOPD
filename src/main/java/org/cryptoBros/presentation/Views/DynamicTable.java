package org.cryptoBros.presentation.Views;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.text.DecimalFormat;

public class DynamicTable extends JPanel {

	private static final String[] columns = { "#", "Cryptocurrency", "Price (€)", "Change (€)", "% Change"};

	private DefaultTableModel model;

	public DynamicTable() {
		setBackground(Color.WHITE);
		setLayout(new BorderLayout());

		model = new DefaultTableModel(null, columns) {
			@Override public boolean isCellEditable(int r, int c) { return false; }
		};

		JTable table = new JTable(model);
		table.setBackground(Color.WHITE);
		table.setForeground(Color.BLACK);
		table.setFont(new Font("SansSerif", Font.PLAIN, 14));
		table.setRowHeight(38);
		table.setShowGrid(false);
		table.setIntercellSpacing(new Dimension(0, 0));
		table.setSelectionBackground(new Color(200, 220, 255));
		table.setSelectionForeground(Color.BLACK);
		table.setFocusable(false);

		int[] widths = {40, 160, 170, 150, 130};
		for (int i = 0; i < widths.length; i++)
			table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);

		JTableHeader th = table.getTableHeader();
		th.setBackground(new Color(230, 230, 230));
		th.setForeground(new Color(100, 100, 100));
		th.setFont(new Font("SansSerif", Font.BOLD, 12));
		th.setReorderingAllowed(false);
		th.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 200, 200)));
		th.setPreferredSize(new Dimension(0, 40));
		((DefaultTableCellRenderer) th.getDefaultRenderer())
				.setHorizontalAlignment(SwingConstants.CENTER);

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
					case 1 -> { setForeground(Color.BLACK); setFont(new Font("SansSerif", Font.BOLD,14)); setHorizontalAlignment(LEFT);   }
					case 2 -> { setForeground(Color.BLACK); setFont(new Font("SansSerif", Font.PLAIN, 14)); setHorizontalAlignment(RIGHT);  }
					case 3, 4 -> {
						setForeground(s.startsWith("+") ? new Color(0, 150, 80) : new Color(200, 50, 50));
						setFont(new Font("SansSerif", Font.BOLD, 14));
						setHorizontalAlignment(RIGHT);
					}
				}
				return this;
			}
		};

		for (int c = 0; c < columns.length; c++)
			table.getColumnModel().getColumn(c).setCellRenderer(cellRenderer);

		JScrollPane sp = new JScrollPane(table);
		sp.setBorder(BorderFactory.createEmptyBorder());
		sp.getViewport().setBackground(Color.WHITE);
		sp.setBackground(Color.WHITE);
		add(sp, BorderLayout.CENTER);
	}

	public void addRow(Object[] row) { model.addRow(row); }
	public void clearRows() { model.setRowCount(0); }
	public void setValueAt(Object value, int row, int col) { model.setValueAt(value, row, col); }
}