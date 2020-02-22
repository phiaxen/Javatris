package model;
import server.*;
import server.ConnectionHandler.Delegate;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import java.util.LinkedList;
import java.util.Timer;

import view.*;

/**
 * THIS CLASS IS A MODEL
 * GameEngine is a class that handles all the calculations in the game.
 * 
 * @author Philip
 * @version 1.0
 */
public class GameEngine implements Runnable {
	
	
	/*
	 * Interface that handles the communication with the game engine.
	 */
	public interface Delegate {
		Client getClient();
	}
	
	private static Shape currentShape;	//The shape that is currently in action
	private Shape shapes[] = new Shape[7];	//An array that contains 7 different shapes
	private Board board;	
	private SideInfo sideInfo;
	private Client client;
	private Boolean online = false; //Change this to true for multiplayer 
	
	//points and levelup things
	public int level = 1;
	public int points = 0;
	private int linesToClear = 10;	//how many lines it takes to level up, increase by 5 for each level
	
	private int linesCleared = 0;	//how many lines the player has cleared
	public Delegate delegate;

	private boolean running = false;
	public boolean paused = false; 
	private Thread thread;
	public final int TICKSPERSECOND = 60;
	
	private int timePassed = 0;
	private Timer GameTime;
	
	private BoardView boardView;
	
	private long time, lastTime;
	
	private boolean gameOver = false;
	
	private LinkedList<Shape> nextShapes = new LinkedList<Shape>(); 

	private SfxManager sfxManager;
	
	public GameEngine(Board board, BoardView boardView, SideInfo sideInfo, Boolean online) {
		this.board = board;
		this.boardView = boardView;
		this.sideInfo = sideInfo;
		this.online = online;
		setFirstShape();
		GameTime = new Timer();
		this.sfxManager = new SfxManager();
	}
	
	TimerTask task = new TimerTask() {
		public void run() {
			timePassed++;
			sideInfo.updateTime(timePassed);
		}
		
	};
	
	
	public void update() {
//		System.out.println("TIME: " + timePassed);
		checkIfGameOver();
		time += System.currentTimeMillis() - lastTime;
		lastTime = System.currentTimeMillis();
			
			CheckCollisionX();
			CheckCollisionY();
			
			if(currentShape.hasCollidedY()) {
				
				sfxManager.playSound2();
				setStaticShapes();
				
				int rowsDeleted = 0;
				int column = 0;
				for(int i=0; i<board.getBoard().length; i++) {
					if(board.checkFullRow(i)) {
						
						rowsDeleted++;
						linesCleared++;
						if(online) 
							{
								client.sendInt(currentShape.getX());
							}
					}
				}
				
				
				//uppdaterar score endast om rader har tagits bort
				if(rowsDeleted > 0) {
					
					if(linesCleared >= linesToClear) {
						levelUp();
					}
					else {
						sfxManager.playSound3();
					}
					points += scoreHandler(level,rowsDeleted);
					sideInfo.updateScore(points);
					sideInfo.updateLines(linesCleared);
					sideInfo.updateLevel(level);
					
					System.out.println("points: " + points);
					System.out.println("lines: " + linesCleared);
				}	
				setCurrentShape();
			}
		
			if((time > currentShape.getCurrentSpeed())&&(!currentShape.hasCollidedY())) {
				
				currentShape.moveDown();
				time = 0;
			}
			if(!currentShape.hasCollidedX()) {
					
					currentShape.moveDeltaX();
					
				}
			
			currentShape.setDeltaX(0);
	}
	
	public void addRow(int column, int color) {
		board.addRow(column, color);
	}
	
	//not bug free
	public void CheckCollisionX() {
		
		for(int i = 0; i<currentShape.getCoords().length; i++) {
			for(int j = 0; j<currentShape.getCoords()[0].length; j++) {
				if(currentShape.getCoords()[i][j] !=0) {
					if(board.getBoard()[currentShape.getY()+i][currentShape.getX()+j+currentShape.getDeltaX()] !=0) {
						currentShape.collidedX();
						return;
					}	
				}
			}
		}
		currentShape.notCollidedX();
	}
	
	public void CheckCollisionY() {
		
		if((currentShape.getY() + 1 + currentShape.getCoords().length > 20)) {
			currentShape.collidedY();
		}else {
			for(int i=0; i<currentShape.getCoords().length; i++) {
				for(int j=0; j<currentShape.getCoords()[0].length; j++) {
					if(currentShape.getCoords()[i][j] != 0) {
						if(board.getBoard()[currentShape.getY()+i+1][j+currentShape.getX()] != 0) {
				
							currentShape.collidedY();
							
						}
						
					}
					
				}
			}
		}
	}
	

	
	private Shape getShape(int shape) {
		
		switch(shape){
		case 0: return new Shape(board,2, new int[][] {
			{1,1,1,1}}); //I
		case 1: return new Shape(board,3, new int[][] {
			{1,1,0},
			{0,1,1}}); //Z
		case 2: return new Shape(board,4,new int[][] {
			{0,1,1},
			{1,1,0}}); //S
		case 3: return new Shape(board,5, new int[][] {
			{0,0,1},
			{1,1,1}}); //L
		case 4: return new Shape(board,6, new int[][] {
			{1,1,1},
			{0,0,1}}); //J
		case 5: return new Shape(board,7, new int[][] {
			{1,1,1},
			{0,1,0}}); //T
		case 6: return shapes[6]= new Shape(board,8, new int[][] {
			{1,1},
			{1,1}}); //Box 
		default: break;
		}
		return null;
		
	}
	
