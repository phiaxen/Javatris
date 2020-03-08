package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedList;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import model.Shape;

import javax.swing.BoxLayout;

/**
 * SideInfo displays information related to the game
 * 
 * @author Tobias Mauritzon
 * @author Philip Axenhamn
 * @version 2.0
 * @since 2020-03-01
 */

public class SideInfo extends JPanel implements PropertyChangeListener {

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

	/**
	 * The constructor of this class initializes this panel.
	 */
	public SideInfo() {
		color = new Color(20, 20, 20);
		panels = new JPanel[3];

		nextShapesPanel = new NextShapesPanel();
		JPanel bottomPanel = new JPanel();
		JPanel topPanel = new JPanel();

		topPanel.setPreferredSize(new Dimension(240, 300));
		topPanel.setLayout(new GridLayout(3, 1, 0, 20));
		topPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
		topPanel.setBackground(color);

		bottomPanel.setBackground(Color.DARK_GRAY);
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));

		this.setBorder(new EmptyBorder(20, 20, 20, 20));
		this.setBackground(color);
		this.setPreferredSize(new Dimension(280, 800));
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		// time
		JPanel timePanel = new JPanel();
		timePanel.setLayout(new BorderLayout());
		timePanel.setBackground(color);
		timeLabel = new JLabel("Time: 0");
		timeLabel.setForeground(Color.WHITE);
		timeLabel.setFont(new Font("Arial", Font.PLAIN, 18));
		timePanel.add(timeLabel, BorderLayout.WEST);

		// nextshape
		JPanel nextPanel = new JPanel();
		nextPanel.setLayout(new BorderLayout());
		nextPanel.setBackground(Color.DARK_GRAY);
		nextLabel = new JLabel("NEXT", SwingConstants.CENTER);
		nextLabel.setForeground(Color.WHITE);
		nextLabel.setFont(new Font("Arial", Font.BOLD, 30));
		nextPanel.add(nextLabel, BorderLayout.CENTER);

		scoreLabel = new JLabel("0", SwingConstants.CENTER);
		linesLabel = new JLabel("" + rowsRemoved, SwingConstants.CENTER);
		levelLabel = new JLabel("" + level, SwingConstants.CENTER);

		topPanel.add(makePanel(0, "SCORE", scoreLabel));
		topPanel.add(makePanel(1, "LINES", linesLabel));
		topPanel.add(makePanel(2, "LEVEL", levelLabel));
		bottomPanel.add(nextPanel);
		bottomPanel.add(nextShapesPanel);

		this.add(timePanel);
		this.add(topPanel);
		this.add(bottomPanel);

	}

	/**
	 * Standardizes the GUI for "Final" JLabes and gathers them in one array
	 * 
	 * @param index the index for the panel
	 * @param title sets the displayed text
	 * @param label the Jlabel you want to standardize
	 * @return this function returns a customized JPanel from the array
	 */
	private JPanel makePanel(int index, String title, JLabel label) {
		panels[index] = new JPanel();

		JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);

		titleLabel.setForeground(Color.WHITE);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 15));

		label.setForeground(Color.WHITE);
		label.setFont(new Font("Arial", Font.BOLD, 40));

		panels[index].setLayout(new BorderLayout());
		panels[index].setBackground(color);

		panels[index].add(titleLabel, BorderLayout.NORTH);
		panels[index].add(label, BorderLayout.CENTER);
		return panels[index];
	}

	/**
	 * Update the scoreLabel with new score
	 * 
	 * @param score the score to be displayed
	 */
	public void updateScore(int score) {
		scoreLabel.setText("" + score);
		scoreLabel.repaint();
	}

	/**
	 * Update the timeLabel with new time
	 * 
	 * @param time the time to be displayed
	 */
	public void updateTime(int time) {
		this.timeLabel.setText("Time: " + time + "s");
		timeLabel.repaint();
	}

	/**
	 * Updates lines cleared with a new value
	 * 
	 * @param lines the amount of cleared lines to be displayed
	 */
	public void updateLines(int lines) {
		this.linesLabel.setText("" + lines);
		linesLabel.repaint();
	}

	/**
	 * Updates the level with a new value
	 * 
	 * @param level the value of the level to be displayed
	 */
	public void updateLevel(int level) {
		this.levelLabel.setText("" + level);
		levelLabel.repaint();
	}

	/**
	 * Updates the next shape panel in SideInfo
	 * 
	 * @param shapes the list of the shapes to be displayed
	 */
	public void updateNextShape(LinkedList<Shape> shapes) {
		for (int i = 0; i < shapes.size(); i++) {
			nextShapesPanel.updateNextShape(shapes);
		}
		nextShapesPanel.repaint();
	}

	/**
	 * Handles updating next-shape sequence after update in GameEngine
	 * 
	 * @param evt the event triggering update
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		String property = evt.getPropertyName();
		if (property.equals("next shape")) {
			updateNextShape((LinkedList<Shape>) evt.getNewValue());
		}

		if (property.equals("time")) {
			updateTime((int) evt.getNewValue());
		}

		if (property.equals("level")) {
			updateLevel((int) evt.getNewValue());
		}

		if (property.equals("points")) {
			updateScore((int) evt.getNewValue());
		}

		if (property.equals("lines cleared")) {
			updateLines((int) evt.getNewValue());
		}
	}
}
