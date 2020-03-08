
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
	 * @param shape the number of the shape to get
	 * @return the selected shape
	 */
	private Shape getShape(int shape) {
		switch(shape){
		case 0: return new Shape(2, new int[][] {
			{1,1,1,1}}); //I
		case 1: return new Shape(3,new int[][] {
			{1,1,0},
			{0,1,1}}); //Z
		case 2: return new Shape(4,new int[][] {
			{0,1,1},
			{1,1,0}}); //S
		case 3: return new Shape(5,new int[][] {
			{0,0,1},
			{1,1,1}}); //L
		case 4: return new Shape(6,new int[][] {
			{1,1,1},
			{0,0,1}}); //J
		case 5: return new Shape(7,new int[][] {
			{1,1,1},
			{0,1,0}}); //T
		case 6: return new Shape(8,new int[][] {
			{1,1},
			{1,1}}); //Box 
		default: return null;
		}	
	}
	
	/**
	 * Returns the next shape in the list and updates the list of shapes. Notifies any listeners
	 * @return the next shape
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
	 * Rotates current shape anti-clockwise in 90 degrees.
	 */
	public void rotate(Shape currentShape) {
		
		int rows = currentShape.getCoords().length;
		int cols = currentShape.getCoords()[0].length;
		int[][] Transposed = new int[cols][rows];

		// Transpose
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				Transposed[j][i] = currentShape.getCoords()[i][j];
			}
		}

		// Flip rows
		int[] temp = Transposed[0];
		Transposed[0] = Transposed[cols - 1];
		Transposed[cols - 1] = temp;

	
		int y = currentShape.getY();
		int tempx = currentShape.getX();
		int dx = currentShape.getDeltaX();
		// Before setting the rotation on the shape, check if rotation is possible.
		if ((tempx + dx + Transposed[0].length > 10) || (tempx + dx < 0)) {

			while (tempx + dx < 0) {
				tempx++;
			}

			while (tempx + dx + Transposed[0].length > 10) {
				tempx--;
			}
		}

		for (int i = 0; i < Transposed.length; i++) {
			for (int j = 0; j < Transposed[0].length; j++) {
				if (Transposed[i][j] != 0) {
					if ((y + i) > 19) {
						return;
					}
					if ((board.getBoard()[y + i][j + tempx + dx] != 0)) {
						return;
					}
				}
			}
		}

		currentShape.setX(tempx);
		currentShape.setCoordianates(Transposed);
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
	 * @param shapes New list of shapes
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