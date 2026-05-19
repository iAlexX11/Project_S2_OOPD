package org.cryptoBros.presentation.Views;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.awt.*;

public abstract class AbstractTable extends JPanel {
    protected DefaultTableModel model;
    protected JTable table;

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

    protected abstract boolean isColumnEditable(int col);
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
            callback.onButtonClicked(clickedRow[0]);
            column.getCellEditor().stopCellEditing();
        });
    }

    @FunctionalInterface
    public interface ButtonRowCallback {
        void onButtonClicked(int row);
    }

    public void addRow(Object[] row)   { model.addRow(row); }
    public void clearRows()            { model.setRowCount(0); }
    public void setValueAt(Object value, int row, int col) { model.setValueAt(value, row, col); }
}
