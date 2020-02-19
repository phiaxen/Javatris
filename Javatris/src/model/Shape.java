package model;
import java.awt.image.BufferedImage;

/**
 * 
 * @author Philip
 * @version 1.0
 */


public class Shape {

	private Board board;
	private int[][] shape;
	private int x,y;
	public int currentSpeed,normalSpeed = 600, fastSpeed = 60;
	private int deltaX = 0;
	private final int startPos = 4;

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
		x = startPos;
		currentSpeed = normalSpeed;
		
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
		
		int tempx = x;
		
		//Before setting the rotation on the shape, check if rotation is possible. Ska kanske vara i GameEngine
		if((x+deltaX+Transposed[0].length > 10)||(x + deltaX < 0)) {
			
			while(tempx + deltaX < 0) {
				tempx++;
			}
			
			while(tempx +deltaX+Transposed[0].length > 10) {
				tempx--;
			}
		}
			for(int i=0; i<Transposed.length; i++) {
				for(int j=0; j<Transposed[0].length; j++) {
					if(Transposed[i][j] != 0) {
						
						if((board.getBoard()[y+i][j+tempx+deltaX] != 0)) { 
							return;
						}
					}
				}
			}
		
		x = tempx;
		shape = Transposed;
	}
	
	public boolean rightBound() {
		if(x + shape[0].length == 10) {
			return true;
		}
		return false;
	}
	
	public boolean leftBound() {
		if(x == 0) {
			return true;
		}
		return false;
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
	public int getCurrentSpeed() {
		return currentSpeed;
	}
	public int getStartPos() {
		return startPos;
	}
	
}
