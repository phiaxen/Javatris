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
	private JPanel titlePanel;
	private JPanel buttonsPanel;
	private JDialog dialog;
	private Component com;
	private final Dimension panelSize;
	private final String title;
	private final int bThickness;	//border thickness
	private final int buttons;
	private final int buttonsTextSize;
	private final int titleSize;
	
	/**
	 * Creates a dialog menu. 
	 * After adding buttons to this menu, use the pack function of this class.
	 * When this class is instantiated, the menu will appear. 
	 * This means that the menu will open when an object of this class created.
	 * To close the menu, use the close function of this class.
	 * @param com : the component to which the dialog box should be attached to
	 * @param panelSize : the dimensions of the dialog panel
	 * @param buttons : the amount of buttons 
	 * @param bThickness : the border thickness of the dialog panel
	 * @param buttonsTextSize : the size of the text for each button
	 * @param title : the title name
	 * @param titleSize : the size of the title
	 */
	public DialogMenu(Component com,Dimension panelSize,int buttons,int bThickness,int buttonsTextSize,String title, int titleSize) {
		this.bThickness = bThickness;
		this.panelSize = panelSize;
		this.com = com; 
		this.title = title;
		this.buttons = buttons;
		this.buttonsTextSize = buttonsTextSize;
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
		panel.setBorder(BorderFactory.createLineBorder(Color.GRAY,bThickness));
		
		// a panel for title(1/4 of whole panel)
		titlePanel = new JPanel();
		int tpw = (int)panelSize.getWidth()-bThickness*2;	//title panel width
		int tph = (int)panelSize.getHeight()/4;				//title panel height
		int tpbt = 10;										//title panel border thickness
		titlePanel.setBackground(panelColor);
		titlePanel.setBorder(new EmptyBorder(tpbt,tpbt,tpbt,tpbt));
		titlePanel.setLayout(new GridBagLayout());
		titlePanel.setPreferredSize(new Dimension(tpw,tph));
		
		// the title on the dialog menu
		JLabel Lbl = new JLabel(title);
		Lbl.setForeground(Color.WHITE);
		Lbl.setFont(new Font("Arial", Font.BOLD,titleSize));
		titlePanel.add(Lbl);
		
		// a panel for buttons(3/4 of whole panel)
		buttonsPanel = new JPanel();
		int bpbt = 10;	//buttons pane border thickness
		int vgap = (int)panelSize.getHeight()/40;	//vertical gap
		int bpw = tpw;	//buttons pane width
		int bph = (int)panelSize.getHeight()*3/4 -bThickness*2 -bpbt;	//buttons pane height
		buttonsPanel.setPreferredSize(new Dimension(bpw,bph));
		buttonsPanel.setBackground(panelColor);
		buttonsPanel.setBorder(new EmptyBorder(bpbt,bpbt,bpbt,bpbt));
		buttonsPanel.setLayout(new GridLayout(buttons,1,1,vgap));
	}
 
	/**
	 * Closes the menu
	 */
	public void close() {
		Window winn = SwingUtilities.getWindowAncestor(panel);
		winn.dispose();
	}
	
	/**
	 * Adds a button to the menu
	 * @param button : the button to be added to this menu
	 */
	public void addButton(JButton button) {
		modifyButton(button);
		buttonsPanel.add(button);
	}
	
	/**
	 * Modifies a button to fit the overall design of the menu.
	 * @param button : the button to be modified
	 */
	private void modifyButton(JButton button) {
		button.setFont(new Font("Arial", Font.BOLD, buttonsTextSize));
		button.setForeground(Color.WHITE);
		button.setBackground(panelColor);
		button.setBorderPainted(false);
		button.setFocusPainted(false); 
	}
	
	
	/**
	 * Adds the subPanels to the main panel and creates a JDialog with a glass pane
	 */
	public void pack() {
		
		panel.add(titlePanel);
		panel.add(buttonsPanel);
		
		// create glass pane
		JPanel darkPane = new JPanel() {
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
     
     	// a root pane container containing the JPanel(com) in the main frame
     	RootPaneContainer window = (RootPaneContainer) SwingUtilities.getWindowAncestor(com);
     	window.setGlassPane(darkPane);  
     	darkPane.setVisible(true);  
     	
     	//make a JDialog containing the panel made in this class(panel) with the dark pane
     	dialog = new JDialog((Window)window, "", ModalityType.APPLICATION_MODAL);
     	dialog.getContentPane().add(panel); 
     	dialog.setUndecorated(true); // remove borders
     	dialog.pack(); 
     	dialog.setLocationRelativeTo((Window)window); //center dialog on panel
     	dialog.setVisible(true); 
     	darkPane.setVisible(false); 	
	}
}