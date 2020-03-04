package view;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * ImageResizer should be used to resize a BufferedImage by width and height or
 * by a percentage of its original size
 * 
 * @author Philip Axenhamn
 * @version 1.0
 * @since 2020-02-08
 */
public abstract class ImageResizer {
	/**
	 * Resizes an BufferedImage by width and height
	 * 
	 * @param image:        the image to be resized
	 * @param scaledWidth:  absolute width in pixels
	 * @param scaledHeight: absolute height in pixels
	 * @throws IOException
	 */
	public static BufferedImage resize(BufferedImage image, int scaledWidth, int scaledHeight) throws IOException {
		// create the resized image
		BufferedImage resizedImage = new BufferedImage(scaledWidth, scaledHeight, image.getType());

		// scale the input image to the resized image
		Graphics2D g2d = resizedImage.createGraphics();
		g2d.drawImage(image, 0, 0, scaledWidth, scaledHeight, null);
		g2d.dispose();

		return resizedImage;
	}

	/**
	 * Resizes an image by a percentage of original size (proportional).
	 * 
	 * @param image:   path of the original image
	 * @param percent: the percentage change in size
	 * @throws IOException
	 */
	public static BufferedImage resize(BufferedImage image, double percent) throws IOException {
		int scaledWidth = (int) (image.getWidth() * percent);
		int scaledHeight = (int) (image.getHeight() * percent);
		return resize(image, scaledWidth, scaledHeight);
	}
	
}
