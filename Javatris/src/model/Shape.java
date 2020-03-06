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
public class Shape implements Serializable {

	private static final long serialVersionUID = 2L;
	private int[][] shape;
	private int x, y;
	public int currentSpeed, normalSpeed = 700, fastSpeed = 40;
	private int deltaX = 0;
	private final int startPos = 4;
	private int color;
	private boolean hasCollidedY = false;
	private boolean hasCollidedX = false;

	public Shape(int color, int[][] positions) {
		this.color = color;
		this.shape = positions;
		x = startPos;
		currentSpeed = normalSpeed;
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
			if (Arrays.deepEquals(this.shape, temp.shape) && (x == temp.x) && (y == temp.y)	
					&& (hasCollidedY == temp.hasCollidedY) && (hasCollidedX == temp.hasCollidedX) 
					&& (color == temp.color) && (currentSpeed == temp.currentSpeed) && (normalSpeed == temp.normalSpeed)){
				return true;
			}
		}
		return false;
	}

	/**
	 * Changes the x-position of the shape
	 * 
	 * @param x : the x-position 
	 */
	public void setX(int x) {
		this.x = x;

	}

	/**
	 * Changes the 2D-array of the shape, used for rotation
	 * 
	 * @param coords : the new 2D-array
	 */
	public void setCoordianates(int[][] coords) {
		this.shape = coords;
	}
}
