package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.Dialog.ModalityType;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.RootPaneContainer;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

/**
 * A Dialog combined with a glassPane and panels
 * @author Philip
 * @version 1.0
 */
public class DialogMenu {
	
	private static final Color panelColor = new Color(0,0,0);
	private static final int ALPHA = 180; //0-255
	private static final Color GP_BG = new Color(0, 0, 0, ALPHA); //for glassPane 
	private JPanel panel;	
	private JPanel topPanel;
	private JPanel bottomPanel;
	private JPanel darkPane;
	private JDialog dialog;
	private Component com;
	private final Dimension panelSize;
	private final String title;
	private final int borderThickness;	
	private final int elements;
	private final int titleSize;
	private boolean isClosed = true;
	private RootPaneContainer window;
	
	/**
	 * Creates a dialog menu.
	 * @param com : the component to which the dialog box should be attached to
	 * @param panelSize : the dimensions of the dialog panel
	 * @param elements : the amount of elements on the menu 
	 * @param borderThickness : the border thickness of the dialog panel
	 * @param title : the title name
	 * @param titleSize : the size of the title
	 */
	public DialogMenu(Component com,Dimension panelSize,int elements,int borderThickness,String title, int titleSize) {
		this.com = com; 
		this.panelSize = panelSize;
		this.elements = elements;
		this.borderThickness = borderThickness;
		this.title = title;
		this.titleSize = titleSize;
		init();
	}
	
	/**
	 * Creates the main panel containing a titlePanel and a buttonsPanel
	 */
	private void init() {
		
		// the main menu panel
		panel = new JPanel();
		panel.setBackground(panelColor);
		panel.setPreferredSize(panelSize);
		panel.setBorder(BorderFactory.createLineBorder(Color.GRAY,borderThickness));
		
		// a panel for title(1/4 of whole panel)
		topPanel = new JPanel();
		int tpw = (int)panelSize.getWidth()-borderThickness*2;	//title panel width
		int tph = (int)panelSize.getHeight()/4;				//title panel height
		int tpbt = 10;										//title panel border thickness
		topPanel.setBackground(panelColor);
		topPanel.setBorder(new EmptyBorder(tpbt,tpbt,tpbt,tpbt));
		topPanel.setLayout(new GridBagLayout());
		topPanel.setPreferredSize(new Dimension(tpw,tph));
		
		// the title on the dialog menu
		JLabel Lbl = new JLabel(title);
		Lbl.setForeground(Color.WHITE);
		Lbl.setFont(new Font("Arial", Font.BOLD,titleSize));
		topPanel.add(Lbl);
		
		// a panel for elements(3/4 of whole panel)
		bottomPanel = new JPanel();
		int bpbt = 10;	//buttons pane border thickness
		int vgap = (int)panelSize.getHeight()/40;	//vertical gap
		int bpw = tpw;	//buttons pane width
		int bph = (int)panelSize.getHeight()*3/4 -borderThickness*2 -bpbt;	//buttons pane height
		bottomPanel.setPreferredSize(new Dimension(bpw,bph));
		bottomPanel.setBackground(panelColor);
		bottomPanel.setBorder(new EmptyBorder(bpbt,bpbt,bpbt,bpbt));
		bottomPanel.setLayout(new GridLayout(elements,1,1,vgap));
		
		panel.add(topPanel);
		panel.add(bottomPanel);
		
		// create glass pane
		darkPane = new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g) {
				//keep it dark
				g.setColor(getBackground());
				g.fillRect(0, 0, getWidth(), getHeight());
				super.paintComponent(g);
			}
		};
     
     	darkPane.setOpaque(false); 
     	darkPane.setBackground(GP_BG); 
     	
     	dialogSetup();
	}
	
	/**
	 * Create a dialog
	 */
	private void dialogSetup() {
		window = (RootPaneContainer) SwingUtilities.getWindowAncestor(com);
	     
     	//make a JDialog containing the panel made in this class(panel) with the dark pane
     	dialog = new JDialog((Window)window, "", ModalityType.APPLICATION_MODAL);
     	dialog.getContentPane().add(panel); 
     	dialog.setUndecorated(true); // remove borders
     	dialog.pack(); 
	}
 
	/**
	 * Adds an element to the menu
	 * @param element : the element to be added to this menu
	 */
	public void addElement(Component element) {
		bottomPanel.add(element);
	}
	
	/**
	 * open the menu
	 */
	public void open() {
		isClosed = false;
		dialog.setLocationRelativeTo((Window)window);
		window.setGlassPane(darkPane);
		darkPane.setVisible(true); 
		dialog.setVisible(true);
	}
	
	/**
	 * Closes the menu
	 */
	public void close() {
		isClosed = true;
		dialog.setLocationRelativeTo((Window)window);
		window.setGlassPane(darkPane);
		darkPane.setVisible(false);
		dialog.setVisible(false); 
	}
	
	public boolean isClosed() {
		return isClosed;
	}
	
}