package model;
import java.awt.image.BufferedImage;

/**
 * 
 * @author Philip
 * @version 1.0
 */


/*
 * En Shape har följande egenskaper:
 * 1. x- och y position
 * 2. En 2D-array för själva formen, där 1 = ifylld och 0 = transparent
 * 3. Färg, en int mellan 1-7 som motsvarar de olika färgerna
*/

public class Shape {

	private Board board;
	private int[][] shape;
	private int x,y;
	public long time, lastTime;
	public int currentSpeed,normalSpeed = 600, fastSpeed = 60;
	private int deltaX = 0;

	public boolean CanMoveX = true;
	public boolean CanRotate = true;
	public boolean atBottom = false;
	private boolean hasCollidedY = false;
	private boolean hasCollidedX = false;
	
	private int color;
	
	public Shape(Board board,int color, int[][] positions) {
		
		this.board = board;
		this.color = color;
		this.shape = positions;
		
		x = 3;
		time = 0;
		currentSpeed = normalSpeed;
		lastTime = System.currentTimeMillis();
	}
	

	//Rotate the shape anti-clockwise.
	public void rotate() {
		if(!CanRotate) {
			return;
		}
		int rows = shape.length;
		int cols = shape[0].length;
		int [][] Transposed = new int[cols][rows];
		
		//Transpose
		for(int i = 0; i < rows; i++) {
				for(int j = 0; j < cols; j++) {
					Transposed[j][i] = shape[i][j];
			}
		}
		
		//Flip rows
		int[] temp = Transposed[0];
		Transposed[0] = Transposed[cols - 1];
		Transposed[cols - 1] = temp;
		
		//Before setting the rotation on the shape, check if rotation is possible. Ska kanske vara i GameEngine
		if((x+deltaX+Transposed[0].length > 10)||(x + deltaX < 0)) {
			
			while(x + deltaX < 0) {
				x++;
			}
			
			while(x+deltaX+Transposed[0].length > 10) {
				x--;
			}
		}else {
			for(int i=0; i<Transposed.length; i++) {
				for(int j=0; j<Transposed[0].length; j++) {
					if(Transposed[i][j] != 0) {
						if(board.getBoard()[y+i][j+x+deltaX] != 0) {
							return;
						}
					}
				}
			}
		}
		shape = Transposed;
	}
	
	public void moveLeft() {
		x--;
	}
	
	public void moveRight() {
		x++;
	}
	
	public void moveDown() {
		y++;
	}
	
	public void moveDeltaX() {
		x+= deltaX;
	}
	
	//provisorisk lösning:
	
	//setters
	public void collidedY() {
		hasCollidedY = true;
	}
	
	public void collidedX() {
		hasCollidedX = true;
	}
	public void notCollidedX() {
		hasCollidedX = false;
	}
	public void setDeltaX(int deltaX) {
		this.deltaX = deltaX;
	}
	
	
	public void fasterSpeedDown() {
		currentSpeed = fastSpeed;
	}
		
	
	public void normalSpeedDown() {
		currentSpeed = normalSpeed;
	}
	
	
	//getters
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public int getDeltaX() {
		return deltaX;
	}
	public boolean hasCollidedY() {
		return hasCollidedY;
	}
	public boolean hasCollidedX() {
		return hasCollidedX;
	}
	public int getColor() {
		return color;
	}
	public int[][] getCoords() {
		return shape;
	}
	
}
