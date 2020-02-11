package model;

/**
 * THIS CLASS IS A MODEL
 * Board is a class that creates the 20x10 board in the game.
 * It has a function that can set collided shapes on the board which is later drawn by another class named BoardView. 
 * 
 * @author Philip
 * @version 1.0
 */


public class Board {
	
	// 2D-array av spelplanen 20x10
	private int[][] board;
	
	public Board(int heigth, int width) {
		board = new int[heigth][width];
	}
	
	//Sätter statiska former i spelplanen. Olika siffror mellan 1-7(motsvarar färger) sätts i spelplanens 2D-array.
	//Denna funktion kallas när en shape kolliderar, själva formen, dess position i x-/y led samt dess färg(1-7) tas som inparametrar
	public void setStaticShapeInBoard(int[][] shape,int x,int y, int color) {
		for(int i=0; i<shape.length; i++) {
			for(int j=0; j<shape[i].length; j++) {
				if(shape[i][j] != 0) {
					board[y+i][x+j] = color;
				}
			}
		}
	}
	
	//returnerar spelplanen, används av BoardView samt av GameEngine för kollisioner.
	public int[][] getBoard() {
		return board;
	}
}
