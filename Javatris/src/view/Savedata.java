package view;

import java.io.Serializable;
import java.util.LinkedList;

import model.Shape;

/*
 * Serializable class containing all the game data that is needed to save the current gamestate.
 */
public class Savedata implements Serializable{


	private static final long serialVersionUID = 5L;
	int board[][];
	Shape currentShape;
	LinkedList<Shape> nextShapes;
	int score;
	int time;
	int level;
	int removedRows;
	
	//hej
	public Savedata(int board[][], Shape shape, LinkedList<Shape> shapes, int score, int time, int level, int removedRows) 
	{
		this.board = board;
		currentShape = shape;
		nextShapes = new LinkedList<Shape>();
		for(Shape s: shapes) 
		{
			nextShapes.addFirst(s);
		}
		this.score = score;
		this.time = time;
		this.level = level;
		this.removedRows = removedRows;
	}
	
	
	/*
	 * sets the board to the inputed board
	 * @param board the current board of the game
	 */
	public void setBoard(int board[][]) 
	{
		this.board = board;
	}
	
	/*
	 * sets the currentShape to the inputed shape
	 * @param shape the current controllable Shape of the game
	 */
	public void setCurrentShape(Shape shape) 
	{
		
		currentShape = shape;
	}
	
	/*
	 * sets the NextShapes list to the games current NextShapes list
	 * @param shapes the current list of the next shapes in game
	 */
	public void setNextShapes(LinkedList<Shape> shapes)
	{
		nextShapes = new LinkedList<Shape>();
		for(Shape shape: shapes) 
		{
			nextShapes.addFirst(shape);
		}
	}
	
	/*
	 * sets the score to the inputed int
	 * @param score the current score of the game
	 */
	public void setScore(int score) 
	{
		this.score = score;
	}
	
	/*
	 * sets the time to the inputed int
	 * @param time the current time of the game
	 */
	public void setTime(int time) 
	{
		this.time = time;
	}
	
	/*
	 * sets the level to the inputed int
	 * @param level the current level of the game
	 */
	public void setLevel(int level) 
	{
		this.level = level;
	}
	
	/*
	 * sets the removedRows to the inputed int
	 * @param removedRows the current removedRows of the game
	 */
	public void setRemovedRows(int removedRows) 
	{
		this.removedRows = removedRows;
	}
}
