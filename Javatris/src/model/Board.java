package model;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Board is a class that contains a 2D array of integers representing the board of the game.
 * It also has functions to modify it like adding rows, removing rows and adding shapes to the array..
 * 
 * @author Philip Axenhamn
 * @author Joachim Antfolk
 * @since 2020-03-08
 * @version 1.0
 */
public class Board implements Cloneable, Serializable {

	
	private static final long serialVersionUID = 1L;
	private int[][] board; // 2D-array of the board (standard: 20x10)
	private final int HEIGHT = 20; // Heigth of the game-board
	private final int WIDTH = 10; // Width of the game-board

	/**
	 * Creates a board using the value of HEIGHT and WIDTH
	 */
	public Board() {
		board = new int[HEIGHT][WIDTH];
	}

	/**
	 * Puts static shapes on the board. Numbers between 1-7(the colors) are added to
	 * the board's 2D array.
	 * 
	 * @param shape the coordinates of a shape
	 * @param x the x-position of the shape
	 * @param y the y-position of the shape
	 * @param color the color of the shape
	 */
	public void setStaticShapeInBoard(int[][] shape, int x, int y, int color) {
		for (int i = 0; i < shape.length; i++) {
			for (int j = 0; j < shape[i].length; j++) {
				if (shape[i][j] != 0) {
					board[y + i][x + j] = color;
				}
			}
		}
	}

	/**
	 * Moves rows up
	 */
	private void moveRowsUp() {
		for (int i = 1; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				board[i - 1][j] = board[i][j];
				board[i][j] = 0;
			}
		}
	}

	/**
	 * Moves rows down
	 * @param row the index for the deleted row, all rows over this row will be moved down
	 */
	public void moveRowsDown(int row) {
		for (int i = row - 1; i >= 0; i--) {
			for (int j = 0; j < board[0].length; j++) {
				board[i + 1][j] = board[i][j];
			}
		}
	}

	/**
	 * Adds a row and moves all other rows up
	 * @param column the column that should not have a block
	 * @param color the color of each block in the row
	 */
	public void addRow(int column, int color) {
		moveRowsUp();
		if (column >= 0 && column < 10) {
			for (int i = 0; i < 10; i++) {
				if (i != column)
					board[board.length - 1][i] = color;
			}
		}
	}

	/**
	 * Removes one row and moves all other rows down
	 * 
	 * @param row the row to be removed from the board
	 */
	public void deleteRow(int row) {
		for (int i = 0; i < board[0].length; i++) {
			board[row][i] = 0;
		}
		moveRowsDown(row);
	}

	/**
	 * Checks if the board contains a full row, if so, delete the row
	 * 
	 * @param row the row to be checked
	 * @return deleted true if a row was deleted, otherwise false
	 */
	public boolean checkFullRow(int row) {
		int cols = board[0].length;
		boolean fullRow = true;
		boolean deleted = false;

		for (int j = 0; j < cols; j++) {
			if (board[row][j] == 0) {

				fullRow = false;
			}
		}

		if (fullRow) {
			deleteRow(row);
			deleted = true;
		}
		return deleted;
	}

	/**
	 * @return board returns this board
	 */
	public int[][] getBoard() {
		return board;
	}

	/**
	 * Returns deep copy of Board object
	 * @return Board deep copy of this Board
	 */
	@Override
	public Board clone() {
		try {
			int[][] boardClone = new int[HEIGHT][WIDTH];

			Board copy = (Board) super.clone();
			for (int i = 0; i < HEIGHT; i++) {
				boardClone[i] = board[i].clone();
			}

			copy.board = boardClone;
			return copy;
		} catch (CloneNotSupportedException e) {
			throw new InternalError();
		}
	}

	/**
	 * Compares this object with parameter
	 * @param other Object to be compared to
	 */
	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if ((other != null) && (this.getClass() == other.getClass())) {
			Board temp = (Board) other;
			if ((temp.HEIGHT == HEIGHT) && (temp.WIDTH == WIDTH) && (Arrays.deepEquals(this.board, temp.board))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Puts a board on this board
	 * @param board the board to be put on this board
	 */
	public void setBoard(int[][] board) {
		this.board = board;
	}

	/**
	 * Resets board to init state
	 */
	public void resetBoard() {
		for (int i = 0; i < HEIGHT; i++) {
			for (int j = 0; j < WIDTH; j++) {
				board[i][j] = 0;
			}
		}
	}
}
