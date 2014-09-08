package eu.bibl.launcher.ui.components.img;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * @author sc4re
 */
public class ImagePanel extends JPanel {
	
	private static final long serialVersionUID = 7139353926453246541L;
	
	/** The image we render **/
	private final BufferedImage image;
	
	public ImagePanel(InputStream stream) throws IOException {
		image = ImageIO.read(stream);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		int width = getWidth();
		int height = getHeight();
		float scale = 0.99f;
		float imgW = image.getWidth(null);
		float imgH = image.getHeight(null);
		while ((imgW > width) || (imgH > height)) {
			imgW = imgW * scale;
			imgH = imgH * scale;
		}
		int w = (int) imgW;
		int h = (int) imgH;
		int x = w / 2;
		int y = (h / 2) - (h / 2);
		g.drawImage(image, x, y, w, h, null); // see javadoc for more info on the parameters
	}
}
