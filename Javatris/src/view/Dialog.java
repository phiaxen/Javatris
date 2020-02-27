package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.RootPaneContainer;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;



public class Dialog {
	private static final Color panelColor = new Color(10,20,70);
	private static final int ALPHA = 180; //0-255
	private static final Color GP_BG = new Color(0, 0, 0, ALPHA);
	private JPanel panel;
	private Component com;

	private JPanel titlePane;
	private JPanel buttonsPane;
	private final int bThickness;	//border thickness
	private final Dimension panelSize;
	private final String title;
	private final int buttons;
	private final int buttonsTextSize;
	private final int titleSize;
	private JDialog dialog;
	/**
	 * Creates a dialog panel
	 * @param com : the component to which the dialog box should be attached to
	 * @param panelSize : the dimensions of the dialog panel
	 * @param buttons : the amount of buttons 
	 * @param bThickness : the bordet thickness of the dialog panel
	 * @param buttonsTextSize : the size of the text for each button
	 * @param title : the title name
	 * @param titleSize : the size of the title
	 */
	public Dialog(Component com,Dimension panelSize,int buttons,int bThickness,int buttonsTextSize,String title, int titleSize) {
		this.bThickness = bThickness;
		this.panelSize = panelSize;
		this.com = com; 
		this.title = title;
		this.buttons = buttons;
		this.buttonsTextSize = buttonsTextSize;
		this.titleSize = titleSize;
		init();
	}
	
	private void init() {
		
		// the main menu panel
		panel = new JPanel();
		panel.setBackground(Color.BLACK);
		panel.setPreferredSize(panelSize);
		panel.setBorder(BorderFactory.createLineBorder(Color.GRAY,bThickness));
		
		// a panel for title(1/4 of whole panel)
		titlePane = new JPanel();
		int tpw = (int)panelSize.getWidth()-bThickness*2;	//title pane width
		int tph = (int)panelSize.getHeight()/4;		//title pane height
		int tpbt = 10;	//title pane border thickness
		titlePane.setBackground(Color.BLACK);
		titlePane.setBorder(new EmptyBorder(tpbt,tpbt,tpbt,tpbt));
		titlePane.setLayout(new GridBagLayout());
		titlePane.setPreferredSize(new Dimension(tpw,tph));
		
		JLabel Lbl = new JLabel(title);
		Lbl.setForeground(Color.WHITE);
		Lbl.setFont(new Font("Arial", Font.BOLD,titleSize));
		titlePane.add(Lbl);
		
		// a panel for buttons(3/4 of whole panel)
		buttonsPane = new JPanel();
		int bpbt = 10;	//buttons pane border thickness
		int vgap = (int)panelSize.getHeight()/40;	//vertical gap
		int bpw = tpw;	//buttons pane width
		int bph = (int)panelSize.getHeight()*3/4 -bThickness*2 -bpbt;	//buttons pane height
		buttonsPane.setPreferredSize(new Dimension(bpw,bph));
		buttonsPane.setBackground(Color.BLACK);
		buttonsPane.setBorder(new EmptyBorder(bpbt,bpbt,bpbt,bpbt));
		buttonsPane.setLayout(new GridLayout(buttons,1,1,vgap));
		

	}
 
	public void close() {
		Window winn = SwingUtilities.getWindowAncestor(panel);
		winn.dispose();
	}
	
	
	public void addButton(JButton button) {
		modifyButton(button);
		buttonsPane.add(button);
	}
	
	private void modifyButton(JButton button) {
		button.setFont(new Font("Arial", Font.BOLD, buttonsTextSize));
		button.setForeground(Color.WHITE);
		button.setBackground(Color.BLACK);
		button.setBorderPainted(false);
		button.setFocusPainted(false); 
	}
	
	
	//after adding elements call pack to put things together
	public void pack() {
		
		panel.add(titlePane);
		panel.add(buttonsPane);
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