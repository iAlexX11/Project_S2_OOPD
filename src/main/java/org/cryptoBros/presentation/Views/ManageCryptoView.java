package org.cryptoBros.presentation.Views;
import org.cryptoBros.presentation.Enum.ButtonEnumeration;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ManageCryptoView extends Pages {

    private ManageCryptoTable manageCryptoTable;
	private JButton addButton;
	private String confirmedCryptoName;
	private ActionListener actionListener;

	@Override
	protected void configureView() {
		getContent().add(setCore(), BorderLayout.CENTER);
	}

	@Override
	public void setActions(ActionListener listener) {
		this.actionListener = listener;
		getContent().add(setHeader(), BorderLayout.NORTH);
		addHeaderActions(listener);
		addButton.addActionListener(listener);
	}

    private JPanel setCore() {
        JPanel core = new JPanel(new BorderLayout());
        core.setBackground(new Color(239, 247, 255));
        core.add(buildTableSection(), BorderLayout.CENTER);
        core.add(buildAddSection(), BorderLayout.SOUTH);
        return core;
    }

    private JPanel buildTableSection() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(new Color(239, 247, 255));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 10, 50));

        JLabel jlTitle = new JLabel("Manage Cryptocurrencies", SwingConstants.CENTER);
        jlTitle.setFont(new Font("SansSerif", Font.BOLD, 30));
        jlTitle.setForeground(Color.BLACK);

        manageCryptoTable = new ManageCryptoTable();

        panel.add(jlTitle, BorderLayout.NORTH);
        panel.add(manageCryptoTable, BorderLayout.CENTER);

        return panel;
    }

    private JPanel buildAddSection() {
        JPanel addSection = new JPanel(new FlowLayout(FlowLayout.CENTER));
        addSection.setBackground(new Color(239, 247, 255));
        addSection.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(185, 200, 245));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(150, 170, 230), 1, true),
                BorderFactory.createEmptyBorder(16, 40, 16, 40)
        ));

        JLabel addTitle = new JLabel("Add New Cryptocurrency", SwingConstants.CENTER);
        addTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        addTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel addDescription = new JLabel("Select the JSON file containing the currency information.");
        addDescription.setFont(new Font("SansSerif", Font.PLAIN, 13));
        addDescription.setAlignmentX(Component.CENTER_ALIGNMENT);

        addButton = new JButton("ADD");
        addButton.setActionCommand(ButtonEnumeration.ADD_CRYPTO.name());
        addButton.setFont(new Font("SansSerif", Font.BOLD, 15));
        addButton.setBackground(new Color(99, 125, 217));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        addButton.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));
        addButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        card.add(addTitle);
        card.add(Box.createVerticalStrut(6));
        card.add(addDescription);
        card.add(Box.createVerticalStrut(12));
        card.add(addButton);

        addSection.add(card);
        return addSection;
    }

    public void setCryptoData (Object[][] data) {
        manageCryptoTable.clearRows();
        for (Object[] row : data) manageCryptoTable.addRow(row);
    }

    public void setDeleteCallback(AbstractTable.ButtonRowCallback callback) {
        manageCryptoTable.setDeleteCallback(callback);
    }

	public void setEditCallback(AbstractTable.ButtonRowCallback callback) {
		manageCryptoTable.setEditCallback(row -> {
			askNewCryptoName();
			callback.onButtonClicked(row);
		});
	}

	private JDialog createBaseDialog(String title) {
		JDialog dialog = new JDialog();
		dialog.setTitle(title);
		dialog.setModal(true);
		return dialog;
	}

	private GridBagConstraints createFieldConstraints(int column, int row) {
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = column;
		constraints.gridy = row;
		constraints.insets = new Insets(8, 10, 8, 10);
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.weightx = column == 1 ? 1.0 : 0;
		return constraints;
	}

	private void askNewCryptoName() {
		JDialog dialog = createBaseDialog("Change Crypto Name");
		JPanel formPanel = new JPanel(new GridBagLayout());
		formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		JTextField usernameField = new JTextField(15);
		formPanel.add(new JLabel("New Name:"), createFieldConstraints(0, 0));
		formPanel.add(usernameField, createFieldConstraints(1, 0));

		JButton submitButton = new JButton("Confirm");
		submitButton.addActionListener(e -> {
			confirmedCryptoName = usernameField.getText().trim();
			actionListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, ButtonEnumeration.EDIT_CRYPTO.name()));
			dialog.dispose();
		});

		GridBagConstraints buttonConstraints = createFieldConstraints(0, 1);
		buttonConstraints.gridwidth = 2;
		buttonConstraints.fill = GridBagConstraints.NONE;
		buttonConstraints.anchor = GridBagConstraints.CENTER;
		formPanel.add(submitButton, buttonConstraints);

		dialog.add(formPanel);
		dialog.pack();
		dialog.setLocationRelativeTo(null);
		dialog.setVisible(true);
	}

    public String getCryptoNameAtRow(int row) {
        return (String) manageCryptoTable.model.getValueAt(row, 0);
    }

    public JFileChooser createFileChooser() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Cryptocurrency JSON file");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("JSON files", "json"));
        return fileChooser;
    }

	public String getCryptoName() {
		return confirmedCryptoName;
	}
}
