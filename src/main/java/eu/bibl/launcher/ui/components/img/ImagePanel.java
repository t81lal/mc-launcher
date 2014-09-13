package eu.bibl.launcher.ui.components.img;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JPanel;

import net.coobird.thumbnailator.Thumbnails;

/**
 * @author sc4re
 * @author Bibl
 */
public class ImagePanel extends JPanel {
	
	private static final long serialVersionUID = 7139353926453246541L;
	
	private final InputStream stream;
	private BufferedImage image;
	
	public ImagePanel(InputStream stream) throws IOException {
		this.stream = stream;
	}
	
	private int x, y;
	
	private BufferedImage getImage() {
		if (image != null)
			return image;
		try {
			image = Thumbnails.of(stream).size(getWidth(), getHeight()).asBufferedImage();
			x = (getWidth() - image.getWidth()) / 2;
			y = (getHeight() - image.getHeight()) / 2;
			y += 4;
			return image;
		} catch (IOException e) {
			return null;
		}
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.drawImage(getImage(), x, y, null);
	}
}