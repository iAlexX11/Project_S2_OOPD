package org.cryptoBros.presentation.Views;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.awt.*;

/**
 * Reusable Swing table panel with configurable columns, button columns, and row operations.
 *
 * Subclasses define which columns are editable and how they are rendered
 * by implementing {@link #isColumnEditable(int)} and {@link #configureColumns()}.
 */
public abstract class AbstractTable extends JPanel {
    /** Backing table model that stores column definitions and row data. */
    protected DefaultTableModel model;
    /** Visual table component displayed inside this panel. */
    protected JTable table;

    /**
     * Creates the table panel with the given column names and preferred widths.
     *
     * @param columns      header labels for each column
     * @param columnWidths preferred pixel width for each column
     */
    public AbstractTable(String[] columns, int[] columnWidths) {
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        model = new DefaultTableModel(null, columns) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return isColumnEditable(col);
            }
        };

        table = new JTable(model);
        configureTable(columnWidths);
        configureHeader();
        configureColumns();
        setScrollPane();
    }

    /**
     * Returns whether the column at the given index should be editable.
     *
     * @param col zero-based column index
     * @return {@code true} if cells in this column may be edited
     */
    protected abstract boolean isColumnEditable(int col);

    /**
     * Configures column renderers and editors after the table has been created.
     */
    protected abstract void configureColumns();

    private void configureTable(int[] columnWidths) {
        table.setBackground(Color.WHITE);
        table.setForeground(Color.BLACK);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.setRowHeight(38);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(200, 220, 255));
        table.setSelectionForeground(Color.BLACK);
        table.setFocusable(false);

        for (int i = 0; i < columnWidths.length; i++)
            table.getColumnModel().getColumn(i).setPreferredWidth(columnWidths[i]);
    }

    private void configureHeader() {
        JTableHeader th = table.getTableHeader();
        th.setBackground(new Color(230, 230, 230));
        th.setForeground(new Color(100, 100, 100));
        th.setFont(new Font("SansSerif", Font.BOLD, 12));
        th.setReorderingAllowed(false);
        th.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 200, 200)));
        th.setPreferredSize(new Dimension(0, 40));
        ((DefaultTableCellRenderer) th.getDefaultRenderer())
                .setHorizontalAlignment(SwingConstants.CENTER);
    }

    private void setScrollPane() {
        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.getViewport().setBackground(Color.WHITE);
        sp.setBackground(Color.WHITE);
        add(sp, BorderLayout.CENTER);
    }

    /**
     * Creates a styled button for use inside table cells.
     *
     * @param label      text displayed on the button
     * @param background button background colour
     * @return a pre-styled {@link JButton}
     */
    protected JButton makeButton(String label, Color background) {
        JButton button = new JButton(label);
        button.setBackground(background);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("SansSerif", Font.BOLD, 13));
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createEmptyBorder(4, 16, 4, 16));
        return button;
    }

    /**
     * Turns a column into a clickable button column.
     *
     * Each cell in the column renders a styled button. When the user clicks
     * it, the provided callback receives the row index of the click.
     *
     * @param colIndex zero-based column index to convert
     * @param label    text displayed on every button in the column
     * @param color    button background colour
     * @param callback invoked with the clicked row index
     */
    protected void setButtonColumn(int colIndex, String label, Color color, ButtonRowCallback callback) {
        JButton renderer = makeButton(label, color);
        JButton editor   = makeButton(label, color);
        int[] clickedRow = {-1};

        TableColumn column = table.getColumnModel().getColumn(colIndex);
        column.setCellRenderer((t, val, sel, foc, row, col) -> renderer);
        column.setCellEditor(new DefaultCellEditor(new JCheckBox()) {
            @Override
            public Component getTableCellEditorComponent(
                    JTable t, Object val, boolean sel, int row, int col) {
                clickedRow[0] = row;
                return editor;
            }
            @Override public Object getCellEditorValue() { return label; }
        });

        editor.addActionListener(e -> {
            column.getCellEditor().stopCellEditing();
            callback.onButtonClicked(clickedRow[0]);
        });
    }

    /** Callback notified when a button inside a table row is clicked. */
    @FunctionalInterface
    public interface ButtonRowCallback {
        /**
         * Called when the user clicks the button in the given row.
         *
         * @param row zero-based row index of the clicked button
         */
        void onButtonClicked(int row);
    }

    /** Appends a new row of data to the table.
     *
     * @param row cell values matching the column order
     */
    public void addRow(Object[] row)   { model.addRow(row); }

    /** Removes all rows from the table. */
    public void clearRows()            { model.setRowCount(0); }

    /**
     * Updates a single cell value in the table model.
     *
     * @param value new value to set
     * @param row   zero-based row index
     * @param col   zero-based column index
     */
    public void setValueAt(Object value, int row, int col) { model.setValueAt(value, row, col); }
}
