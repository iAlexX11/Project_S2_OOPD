package org.cryptoBros.presentation.Views;

import org.cryptoBros.persistence.Exceptions.DbConnectionException;
import org.cryptoBros.persistence.Exceptions.UserNotFoundException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public abstract class Pages extends BaseView{

	private double balance;
	private JLabel jLBalance;

	public abstract JPanel setHeader();

	public JLabel setBalance() {
		jLBalance = new JLabel();
		jLBalance.setForeground(Color.WHITE);
		jLBalance.setFont(new Font("Apple Casual", Font.PLAIN, 18));
		return jLBalance;
	}

	public void updateBalance(double balance){
		this.balance = balance;
		jLBalance.setText("The balance is: " + String.format("%.2f", balance) + "€");
		jLBalance.setForeground(Color.WHITE);
		jLBalance.setFont(new Font("Apple Casual", Font.PLAIN, 18));
	}

}
