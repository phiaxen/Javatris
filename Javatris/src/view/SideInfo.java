package view;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedList;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import model.Shape;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;



public class SideInfo extends JPanel implements PropertyChangeListener{
	
	private static final long serialVersionUID = 1L;
	private JLabel scoreLabel;
	private JLabel timeLabel;
	private JLabel nextLabel;
	private JLabel linesLabel;
	private JLabel levelLabel;
	private NextShapesPanel nextShapesPanel;
	private JPanel panels[];
	private int rowsRemoved = 0;
	private int level = 1;
	private Color color;
	
	public SideInfo() {
		color = new Color(30,30,30);
		panels = new JPanel[3];
		
		nextShapesPanel = new NextShapesPanel();
		JPanel bottomPanel = new JPanel();
		JPanel topPanel = new JPanel();
		
		
		
		topPanel.setPreferredSize(new Dimension(240,300));
		topPanel.setLayout(new GridLayout(3,1,0,20));
		topPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
		topPanel.setBackground(color);
		
		
		bottomPanel.setBackground(Color.DARK_GRAY);
		bottomPanel.setLayout(new BoxLayout(bottomPanel,BoxLayout.Y_AXIS));
		
		this.setBorder(new EmptyBorder(20, 20, 20, 20));
		this.setBackground(color);
		this.setPreferredSize(new Dimension(280,800));
		this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		
		//time
		JPanel timePanel = new JPanel();
		timePanel.setLayout(new BorderLayout());
		timePanel.setBackground(color);
		timeLabel = new JLabel("TIME: 0");
		timeLabel.setForeground(Color.WHITE);
		timeLabel.setFont(new Font("Arial",Font.PLAIN, 15));
		timePanel.add(timeLabel, BorderLayout.WEST);

		nextLabel = new JLabel("NEXT",SwingConstants.CENTER);
		nextLabel.setForeground(Color.WHITE);
		nextLabel.setFont(new Font("Arial", Font.BOLD, 30));	
		nextLabel.setBorder(new EmptyBorder(0, 65, 0, 0));

		
		scoreLabel = new JLabel("0",SwingConstants.CENTER);
		linesLabel = new JLabel("" + rowsRemoved,SwingConstants.CENTER);
		levelLabel = new JLabel("" + level,SwingConstants.CENTER);
		
		topPanel.add(makePanel(0,"SCORE",scoreLabel));
		topPanel.add(makePanel(1,"LINES",linesLabel));
		topPanel.add(makePanel(2,"LEVEL",levelLabel));
		bottomPanel.add(nextLabel);
		bottomPanel.add(nextShapesPanel);
		
		this.add(timePanel);
		this.add(topPanel);
		this.add(bottomPanel);
		
		
//		next.setAlignmentX(CENTER_ALIGNMENT);
	}
	
	private JPanel makePanel(int id,String title,JLabel label) {
		panels[id] = new JPanel();
		
		JLabel titleLabel = new JLabel(title,SwingConstants.CENTER);
		
		titleLabel.setForeground(Color.WHITE);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 15));
		
		label.setForeground(Color.WHITE);
		label.setFont(new Font("Arial", Font.BOLD, 40));
		
		panels[id].setLayout(new BorderLayout());
		panels[id].setBackground(color);
		
		panels[id].add(titleLabel,BorderLayout.NORTH);
		panels[id].add(label,BorderLayout.CENTER);
		return panels[id];
	}
	
	//Uppdaterar score när den kallas utifrån
	public void updateScore(int score) {
		scoreLabel.setText(""+score);
		
		scoreLabel.repaint();
	}
	
	//Kanske borde hållas reda på i denna klass
	//Borde kanske inte vara en int som inparameter
	public void updateTime(int time) {
		this.timeLabel.setText("TIME: " + time + "s");
		timeLabel.repaint();

	}
	
	public void updateLines(int lines) {
		this.linesLabel.setText(""+lines);
		linesLabel.repaint();


	}
	
	public void updateLevel(int level) {
		this.levelLabel.setText("" + level);
		levelLabel.repaint();
	}

	public void updateNextShape(LinkedList<Shape> shapes) {
		for(int i = 0; i < shapes.size(); i++) {
			nextShapesPanel.updateNextShape(shapes);
		}
		nextShapesPanel.repaint();
	}


	
//	private void init() {
//		
//		try {
//			scoreTitle = ImageIO.read(Board.class.getResource("/images/scoreTitle.png"));
//			
//		}catch(IOException e) {
//			e.printStackTrace();
//		}
//	}
	
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
//		g.drawImage(scoreTitle,0,0,200,50,null);
		
		
	}

	/**
	 * Handles updating next-shape sequence after update in GameEngine
	 * @param evt : event triggering update
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		String property = evt.getPropertyName();
		if(property.equals("next shape")) {
			updateNextShape((LinkedList<Shape>)evt.getNewValue());
		}
		
		if(property.equals("time")) {
			updateTime((int)evt.getNewValue());
		}
		
		if(property.equals("level")) {
			updateLevel((int)evt.getNewValue());
		}
		
		if(property.equals("points")) {
			updateScore((int)evt.getNewValue());
		}
		
		if(property.equals("lines cleared")) {
			updateLines((int)evt.getNewValue());
		}
	}
}
