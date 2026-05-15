package org.cryptoBros.presentation.Views;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.awt.*;

public class PortfolioTable extends JPanel {

    private static final String[] COLUMNS = {"Cryptocurrency", "Units", "Buy Price", "Balance", ""};
    private DefaultTableModel model;
    private JTable table;

    public PortfolioTable() {
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        model = new DefaultTableModel(null, COLUMNS) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
        };

        setTable();
        setHeader();
        setColumns();
        setSellColumn();
        setScrollPane();
    }

    private void setTable() {
        table = new JTable(model);

        table.setBackground(Color.WHITE);
        table.setForeground(Color.BLACK);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.setRowHeight(38);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(200, 220, 255));
        table.setSelectionForeground(Color.BLACK);
        table.setFocusable(false);

        int[] widths = {160, 60, 130, 150, 80};
        for  (int i = 0; i < widths.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }
    }

    private void setHeader () {
        JTableHeader thHeader = table.getTableHeader();
        thHeader.setBackground(new Color(230, 230, 230));
        thHeader.setForeground(new Color(100, 100, 100));
        thHeader.setFont(new Font("SansSerif", Font.PLAIN, 12));
        thHeader.setReorderingAllowed(false);
        thHeader.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 200, 200)));
        thHeader.setPreferredSize(new Dimension(0, 40));
        ((DefaultTableCellRenderer) thHeader.getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
    }

    private void setColumns () {
        DefaultTableCellRenderer tcRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setOpaque(true);
                setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 200, 200)));

                if (!isSelected) setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 245, 245));

                String string = value == null ? "" : value.toString();

                switch (column) {
                    case 0:
                        setForeground(Color.BLACK);
                        setFont(new Font("SansSerif", Font.BOLD, 14));
                        setHorizontalAlignment(LEFT);
                        break;
                    case 1:
                        setForeground(Color.BLACK);
                        setFont(new Font("SansSerif", Font.PLAIN, 14));
                        setHorizontalAlignment(RIGHT);
                        break;
                    case 2:
                        setForeground(Color.BLACK);
                        setFont(new Font("SansSerif", Font.PLAIN, 14));
                        setHorizontalAlignment(RIGHT);
                        break;
                    case 3:
                        setForeground(string.startsWith("+") ? Color.GREEN : Color.RED);
                        setFont(new Font("SansSerif", Font.BOLD, 14));
                        setHorizontalAlignment(RIGHT);
                        break;
                }

                return this;
            }
        };

        for (int i = 0; i < COLUMNS.length - 1; i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(tcRenderer);
        }
    }

    private void setSellColumn () {
        TableColumn sellColumn = table.getColumnModel().getColumn(4);

        JButton sellButton = new JButton("Sell");
        sellButton.setFont(new Font("SansSerif", Font.BOLD, 13));
        sellButton.setBackground(new Color(100, 125, 220));
        sellButton.setForeground(Color.WHITE);
        sellButton.setFocusPainted(false);
        sellButton.setOpaque(true);
        sellButton.setBorder(BorderFactory.createEmptyBorder(4, 12, 4, 12));

        int[] clickedRow = {-1};

        sellColumn.setCellEditor(new DefaultCellEditor(new JCheckBox()) {
          @Override
          public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
              clickedRow[0] = row;

              return sellButton;
          }

          @Override
          public Object getCellEditorValue() {return "Sell"; }
        });

        //TODO Sell button action listener here.
        sellButton.addActionListener(e -> {
            System.out.println("Sell clicked on row: " +  clickedRow[0]);
            sellColumn.getCellEditor().stopCellEditing();
        });
    }

    private void setScrollPane () {
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
