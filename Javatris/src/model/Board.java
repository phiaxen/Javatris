package model;

/**
 * THIS CLASS IS A MODEL
 * Board is a (singleton) class that creates the 20x10 board in the game.
 * 
 * @author Philip
 * @version 1.0
 * @since 2020-02-13
 */

public class Board {
	
	private int[][] board; //2D-array of the board (standard: 20x10)
	
	private final int HEIGHT = 20;	//Heigth of the game-board
	private final int WIDTH = 10;	//Width of the game-board
	
	private static Board single_instance = null; //static variable single_instance of type Board 
	
	/**
	 * Creates an instance of Board class if it has not been done before
	 * @return single_instance : returns a single instance of Board
	 */
    public static synchronized Board getInstance() 
    { 
        if (single_instance == null) {
        	single_instance = new Board(); 
        }
        return single_instance; 
    } 
    
	/**
	 * Creates a board
	 */
	private Board() {
		board = new int[HEIGHT][WIDTH];
	}
	
	/**
	 * Puts static shapes on the board.
	 * Numbers between 1-7(the colors) are added to the board's 2D array.
	 * @param shape : the coordinates of a shape
	 * @param x : the x-position of the shape
	 * @param y : the y-position of the shape
	 * @param color : the color of the shape
	 */
	public void setStaticShapeInBoard(int[][] shape,int x,int y, int color) {
		for(int i=0; i<shape.length; i++) {
			for(int j=0; j<shape[i].length; j++) {
				if(shape[i][j] != 0) {
					board[y+i][x+j] = color;
				}
			}
		}
	}
	
	/**
	 * Moves rows up 
	 */
	private void moveRowsUp() {
		for(int i=1; i<20; i++) {
			for(int j=0; j<board[0].length; j++) {
				board[i-1][j] = board[i][j];
			}
		}
	}
	
	/**
	 * Moves rows down 
	 * @param row : the deleted row, all rows over this row will be moved down
	 */
	public void moveRowsDown(int row) {
		for(int i=row-1; i>=0; i--) {
			for(int j=0; j<board[0].length; j++) {
				board[i+1][j] = board[i][j];
			}
		}
	}
	
	/**
	 * Adds a row and moves all other rows up
	 * @param column : the column that should not have a block
	 * @param color  : the color of each block in the row
	 */
	public void addRow(int column, int color) 
	{
		System.out.println("ADD ROW");
		moveRowsUp();
		if(column >= 0 && column < 10) 
		{
			for(int i = 0; i < 10; i++) 
			{
				if(i != column)
				board[19][i] = color;
			}
		}
	}
	
	/**
	 * Removes one row and moves all other rows down
	 * @param row : the row to be removed from the board
	 */
	public void deleteRow(int row) {
		for(int i = 0; i < board[0].length; i++) {
			board[row][i] = 0;
		}
		moveRowsDown(row);
	}
	
	/**
	 * Checks if the board contains a full row, if so, delete the row and return the amount of rows deleted.
	 * @return rowsDeleted : This returns the amount of rows deleted
	 */
	public int checkFullRows() {
		
		int rows = board.length;
		int cols = board[0].length;
		int rowsDeleted = 0;
		boolean fullRow = true;
		
		for(int i = 0; i <rows; i++ ) {
			for(int j = 0; j < cols; j++) { 
				if(board[i][j] == 0) {
						
						fullRow = false;
				}
			}
			
			if(fullRow) {
				deleteRow(i);
//					if(mulitplayer) 
//					{
//						client.sendInt(6);
//					}
				rowsDeleted++;
			}
			
			fullRow= true;
		}
		
		return rowsDeleted;

	}
	
	//check one row
	public boolean checkFullRow(int row) {
		int cols = board[0].length;
		boolean fullRow = true;
		boolean deleted = false;
		
			for(int j = 0; j < cols; j++) { 
				if(board[row][j] == 0) {
						
						fullRow = false;
				}
			}
			
			if(fullRow) {
				deleteRow(row);
				deleted = true;
			}
			return deleted;
	}

	/**
	 * @return int[][] : This returns the board
	 */
	public int[][] getBoard() {
		return board;
	}
}
