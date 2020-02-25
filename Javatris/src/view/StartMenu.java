package view;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;

import model.Board;

/**
 * A swing menu with a generic layout.
 * 
 * @author Joachim Antfolk
 * @version 2020-02-08
 * @param size : the size of the menu
 * @param elements : the number of elements to be added to the menu   
 */
public class StartMenu extends JPanel{

	private static final long serialVersionUID = 1L;
	private JPanel topPane;
	private JPanel botPane;
	private final int borderThickness;
	private Dimension size;
	private final float topSpace;
	private final float botSpace;
	
	private final int topElements;
	private final int botElements;
	private Color color;
	/**
	 * Creates a generic menu
	 */
//	public Menu() {
//		this(new Dimension(200, 200), 1);
//	}
	
	
	/**
	 * Creates a menu according to user specifications
	 * @param size : size of panel
	 * @param topElements  : amount of elements on top pane
	 * @param botElements : amount of elements on bottom pane
	 * @param topSpace : space between pane height and top element on top pane
	 * @param botSpace : space between pane height and top element on bottom pane
	 * @param color : color of the whole panel
	 */
	public StartMenu(Dimension size, int topElements,int botElements,float topSpace,float botSpace,Color color) {
		this.size = size;
		this.topElements = topElements;
		this.botElements = botElements;
		this.topSpace = topSpace;
		this.botSpace = botSpace;
	
		this.borderThickness = 10;
		menuSetup();
	}
	
	/**
	 * Applies a layout to the menu according to the callers size and element requirenments
	 * @param dimension : the size of the menu
	 * @param elements : the number of elements to be added to the menu
	 */
	private void menuSetup() {
		this.setPreferredSize(size);
		this.setBackground(color);
		this.setBorder(new EmptyBorder(borderThickness,borderThickness,borderThickness,borderThickness));
//		this.setBorder(BorderFactory.createLineBorder(Color.GRAY,borderThickness));
		
		
		
		
		topPane = new JPanel();
		topPane.setBackground(color);
		topPane.setBorder(new EmptyBorder(10,10,10,10));
		topPane.setLayout(new GridLayout(topElements,1,1,20));
		
		topPane.setPreferredSize(new Dimension((int)size.getWidth()-borderThickness*2,(int)(size.getHeight()*topSpace)));
		
		botPane = new JPanel();
		int bpHeight;
		botPane.setBackground(color);
//		buttonsPane.setBackground(Color.CYAN);
//		buttonsPane.setBorder(new EmptyBorder((int)size.getHeight()/10,10,(int)size.getHeight()/10,10));
		botPane.setBorder(new EmptyBorder(50,10,10,10));
		botPane.setLayout(new GridLayout(botElements,1,1,20));

		this.add(topPane);
		bpHeight = (int)(size.getHeight()*botSpace)-borderThickness*3;
		
		botPane.setPreferredSize(new Dimension((int)size.getWidth()-borderThickness*2,(bpHeight)));
		this.add(botPane);
	}
	
	/**
	 * Adds an element to the menu
	 * @param element : element to be added to the menu
	 */
	public void addElementBot(Container element) {
		botPane.add(element);
	}
	
	public void addElementTop(Container element) {
		topPane.add(element);
	}
	
	/**
	 * Adds an image as a title
	 * @param fileName : directory of the image  to used
	 */
	public void addTitle(String fileName) {
		BufferedImage image;
		JLabel title;
		try {
		
			image = ImageIO.read(StartMenu.class.getResource(fileName));
			
			float percent = (float)this.size.getWidth()/1700;
			image = ImageResizer.resize(image,percent);
			
			title = new JLabel(new ImageIcon(image));
			topPane.add(title);
		} catch (IOException e) {
			System.out.println("ERROR: File not found!");// placeholder
		}
		
	}
	
	/**
	 * Opens the menu
	 */
	public void openMenu() {
		this.setVisible(true);
	}
	
	/**
	 * Closes the menu
	 */
	public void closeMenu() {
		this.setVisible(false);
	}	
	
	/**
	 * Set location for menu
	 * @param x : menus new center x position
	 * @param y : menus new center y position
	 */
//	public void setLocation(int x, int y) {
//		int height = (int)this.getWidth();
//		int width = (int)this.getWidth();
//		
//		this.setLocation(x - (width/2), y - (height/2));
//	}
	
	/**
	 * Get menu-size
	 */
//	public Dimension getSize() {
//		return menuFrame.getSize();
//	}
	
	/*
	 * Get the menu's frame
	 */
//	public JFrame getFrame() 
//	{
//		return menuFrame;
//	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		
	}
}
