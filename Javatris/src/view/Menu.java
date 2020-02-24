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

//JPanel shown in the modal JDialog above

public class Menu {
	private static final Color panelColor = new Color(10,20,70);
	private static final int ALPHA = 180; //0-255
	private static final Color GP_BG = new Color(0, 0, 0, ALPHA);
	private JPanel panel;
	private Component com;

	private JPanel titlePane;
	private JPanel buttonsPane;
	private final int bt;	//border thickness
	private final Dimension panelSize;
	
	public Menu(Component com,Dimension panelSize,int bt) {
		this.bt = bt;
		this.panelSize = panelSize;
		this.com = com; 
		init();
	}
	
	private void init() {
		
		// the main menu panel
		panel = new JPanel();
		panel.setBackground(Color.BLACK);
		panel.setPreferredSize(panelSize);
		panel.setBorder(BorderFactory.createLineBorder(Color.GRAY,bt));
		
		// a panel for title(1/4 of whole panel)
		titlePane = new JPanel();
		int tpw = (int)panelSize.getWidth()-bt*2;	//title pane width
		int tph = (int)panelSize.getHeight()/4;		//title pane height
		titlePane.setBackground(Color.BLACK);
		titlePane.setLayout(new GridBagLayout());
		titlePane.setPreferredSize(new Dimension(tpw,tph));
		JLabel pausedLbl = new JLabel("PAUSE");
		pausedLbl.setForeground(Color.WHITE);
		pausedLbl.setFont(new Font("Arial", Font.BOLD,(int)panelSize.getWidth()/10));
		titlePane.add(pausedLbl);
		
		// a panel for buttons(3/4 of whole panel)
		buttonsPane = new JPanel();
		int bpbt = 10;	//buttons pane border thickness
		int vgap = (int)panelSize.getHeight()/40;	//vertical gap
		int bpw = tpw;	//buttons pane width
		int bph = (int)panelSize.getHeight()*3/4 -bt*2 -bpbt;	//buttons pane height
		buttonsPane.setPreferredSize(new Dimension(bpw,bph));
		buttonsPane.setBackground(Color.BLACK);
		buttonsPane.setBorder(new EmptyBorder(bpbt,bpbt,bpbt,bpbt));
		buttonsPane.setLayout(new GridLayout(3,1,1,vgap));
		

	}
 
	public void close() {
		Window winn = SwingUtilities.getWindowAncestor(panel);
		winn.dispose();
	}
 
	public void addElement(Container element) {
		buttonsPane.add(element);
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
     	JDialog dialog = new JDialog((Window)window, "", ModalityType.APPLICATION_MODAL);
     	dialog.getContentPane().add(panel); 
     	dialog.setUndecorated(true); // remove borders
     	dialog.pack(); 
     	dialog.setLocationRelativeTo((Window)window); //center dialog on panel
     	dialog.setVisible(true); 
     	darkPane.setVisible(false);
	}

}