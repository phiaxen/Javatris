package model;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * Serializable class containing all the game data that is needed to save the
 * current gamestate.
 * 
 * @author Andreas Greppe
 * @version 1.0
 * 
 * @author Joachim Antfolk
 * @version 2.0
 */
public class Savedata implements Serializable {

	private static final long serialVersionUID = 5L;
	int board[][];
	Shape currentShape;
	LinkedList<Shape> nextShapes;
	int time;
	int score;
	int level;
	int removedRows;
	int linesToClear;

	/**
	 * sets the board to the inputed board
	 * 
	 * @param board the current board of the game
	 */
	public void setBoard(int board[][]) {
		this.board = board;
	}

	/**
	 * Returns the saved board
	 * 
	 * @return the saved Board
	 */
	public int[][] getBoard() {
		return board;
	}

	/**
	 * sets the currentShape to the inputed shape
	 * 
	 * @param shape the current controllable Shape of the game
	 */
	public void setCurrentShape(Shape shape) {

		currentShape = shape;
	}

	/**
	 * Returns the saved currentShape
	 * 
	 * @return the currentShape
	 */
	public Shape getCurrentShape() {

		return currentShape;
	}

	/**
	 * sets the NextShapes list to the games current NextShapes list
	 * 
	 * @param shapes the current list of the next shapes in game
	 */
	public void setNextShapes(LinkedList<Shape> shapes) {
		nextShapes = new LinkedList<Shape>();
		for (Shape shape : shapes) {
			nextShapes.addFirst(shape);
		}
	}

	public LinkedList<Shape> getNextShapes() {
		return nextShapes;
	}

	/**
	 * sets the score to the inputed int
	 * 
	 * @param score the current score of the game
	 */
	public void setScore(int score) {
		this.score = score;
	}

	/**
	 * sets the time to the inputed long
	 * 
	 * @param time the current time of the game
	 */
	public void setTime(int time) {
		this.time = time;
	}

	/**
	 * sets the level to the inputed int
	 * 
	 * @param level the current level of the game
	 */
	public void setLevel(int level) {
		this.level = level;
	}

	/**
	 * sets the removedRows to the inputed int
	 * 
	 * @param removedRows the current removedRows of the game
	 */
	public void setRemovedRows(int removedRows) {
		this.removedRows = removedRows;
	}

	/**
	 * Gets the saved score
	 * 
	 * @return the score of saved game
	 */
	public int getScore() {

		return score;
	}

	/**
	 * Gets the saved difficulty level
	 * 
	 * @return the level of saved game
	 */
	public int getLevel() {

		return level;
	}

	/**
	 * Gets the saved removed rows
	 * 
	 * @return the removed rows of saved game
	 */
	public int getRemovedRows() {

		return removedRows;
	}

	/**
	 * Gets the saved time
	 * 
	 * @return the time of saved game
	 */
	public int getTime() {
		return time;
	}
}