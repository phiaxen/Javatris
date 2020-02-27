package view;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedList;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import model.Shape;
import javax.swing.BoxLayout;



public class SideInfo extends JPanel implements PropertyChangeListener{
	
	private static final long serialVersionUID = 1L;
	private JLabel scoreLabel;
	private JLabel timeLabel;
	private JLabel nextLabel;
	private JLabel linesLabel;
	private JLabel levelLabel;
	private NextShapesPanel nextShapesPanel;
	private int rowsRemoved = 0;
	private int level = 1;
	
	public SideInfo() {
		
		nextShapesPanel = new NextShapesPanel();
		
		this.setBorder(new EmptyBorder(20, 20, 20, 20));
		this.setBackground(Color.DARK_GRAY);
		this.setPreferredSize(new Dimension(280,800));
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		//scoreText
		scoreLabel = new JLabel("SCORE: 0");
		scoreLabel.setForeground(Color.LIGHT_GRAY);
		scoreLabel.setFont(new Font("Arial", Font.BOLD, 30));
		
		//scoreText
		timeLabel = new JLabel("TIME: 0");
		timeLabel.setForeground(Color.LIGHT_GRAY);
		timeLabel.setFont(new Font("Arial",Font.PLAIN, 30));
		
		linesLabel = new JLabel("Lines: " + rowsRemoved);
		linesLabel.setForeground(Color.LIGHT_GRAY);
		linesLabel.setFont(new Font("Arial",Font.PLAIN, 30));
		
		levelLabel = new JLabel("Level: " + level);
		levelLabel.setForeground(Color.LIGHT_GRAY);
		levelLabel.setFont(new Font("Arial",Font.PLAIN, 30));
		
		nextLabel = new JLabel("NEXT SHAPE");
		nextLabel.setForeground(Color.LIGHT_GRAY);
		nextLabel.setFont(new Font("Arial", Font.PLAIN, 30));	
		nextLabel.setBorder(new EmptyBorder(40, 30, 10, 10));
		
		
		
		this.add(scoreLabel);
		this.add(timeLabel);
		this.add(linesLabel);
		this.add(levelLabel);
		this.add(nextLabel);
		this.add(nextShapesPanel);
		
		
		
//		next.setAlignmentX(CENTER_ALIGNMENT);
	}
	
	//Uppdaterar score när den kallas utifrån
	public void updateScore(int score) {
		System.out.println("SCOR IS: " + score);
		scoreLabel.setText("SCORE: " + score);
		
		scoreLabel.repaint();
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
