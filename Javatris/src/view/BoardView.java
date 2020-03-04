package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import model.*;

/**
 * BoardView is a class that draws the 20x10 board on a JPanel. It can also draw
 * a grid.
 * 
 * @author Philip Axenhamn
 * @author Joachim Antfolk
 * @version 1.0
 */

public class BoardView extends JPanel implements PropertyChangeListener {

	private static final long serialVersionUID = 1L;
	private BufferedImage tiles, background;
	private int[][] boardCoords;
	private final int HEIGHT, WIDTH, BLOCKSIZE;
	private Shape currentShape;
	private boolean withGrid;
	private BufferedImage[] colors;

	public BoardView(int height, int width, int blocksize, boolean withGrid) {
		this.HEIGHT = height;
		this.WIDTH = width;
		this.BLOCKSIZE = blocksize;
		this.withGrid = withGrid;
		this.setPreferredSize(new Dimension(width * blocksize, height * blocksize));
		init();
	}

	/**
	 * Iniatiates the game board
	 */
	private void init() {
		colors = new BufferedImage[8];
		this.setBackground(Color.white); // om man vill ha en enfärgad bakgrund:
		boardCoords = new int[HEIGHT][WIDTH];
		loadImages();

		setColors();
	}

	/**
	 * Loads the images to be used in the game board
	 */
	private void loadImages() {
		try {
			tiles = ImageIO.read(Board.class.getResource("/images/tiles4.png"));
			background = ImageIO.read(Board.class.getResource("/images/background1.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sets the in-game tiles to loaded tile image
	 */
	private void setColors() {
		for (int i = 0; i < colors.length; i++) {
			colors[i] = tiles.getSubimage(i * BLOCKSIZE, 0, BLOCKSIZE, BLOCKSIZE);
		}
	}

	/**
	 * Sets the current shape to parameter currentShape
	 * 
	 * @param currentShape: new shape
	 */
	public void setCurrentShape(Shape currentShape) {
		this.currentShape = currentShape;
	}

	// paintComponent ritar upp bakgrund, Shape som man kan styra(currentShape), en
	// grid(om man vill) och statiska shapes(shapes som har kolliderat)
	// funktionen anropas av systemet
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		g2.drawImage(background, 0, 0, null); // Draw the background
		if (withGrid) {

			g2.setColor(Color.DARK_GRAY); // set color
			g2.setStroke(new BasicStroke(2)); // set thickness

			// horizontal
			for (int i = 0; i <= HEIGHT; i++) {
				g.drawLine(0, i * BLOCKSIZE, WIDTH * BLOCKSIZE, i * BLOCKSIZE);
			}

			// vertical
			for (int j = 0; j <= WIDTH; j++) {
				g.drawLine(j * BLOCKSIZE, 0, j * BLOCKSIZE, HEIGHT * BLOCKSIZE);
			}
		}

		// not bug free
		// draws currentShape
		for (int i = 0; i < currentShape.getCoords().length; i++) {
			for (int j = 0; j < currentShape.getCoords()[i].length; j++) {
				if (currentShape.getCoords()[i][j] == 1) {
					g.drawImage(colors[currentShape.getColor() - 1], j * BLOCKSIZE + currentShape.getX() * BLOCKSIZE,
							i * BLOCKSIZE + currentShape.getY() * BLOCKSIZE, null);
				}
			}
		}

		// draws shapes that has collided
		for (int i = 0; i < boardCoords.length; i++) {
			for (int j = 0; j < boardCoords[i].length; j++) {
				if (boardCoords[i][j] != 0) {
					g2.drawImage(tiles.getSubimage((boardCoords[i][j] - 1) * 40, 0, BLOCKSIZE, BLOCKSIZE),
							j * BLOCKSIZE, i * BLOCKSIZE, null);
				}

			}
		}

	}

	/**
	 * Applies property-changes from observed objects
	 * 
	 * @param evt : the fired event
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		String property = evt.getPropertyName();
		if (property.equals("board")) {
			boardCoords = (((Board) evt.getNewValue()).getBoard());
		}
		if (property.equals("shape")) {
			setCurrentShape(((Shape) evt.getNewValue()));
			repaint();
		}
	}
}
