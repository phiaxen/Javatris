package model;

import java.io.Serializable;
import java.util.Arrays;

/**
 * The shape class contains all the information of shape, such as it's shape in
 * an array, it's color and it's position. It also has functions to modify and
 * control the shape, such as rotating it or moving it left or right.
 * 
 * @author Philip Axenhamn
 * @version 1.0
 */
public class Shape implements Cloneable, Serializable {

	private static final long serialVersionUID = 2L;
	private Board board;
	private int[][] shape;
	private int x, y;
	public int currentSpeed, normalSpeed = 700, fastSpeed = 40;
	private int deltaX = 0;
	private final int startPos = 4;
	private int color;

	public boolean CanMoveX = true;
	public boolean CanRotate = true;
	public boolean atBottom = false;
	private boolean hasCollidedY = false;
	private boolean hasCollidedX = false;

	public Shape(Board board, int color, int[][] positions) {
		this.board = board;
		this.color = color;
		this.shape = positions;
		x = startPos;
		currentSpeed = normalSpeed;
	}

	/**
	 * If the CanRotate flag is true shape rotates anti-clockwise in 90 degrees.
	 */
	public void rotate() {
		if (!CanRotate) {
			return;
		}
		int rows = shape.length;
		int cols = shape[0].length;
		int[][] Transposed = new int[cols][rows];

		// Transpose
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				Transposed[j][i] = shape[i][j];
			}
		}

		// Flip rows
		int[] temp = Transposed[0];
		Transposed[0] = Transposed[cols - 1];
		Transposed[cols - 1] = temp;

		int tempx = x;

		// Before setting the rotation on the shape, check if rotation is possible.
		if ((x + deltaX + Transposed[0].length > 10) || (x + deltaX < 0)) {

			while (tempx + deltaX < 0) {
				tempx++;
			}

			while (tempx + deltaX + Transposed[0].length > 10) {
				tempx--;
			}
		}

		for (int i = 0; i < Transposed.length; i++) {
			for (int j = 0; j < Transposed[0].length; j++) {
				if (Transposed[i][j] != 0) {
					if ((y + i) > 19) {
						return;
					}
					if ((board.getBoard()[y + i][j + tempx + deltaX] != 0)) {
						return;
					}
				}
			}
		}

		x = tempx;
		shape = Transposed;
	}

	/**
	 * Controls that the shape is not outside of the board on the right side
	 */
	public boolean rightBound() {
		if (x + shape[0].length == 10) {
			return true;
		}
		return false;
	}

	/**
	 * Controls that the shape is not outside of the board on the left side
	 */
	public boolean leftBound() {
		if (x == 0) {
			return true;
		}
		return false;
	}

	/**
	 * Moves the shape one unit down
	 */
	public void moveDown() {
		y++;
	}

	/**
	 * Moves the shape one unit either left or right
	 */
	public void moveDeltaX() {
		x += deltaX;
	}

	/**
	 * Sets the boolean variable hasCollidedY to the value of the parameter
	 * 
	 * @param y : the new value of hasCollidedY
	 */
	public void setCollidedY(boolean y) {
		hasCollidedY = y;
	}

	/**
	 * Sets the boolean variable hasCollidedX to the value of the parameter
	 * 
	 * @param x : the new value of hasCollidedX
	 */
	public void setCollidedX(boolean x) {
		hasCollidedX = x;
	}

	/**
	 * Sets the int variable deltaX to the value of the parameter
	 * 
	 * @param deltaX the horizontal direction the shape is moving in
	 */
	public void setDeltaX(int deltaX) {
		this.deltaX = deltaX;
	}

	/**
	 * Sets the currentSpeed to the value of fastSpeed, used to make the blocks fall
	 * faster when pressing down arrow.
	 */
	public void fasterSpeedDown() {
		currentSpeed = fastSpeed;
	}

	/**
	 * Sets the currentSpeed back to the value of normalSpeed is used when the
	 * player releases the arrow down.
	 */
	public void normalSpeedDown() {
		currentSpeed = normalSpeed;
	}

	/**
	 * Changes the normalSpeed to the value of the parameter is used to increase the
	 * speed of the game.
	 * 
	 * @param speed: the new value of normalSpeed;
	 */
	public void changeNormalSpeed(int speed) {
		currentSpeed = speed;
		normalSpeed = speed;
	}

	/**
	 * Returns the value of x, which is the horizontal position of the shape
	 * 
	 * @return x horizontal position of the shape
	 */
	public int getX() {
		return x;
	}

	/**
	 * Returns the value of y, which is the vertical position of the shape
	 * 
	 * @return y vertical position of the shape
	 */
	public int getY() {
		return y;
	}

	/**
	 * Returns the value of DeltaX which is the horizontal direction the shape
	 * currently moves in
	 * 
	 * @return deltaX : the current horizontal movement of the shape
	 */
	public int getDeltaX() {
		return deltaX;
	}

	/**
	 * Returns the value of hasCollidedY which checks if the shape is colliding
	 * vertically with the board.
	 * 
	 * @return hasCollidedY : the value of the hasCollidedY flag
	 */
	public boolean hasCollidedY() {
		return hasCollidedY;
	}

	/**
	 * Returns the value of hasCollidedX which checks if the shape is colliding
	 * horizontally with the board.
	 * 
	 * @return hasCollidedX : the value of the hasCollidedX flag
	 */
	public boolean hasCollidedX() {
		return hasCollidedX;
	}

	/**
	 * return the int that represents the colour of the shape
	 * 
	 * @return colour: the int value that represents the colour of the shape
	 */
	public int getColor() {
		return color;
	}

	/**
	 * Returns the 2D array that contains the shape's shape
	 * 
	 * @return shape the 2D array with the positions of the shape
	 */
	public int[][] getCoords() {
		return shape;
	}

	/**
	 * Returns the currentSpeed of the shape
	 * 
	 * @return currentSpeed: the current speed of tha game
	 */
	public int getCurrentSpeed() {
		return currentSpeed;
	}

	/**
	 * Returns the startPos which is the horizontal position in board that shape is
	 * spawned in
	 * 
	 * @return startPos : the horizontal starting coordinate that shape is spawned
	 *         in.
	 */
	public int getStartPos() {
		return startPos;
	}

	/**
	 * Compares this with other
	 * 
	 * @param other : object to compare with
	 */
	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if ((other != null) && (this.getClass() == other.getClass())) {
			Shape temp = (Shape) other;
			if (board.equals(temp.board) && Arrays.deepEquals(this.shape, temp.shape) && (x == temp.x) && (y == temp.y)
					&& (hasCollidedY == temp.hasCollidedY) && (hasCollidedX == temp.hasCollidedX)
					&& (color == temp.color)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns deep clone of shape
	 * 
	 * @return copy : the deep clone of the shape
	 */
	@Override
	public Shape clone() {
		try {
			Shape copy = (Shape) super.clone();
			copy.board = board.clone();
			for (int i = 0; i < shape.length; i++) {
				copy.shape[i] = shape[i].clone();
			}
			return copy;
		} catch (CloneNotSupportedException e) {
			throw new InternalError();
		}
	}
}
