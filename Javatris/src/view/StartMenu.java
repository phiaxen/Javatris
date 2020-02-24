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
	private JPanel titlePane;
	private JPanel buttonsPane;
	private final int borderThickness;
	private Dimension size;
	private boolean hasTitle;
	
	/**
	 * Creates a generic menu
	 */
//	public Menu() {
//		this(new Dimension(200, 200), 1);
//	}
	
	/**
	 * Creates a menu according to user specifications 
	 */
	public StartMenu(Dimension size,Boolean hasTitle, int elements,Color color) {
		this.hasTitle = hasTitle;
		this.borderThickness = 10;
		this.size = size;
		menuSetup(elements,color);
	}
	
	/**
	 * Applies a layout to the menu according to the callers size and element requirenments
	 * @param dimension : the size of the menu
	 * @param elements : the number of elements to be added to the menu
	 */
	private void menuSetup(int elements,Color color) {
		this.setPreferredSize(size);
		this.setBackground(color);
		this.setBorder(new EmptyBorder(borderThickness,borderThickness,borderThickness,borderThickness));
		this.setBorder(BorderFactory.createLineBorder(Color.GRAY,borderThickness));
		
		titlePane = new JPanel();
		titlePane.setBackground(color);
		titlePane.setLayout(new GridLayout());
		titlePane.setPreferredSize(new Dimension((int)size.getWidth()-borderThickness*2,(int)size.getHeight()/4));
		
		buttonsPane = new JPanel();
		int bpHeight;
		buttonsPane.setBackground(color);
//		buttonsPane.setBorder(new EmptyBorder((int)size.getHeight()/10,10,(int)size.getHeight()/10,10));
		buttonsPane.setBorder(new EmptyBorder(10,10,10,10));
		buttonsPane.setLayout(new GridLayout(elements,1,1,20));

		
		if(hasTitle) {
			this.add(titlePane);
			bpHeight = (int)(size.getHeight()*3/4)-borderThickness*3;
		}else {
			bpHeight = (int)(size.getHeight())-borderThickness*3;
		}
		buttonsPane.setPreferredSize(new Dimension((int)size.getWidth()-borderThickness*2,(bpHeight)));
		this.add(buttonsPane);
	}
	
	/**
	 * Adds an element to the menu
	 * @param element : element to be added to the menu
	 */
	public void addElement(Container element) {
		buttonsPane.add(element);
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
			
			float percent = (float)this.size.getWidth()/2000;
			image = ImageResizer.resize(image,percent);
			
			title = new JLabel(new ImageIcon(image));
			titlePane.add(title);
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
