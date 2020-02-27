package view;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;

import model.Board;

/**
 * A menu with a generic layout.
 * @author Joachim Antfolk
 * @author Philip Axenhamn
 * @version 2020-02-08  
 */
public class Menu extends JPanel{

	private static final long serialVersionUID = 1L;
	private JPanel topPanel;
	private JPanel botPanel;
	private final int borderThickness;
	private Dimension size;
	private final float topSpace;
	private final float botSpace;
	private final int topElements;
	private final int botElements;
	private Color color;

	/**
	 * Creates a menu according to user specifications
	 * @param size : size of the panel
	 * @param topElements  : amount of elements on top pane
	 * @param botElements : amount of elements on bottom pane
	 * @param topSpace : space between pane height and top element on top pane
	 * @param botSpace : space between pane height and top element on bottom pane
	 * @param color : color of the whole panel
	 */
	public Menu(Dimension size, int topElements,int botElements,float topSpace,float botSpace,Color color) {
		this.size = size;
		this.topElements = topElements;
		this.botElements = botElements;
		this.topSpace = topSpace;
		this.botSpace = botSpace;
	
		this.borderThickness = 10;
		menuSetup();
	}
	
	/**
	 * Creates the main panel, top- and bottom panel according to user specifications
	 */
	private void menuSetup() {
		int bpHeight = (int)(size.getHeight()*botSpace)-borderThickness*3;
		
		//main panel setup
		this.setPreferredSize(size);
		this.setBackground(color);
		this.setBorder(new EmptyBorder(borderThickness,borderThickness,borderThickness,borderThickness));
		
		//top panel setup
		topPanel = new JPanel();
		topPanel.setBackground(color);
		topPanel.setBorder(new EmptyBorder(10,10,10,10));
		topPanel.setLayout(new GridLayout(topElements,1,1,20));
		topPanel.setPreferredSize(new Dimension((int)size.getWidth()-borderThickness*2,(int)(size.getHeight()*topSpace)));
		this.add(topPanel);
		
		//bottom panel setup
		botPanel = new JPanel();
		botPanel.setBackground(color);
		botPanel.setBorder(new EmptyBorder(50,10,10,10));
		botPanel.setLayout(new GridLayout(botElements,1,1,20));
		botPanel.setPreferredSize(new Dimension((int)size.getWidth()-borderThickness*2,(bpHeight)));
		this.add(botPanel);
	}
	
	/**
	 * Adds an element to the top panel
	 * @param element : element to be added to the menu
	 */
	public void addElementTop(Container element) {
		topPanel.add(element);
	}
	
	/**
	 * Adds an element to the bottom panel
	 * @param element : element to be added to the menu
	 */
	public void addElementBot(Container element) {
		botPanel.add(element);
	}
	
	/**
	 * Adds an image as a title
	 * @param fileName : directory of the image  to used
	 */
	public void addTitle(String fileName) {
		BufferedImage image;
		JLabel title;
		float percent = (float)this.size.getWidth()/1700;
		try {
			image = ImageIO.read(Menu.class.getResource(fileName));
			image = ImageResizer.resize(image,percent);
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
	 * @return returns true if this menu is visible, otherwise false
	 */
	public boolean isOpened() {
		return this.isVisible();
	}
	
}
