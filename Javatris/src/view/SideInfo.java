package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import model.Board;
import model.Shape;

import javax.swing.BoxLayout;
import javax.swing.GroupLayout.Alignment;

public class SideInfo extends JPanel implements PropertyChangeListener{
	private static final long serialVersionUID = 1L;
	
//	private BufferedImage scoreTitle;
	private JLabel scoreText;
	private JLabel timeLabel;
	private JLabel nextLabel;
	private JLabel linesLabel;
	private JLabel levelLabel;
	
	private NextShapesPanel nextShapesPanel;
	
	private BoxLayout box;
	private int rowsRemoved = 0;
	private int level = 1;
	
	
	
	public SideInfo() {
		
		nextShapesPanel = new NextShapesPanel();
		
		box = new BoxLayout(this, BoxLayout.Y_AXIS);
		
		this.setBorder(new EmptyBorder(20, 20, 20, 20));
	
		//this.setBackground(Color.LIGHT_GRAY);
		this.setBackground(Color.LIGHT_GRAY);
		this.setPreferredSize(new Dimension(280,800));
		this.setLayout(box);
		
		scoreText = new JLabel("SCORE: 0");
		scoreText.setFont(new Font("Arial", Font.BOLD, 30));
		
		timeLabel = new JLabel("TIME: 0");
		timeLabel.setFont(new Font("Arial",Font.PLAIN, 30));
		
		linesLabel = new JLabel("Lines: " + rowsRemoved);
		linesLabel.setFont(new Font("Arial",Font.PLAIN, 30));
		
		levelLabel = new JLabel("Level: " + level);
		levelLabel.setFont(new Font("Arial",Font.PLAIN, 30));
		
		nextLabel = new JLabel("NEXT SHAPE");
		nextLabel.setFont(new Font("Arial", Font.PLAIN, 30));	
		nextLabel.setBorder(new EmptyBorder(40, 30, 10, 10));
		
		
		
		this.add(scoreText);
		this.add(timeLabel);
		this.add(linesLabel);
		this.add(levelLabel);
		this.add(nextLabel);
		this.add(nextShapesPanel);
		
		
		
//		next.setAlignmentX(CENTER_ALIGNMENT);
	}
	
	//Uppdaterar score när den kallas utifrån
	public void updateScore(int score) {
		this.scoreText.setText("SCORE: " + score);
		scoreText.repaint();
	}
	
	//Kanske borde hållas reda på i denna klass
	//Borde kanske inte vara en int som inparameter
	public void updateTime(int time) {
		this.timeLabel.setText("TIME: " + time + "s");
		timeLabel.repaint();

	}
	
	public void updateLines(int lines) {
		this.linesLabel.setText("Lines: " + lines);
		linesLabel.repaint();


	}
	
	public void updateLevel(int level) {
		this.levelLabel.setText("Level: " + level);
		levelLabel.repaint();
	}

	public void updateNextShape(LinkedList<Shape> shapes) {
		for(int i = 0; i < 3; i++) {
			nextShapesPanel.updateNextShape(shapes.get(i).clone());
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
	
	
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
//		g.drawImage(scoreTitle,0,0,200,50,null);
		
		
	}

	/**
	 * Handles updating next-shape sequence after update in GameEngine
	 * @param evt : event triggering update
	 */
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
