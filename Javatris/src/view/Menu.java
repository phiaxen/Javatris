package view;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;

/**
 * A menu with a generic layout.
 * 
 * @author Joachim Antfolk
 * @author Philip Axenhamn
 * @version 2020-02-08
 */
public class Menu extends JPanel {

	private static final long serialVersionUID = 1L;
	private JPanel topPanel;
	private JPanel bottomPanel;
	private final int borderThickness = 10;
	private final Dimension size;
	private final float topSpace;
	private final int topElements;
	private final int bottomElements;
	private final Color color;

	/**
	 * Creates a menu according to user specifications. Has a top- and bottom panel.
	 * 
	 * @param size : size of the panel
	 * @param topElements : amount of elements on top pane
	 * @param bottomElements : amount of elements on bottom pane
	 * @param topSpace : how much of the panel that should consist of the top panel. 
	 * A number between 0 and 1, where 1 = 100%. 
	 * The bottom panel will take the space that is left.
	 * @param color : color of the whole panel
	 */
	public Menu(Dimension size, int topElements, int bottomElements, float topSpace, Color color) {
		this.size = size;
		this.topElements = topElements;
		this.bottomElements = bottomElements;
		this.topSpace = topSpace;
		this.color = color;
		menuSetup();
	}

	/**
	 * Creates the main panel, top- and bottom panel according to user
	 * specifications
	 */
	private void menuSetup() {
		int topPanelHeight = (int) (size.getHeight() * topSpace);
		int bottomPanelHeight = (int) (size.getHeight() * (1 - topSpace)) - borderThickness * 3;

		// main panel setup
		this.setPreferredSize(size);
		this.setBackground(color);
		this.setBorder(new EmptyBorder(borderThickness, borderThickness, borderThickness, borderThickness));

		// top panel setup
		topPanel = new JPanel();
		topPanel.setBackground(color);
		topPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		topPanel.setLayout(new GridLayout(topElements, 1, 1, 20));
		topPanel.setPreferredSize(new Dimension((int) size.getWidth() - borderThickness * 2, topPanelHeight));
		this.add(topPanel);

		// bottom panel setup
		bottomPanel = new JPanel();
		bottomPanel.setBackground(color);
		bottomPanel.setBorder(new EmptyBorder(50, 10, 10, 10));
		bottomPanel.setLayout(new GridLayout(bottomElements, 1, 1, 20));
		bottomPanel.setPreferredSize(new Dimension((int) size.getWidth() - borderThickness * 2, (bottomPanelHeight)));
		this.add(bottomPanel);
	}

	/**
	 * Adds an element to the top panel
	 * 
	 * @param element : element to be added to the menu
	 */
	public void addElementTop(Container element) {
		topPanel.add(element);
	}

	/**
	 * Adds an element to the bottom panel
	 * 
	 * @param element : element to be added to the menu
	 */
	public void addElementBottom(Container element) {
		bottomPanel.add(element);
	}

	/**
	 * Adds an image as a title
	 * 
	 * @param fileName : directory of the image to used
	 */
	public void addTitle(String fileName) {
		BufferedImage image;
		JLabel title;
		float percent = (float) this.size.getWidth() / 1700;
		try {
			image = ImageIO.read(Menu.class.getResource(fileName));
			image = ImageResizer.resize(image, percent);
			title = new JLabel(new ImageIcon(image));
			topPanel.add(title);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("ERROR: File not found!");
		}
	}

	/**
	 * Opens the menu
	 */
	public void open() {
		this.setVisible(true);
	}

	/**
	 * Closes the menu
	 */
	public void close() {
		this.setVisible(false);
	}

	/**
	 * Closes the menu
	 * 
	 * @return returns true if this menu is visible, otherwise false
	 */
	public boolean isOpened() {
		return this.isVisible();
	}

}
