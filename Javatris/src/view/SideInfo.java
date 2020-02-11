package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.BoxLayout;
import javax.swing.GroupLayout.Alignment;

public class SideInfo extends JPanel {
	private static final long serialVersionUID = 1L;
	
	
//	private BufferedImage scoreTitle;
	private JLabel scoreText;
	private JLabel timeLabel;
	private JLabel next;
	private JPanel nextPanel;
	private int[] shape;
	private BoxLayout box;
	
	public SideInfo() {
	
		box = new BoxLayout(this, BoxLayout.Y_AXIS);
		
		
		this.setBorder(new EmptyBorder(10, 20, 10, 10));
		
		
		
		this.setBackground(Color.LIGHT_GRAY);
		this.setPreferredSize(new Dimension(300,800));
		this.setLayout(box);
		
		scoreText = new JLabel("SCORE: 0");
		scoreText.setFont(new Font("Arial", Font.ITALIC, 30));
		
		
		timeLabel = new JLabel("TIME: 100");
		timeLabel.setFont(new Font("Arial", Font.ITALIC, 30));
		
		next = new JLabel("NEXT SHAPE");
		next.setFont(new Font("Arial", Font.PLAIN, 30));	
		next.setBorder(new EmptyBorder(40, 35, 10, 10));
		
		nextPanel = new JPanel(new GridLayout(4,18,1,1));
		nextPanel.setBackground(Color.black);
		nextPanel.setBorder(new EmptyBorder(1, 1, 1, 1));
		
		this.add(scoreText);
		this.add(timeLabel);
		this.add(next);
		this.add(nextPanel);
		
		
		
		//next.setAlignmentX(CENTER_ALIGNMENT);;
	}
	
	//Uppdaterar score när den kallas utifrån
	public void updateScore(int score) {
		this.scoreText.setText("SCORE: "+score);
	}
	
	//Kanske borde hållas reda på i denna klass
	//Borde kanske inte vara en int som inparameter
	public void updateTime(int time) {
		this.timeLabel.setText("TIME: "+time);
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
//		g.drawImage(scoreTitle,0,0,200,50,null);
	}
}
