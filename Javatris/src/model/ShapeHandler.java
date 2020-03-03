
package model;

import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Handles the generation of shapes
 * @author Joachim Antfolk
 * @version 1.0
 */
public class ShapeHandler extends AbstractModel{
	
	private final Board board;
	
	private Shape shapes[] = new Shape[7];	//An array that contains 7 different shapes
	
	private LinkedList<Shape> nextShapes;
	private LinkedList<Shape> oldShapes;
	
	public ShapeHandler(Board board) {
		this.board = board;
		nextShapes = new LinkedList<Shape>();
		oldShapes = new LinkedList<Shape>();
		setFirstShape();
	}
	
	/**
	 * Returns shape
	 * @param shape : the number of the shape to get
	 * @return : the selected shape
	 */
	private Shape getShape(int shape) {
		switch(shape){
		case 0: return new Shape(board,2, new int[][] {
			{1,1,1,1}}); //I
		case 1: return new Shape(board,3,new int[][] {
			{1,1,0},
			{0,1,1}}); //Z
		case 2: return new Shape(board,4,new int[][] {
			{0,1,1},
			{1,1,0}}); //S
		case 3: return new Shape(board,5,new int[][] {
			{0,0,1},
			{1,1,1}}); //L
		case 4: return new Shape(board,6,new int[][] {
			{1,1,1},
			{0,0,1}}); //J
		case 5: return new Shape(board,7,new int[][] {
			{1,1,1},
			{0,1,0}}); //T
		case 6: return new Shape(board,8,new int[][] {
			{1,1},
			{1,1}}); //Box 
		default: return null;
		}	
	}
	
	/**
	 * Returns the next shape in the list and updates the list of shapes. Notifies any listeners
	 * @return : the next shape
	 */
	public Shape nextShape() {
		Shape nextShape;
		
		nextShape = nextShapes.pollFirst();
		int randomNum = ThreadLocalRandom.current().nextInt(0, shapes.length);
		nextShapes.addLast(getShape(randomNum));
		updateListeners();
		return nextShape;
	}
	
	/*
	 * this function is called only when the game starts, only once
	 */
	private void setFirstShape() {
		int randomNum;
		for(int i = 0; i < 3; i++) {
			randomNum = ThreadLocalRandom.current().nextInt(0, shapes.length);
			nextShapes.add(i, getShape(randomNum));
		}
		updateListeners();
	}
	
	/**
	 * Notifies any listeners of current state 
	 */
	public void updateListeners() {
		firePropertyChange("next shape", oldShapes, nextShapes);
	}
	
	/**
	 * Gets the current list of shapes
	 * @return
	 */
	public LinkedList<Shape> getNextShapes()
	{
		return nextShapes;
	}
	
	/**
	 * Sets the list of shapes as param
	 * @param shapes : New list of shapes
	 */
	public void setNextShapes(LinkedList<Shape> shapes) 
	{
		nextShapes = new LinkedList<Shape>();
		for(Shape shape: shapes) 
		{
			nextShapes.addFirst(shape);
		}
	}
}