	//this function is called only when the game starts, only once
	private void setFirstShape() {
		int randomNum = ThreadLocalRandom.current().nextInt(0, shapes.length);
		currentShape = getShape(randomNum);
		
		//When the game starts, fill the list with 3 random shapes
		for(int i = 0; i < 3; i++) {
			randomNum = ThreadLocalRandom.current().nextInt(0, shapes.length);
			nextShapes.add(getShape(randomNum));
			sideInfo.updateNextShape(nextShapes);
			sideInfo.repaint();
		}
	}
	
	public void setCurrentShape() {
		currentShape = nextShape();
	}
	
	private Shape nextShape() {
		
		
		Shape nextShape;
		
		nextShape = nextShapes.pollFirst();
		int randomNum = ThreadLocalRandom.current().nextInt(0, shapes.length);
		nextShapes.addLast(getShape(randomNum));
		
		for(int i = 0; i < 3; i++) {
			sideInfo.updateNextShape(nextShapes);
			sideInfo.repaint();
		}
		return nextShape;
	}
	
//	public void checkIfGameOver() {
//		int rows = currentShape.getCoords().length;
//		int cols =  currentShape.getCoords()[0].length;
//		int startPos = currentShape.getStartPos();
//		
//		for(int i = 0; i < rows; i++){
//			for(int j = 0; j <cols; j++ ) {
//				if(board.getBoard()[i][j + startPos] !=0) {
//					gameOver = true;
//					System.out.println("GAME OVER =(");
//				}
//			}
//			
//		}
//	}

	public void checkIfGameOver(){
		for(int i = 0; i <10; i++ ) {
			if(board.getBoard()[0][i] !=0) {
				gameOver = true;
				System.out.println("GAME OVER =(");
			}
		}
	}
	public void setStaticShapes() {
	
		board.setStaticShapeInBoard(currentShape.getCoords(),currentShape.getX(),currentShape.getY(),currentShape.getColor());
	}
	
	public Shape getCurrentShape() {
		return currentShape;
	}	
	
	private void levelUp() {
			sfxManager.playSound4();
			level++;
			linesToClear = linesToClear + 5;
	}
	
	public int getLevel() {
		return level;
	}
	
	public int getPoints() {
		return points;
	}
	
	private int scoreHandler(int level, int rows) {
		switch(rows) {
			case 1: return 40*level;
			case 2: return 100*level;
			case 3: return 300*level;
			case 4: return 1200*level;
			default: return 0;
		}
		
	}

	public synchronized void start() {
		System.out.println("GAME START");
		boardView.setCurrentShape(currentShape);
		if(running) {
			return;
		}
	
		running = true;
		
		if(!online) 
		{
			GameTime.scheduleAtFixedRate(task, 0, 1000);
			thread = new Thread(this);
			thread.start(); //start thread
		}else {
			if(this.delegate != null) 
			{
				client = this.delegate.getClient();
				
			}
		}
		
	}
	
	public synchronized void startOnline() 
	{
		thread = new Thread(this);
		thread.start();
	}
	
	public void pause() {
		paused = true;
	}
	
	public void resume() {
		synchronized(thread) {
			paused = false;
			thread.notify();
		}
		
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
		
		long lastTime = System.nanoTime();
		double ns = 1000000000 / TICKSPERSECOND;
		double deltaTime = 0;
		int updates = 0;
		int frames = 0;
		long fpsTimer = System.currentTimeMillis();
		
		//gameloop, now the CPU-usage should not rise as much as before =)
		while(!Thread.interrupted() ) {
			
			if(paused) {
				try {
					synchronized(thread) {
						thread.wait();
					}
				}catch(InterruptedException e){
					e.printStackTrace();
				}
			}else {
				try {
					Thread.sleep(1); //sleep in 1ms
				}
				catch(InterruptedException e) {
					break;
				}
				
				long now = System.nanoTime();
				deltaTime += (now - lastTime) / ns;
				lastTime = now;
				
				
				if(deltaTime >= 1) {
					tick();
//					System.out.println(deltaTime);
					updates++;
					
					deltaTime--;
					render();
				}
				
				
				
				frames++;
				
				//debug
//				if(System.currentTimeMillis() - fpsTimer > 1000){
//					fpsTimer += 1000; 
//					System.out.println(updates + " Ticks, FPS " + frames);
//					updates = 0;
//					frames = 0;
//					
//				}
			}
			
			
		}
		stop();
		
	}
	
	
	//everything in game that updates
	private void tick() {
		if(!gameOver)
		update();
	}
	
	//everything in game that renders
	private void render() {
		boardView.setCurrentShape(currentShape);
		boardView.repaint();
		
	}
	
	public void quit() 
	{
		System.exit(1);
	}
	
	
}
