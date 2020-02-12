package model;
import server.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;
import javax.imageio.ImageIO;
import javax.swing.Timer;

import view.*;

/**
 * THIS CLASS IS A MODEL
 * GameEngine is a class that handles all the calculations in the game.
 * 
 * @author Philip
 * @version 1.0
 */
public class GameEngine implements Runnable {
 
	public static Shape currentShape;	//The shape that is currently in action
	public Shape shapes[] = new Shape[7];	//An array that contains 7 different shapes
	public Board board;	
	private BufferedImage blocks;	
	private SideInfo sideInfo;
	private Client client;
	private Boolean mulitplayer = false; //Change this to true for multiplayer 
	public int level = 0;
	public int points = 0;

	
	private boolean running = false;
	private Thread thread;
	
	private BoardView boardView;
	
	public GameEngine(Board board, BoardView boardView, SideInfo sideInfo) {
		this.board = board;
		this.boardView = boardView;
		this.sideInfo = sideInfo;
		
		if(mulitplayer) 
		{
			client = new Client(this, "127.0.0.1", 6969);
		}
	
		SpawnShape();
	}
	
	
	
	
	public void update() {
	
		
		currentShape.time += System.currentTimeMillis() - currentShape.lastTime;
		currentShape.lastTime = System.currentTimeMillis();
	
			while((currentShape.getX() + currentShape.getDX() < 0)) {
				currentShape.x++;
			}
			if(currentShape.getX() + currentShape.getDX() + currentShape.getShape()[0].length > 10) {
				while(currentShape.getX() + currentShape.getDX() + currentShape.getShape()[0].length > 10) {
					currentShape.x--;
				}
			}
			
			
			CheckCollisionY();
			CheckCollisionX();
			
			if(currentShape.hasCollidedY()) {
				setStaticShapes();
				SpawnShape();
				int rowsDeleted = 0;
				for(int i=0; i<board.getBoard().length; i++) {
					if(board.checkFullRow(i)) {
						rowsDeleted++;
						if(mulitplayer) 
							{
								client.sendInt(6);
							}
					}
				}
				
				points += scoreHandler(level,rowsDeleted);
				sideInfo.updateScore(points);
				System.out.println("points: " + points);	
				
			}
		
			if((currentShape.time > currentShape.currentSpeed)&&(!currentShape.hasCollidedY())) {
				
				currentShape.y++;
				currentShape.time = 0;
			}
			if(!currentShape.hasCollidedX()) {
					
					currentShape.x += currentShape.getDX();
					
				}
			
			currentShape.setDeltaX(0);
	}
	
	public void addRow(int column, int color) {
		board.addRow(column, color);
	}
	public void CheckCollisionX() {
		
		for(int i = 0; i<currentShape.getShape().length; i++) {
			for(int j = 0; j<currentShape.getShape()[0].length; j++) {
				if(currentShape.getShape()[i][j] !=0) {
					if(board.getBoard()[currentShape.getY()+i][currentShape.getX()+j+currentShape.getDX()] !=0) {
						currentShape.collidedX();
						return;
					}	
				}
			}
		}
		currentShape.notCollidedX();
	}
	
	public void CheckCollisionY() {
		
		if((currentShape.y + 1 + currentShape.getShape().length > 20)) {
			currentShape.collidedY();
		}else {
			for(int i=0; i<currentShape.getShape().length; i++) {
				for(int j=0; j<currentShape.getShape()[0].length; j++) {
					if(currentShape.getShape()[i][j] != 0) {
						if(board.getBoard()[currentShape.y+i+1][j+currentShape.x] != 0) {
				
							currentShape.collidedY();
							
						}
						
					}
					
				}
			}
		}
	}
	
	private Shape getShape(int shape) {
		
		switch(shape){
		case 0: return new Shape(board,1, new int[][] {
			{1,1,1,1}}); //I
		case 1: return new Shape(board,2, new int[][] {
			{1,1,0},
			{0,1,1}}); //Z
		case 2: return new Shape(board,3,new int[][] {
			{0,1,1},
			{1,1,0}}); //S
		case 3: return new Shape(board,4, new int[][] {
			{0,0,1},
			{1,1,1}}); //L
		case 4: return new Shape(board,5, new int[][] {
			{1,1,1},
			{0,0,1}}); //J
		case 5: return new Shape(board,6, new int[][] {
			{1,1,1},
			{0,1,0}}); //T
		case 6: return shapes[6]= new Shape(board,7, new int[][] {
			{1,1},
			{1,1}}); //Box 
		default: break;
		}
		return null;
		
	}
	
	public void SpawnShape() {
		
		int randomNum = ThreadLocalRandom.current().nextInt(0, shapes.length);
		currentShape = getShape(randomNum);
		
	}
	

	public void setStaticShapes() {
	
		board.setStaticShapeInBoard(currentShape.getShape(),currentShape.x,currentShape.y,currentShape.getColor());
	
	}
	
	public static Shape getCurrentShape() {
		return currentShape;
	}	
	
	private void levelUp() {
		
	}
	
	public int getLevel() {
		return level;
	}
	
	public int getPoints() {
		return points;
	}
	
	private int scoreHandler(int level, int rows) {
		switch(rows) {
			case 1: return 40*(level + 1);
			case 2: return 100*(level + 1);
			case 3: return 300*(level + 1);
			case 4: return 1200*(level + 1);
			default: return 0;
		}
		
	}

	public synchronized void start() {
		
		if(running) {
			return;
		}
		running = true;
		thread = new Thread(this);
		thread.start(); //start thread
	}

	private synchronized void stop() {
		if(!running) {
			return; 
		}
		running = false;
		try {
			thread.join();
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
		System.exit(1);
	}
	
	//This function gets called when we start the thread.
	@Override
	public void run() {
//		init();
		
		long lastTime = System.nanoTime();
		final double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double deltaTime = 0;
		int updates = 0;
		int frames = 0;
		long timer = System.currentTimeMillis();
		
		//gameloop, now the CPU-usage should not rise as much as before =)
		while(!Thread.interrupted()) {
			
			try {
				Thread.sleep(1);
			}
			catch(InterruptedException e) {
				break;
			}
			
			long now = System.nanoTime();
			deltaTime += (now - lastTime) / ns;
			lastTime = now;
			
			if(deltaTime >= 1) {
				tick();
				
				updates++;
				
				deltaTime--;
				render();
			}
			
			
			
			frames++;
			
			if(System.currentTimeMillis() - timer > 1000){
				timer += 1000; 
				updates = 0;
				frames = 0;
				
			}
		}
		stop();
		
	}
	
	
	//everything in game that updates
	private void tick() {
		update();
	}
	
	//everything in game that renders
	private void render() {
		boardView.repaint();
	}
	
	
}
