package org.cryptoBros.presentation.Views;

import org.cryptoBros.persistence.Exceptions.ErrorChangingProfilePictureException;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.io.File;
import java.io.IOException;

class ProfilePicture extends JPanel {
	private Image image;

	public ProfilePicture(String path) throws ErrorChangingProfilePictureException {
		try {
			image = ImageIO.read(new File(path));
		} catch (IOException e) {
			throw new ErrorChangingProfilePictureException("Error while trying to change the profile picture");
		}
		setOpaque(false);
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setClip(new Ellipse2D.Float(0, 0, getWidth(), getHeight()));
		g2.drawImage(image, 0, 0, getWidth(), getHeight(), this);
	}
}