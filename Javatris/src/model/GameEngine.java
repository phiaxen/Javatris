package model;
import server.*;
import server.ConnectionHandler.Delegate;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import java.util.LinkedList;
import java.util.Timer;

/**
 * GameEngine is a class that handles all the calculations in the game.
 * @author Philip
 * @author Joachim Antfolk
 * @version 1.0
 * @since 2020-02-10
 */
public class GameEngine extends AbstractModel implements Runnable{
	
	
	/*
	 * Interface that handles the communication with the game engine.
	 */
	public interface Delegate {
		Client getClient();
		void pause();
		void resume();
	}

	private static Shape currentShape;	//The shape that is currently in action
	private static Shape oldShape;
	private Shape shapes[] = new Shape[7];	//An array that contains 7 different shapes
	private Board board;	
	private Client client;
	private Boolean online = false; //Change this to true for multiplayer 
	
	//points and levelup things
	private int level = 1;
	private int points = 0;
	private int linesToClear = 10;	//how many lines it takes to level up, increase by 5 for each level
	
	private int linesCleared = 0;	//how many lines the player has cleared
	public Delegate delegate;

	private boolean running = false;
	
	private Thread thread;
	private final int TICKSPERSECOND = 60;
	
	private int timePassed = 0;
	private int oldTime = 0;
	private Timer GameTime;
	
	private long time, lastTime;
	
	private boolean gameOver = false;
	
	private LinkedList<Shape> nextShapes = new LinkedList<Shape>();
	private LinkedList<Shape> oldShapes = new LinkedList<Shape>();
	  
	private boolean GameStart = true;

	public GameEngine(Board board, Boolean online) {
		super();
		this.board = board;
		this.online = online;
		GameTime = new Timer();
		setFirstShape();
	}
	
	
	TimerTask task = new TimerTask() {
		public void run() {
			if(running && !gameOver) {
				oldTime = timePassed;
				timePassed++;
				firePropertyChange("time", oldTime, timePassed);
			}
		}
	};
	
	
	public void update() {
		Board oldBoard = board.clone();
		
		checkIfGameOver();
		if(!gameOver) {
			time += System.currentTimeMillis() - lastTime;
			lastTime = System.currentTimeMillis();
			
			CheckCollisionX();
			CheckCollisionY();
			
			if(currentShape.hasCollidedY()) {
				setStaticShapes();
				
				int rowsDeleted = 0;
				int oldlinesC = linesCleared;
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
					levelUp();
					int oldPoints = points;
					points += scoreHandler(level,rowsDeleted);
					
					firePropertyChange("points", oldPoints, points);
					firePropertyChange("lines cleared", oldlinesC, linesCleared);
				}	
				SpawnShape();
			}
		
			if((time > currentShape.getCurrentSpeed())&&(!currentShape.hasCollidedY())) {
				
				currentShape.moveDown();
				time = 0;
			}
			if(!currentShape.hasCollidedX()) {
					
					currentShape.moveDeltaX();
					
				}
			
			currentShape.setDeltaX(0);
			
			firePropertyChange("board", oldBoard, board);
		}
	}
	
	public void addRow(int column, int color) {
		Board oldBoard = board.clone();
		board.addRow(column, color);
		firePropertyChange("board", oldBoard, board);
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
		boolean currentState = currentShape.hasCollidedY();
		
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
		firePropertyChange("collisionY", currentState, currentShape.hasCollidedY());
	}
	
	/*
	 * this function is called only when the game starts, only once
	 */
	public void setFirstShape() {
		int randomNum = ThreadLocalRandom.current().nextInt(0, shapes.length);
		currentShape = getShape(randomNum);
		
		//When the game starts, fill the list with 3 random shapes
		for(int i = 0; i < 3; i++) {
			randomNum = ThreadLocalRandom.current().nextInt(0, shapes.length);
			nextShapes.add(i, getShape(randomNum));
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
		default: return null;
		}	
	}
	
	public void SpawnShape() {
		currentShape = nextShape();
	}
	
	private Shape nextShape() {
		Shape nextShape;
	
		for(int i = 0; i < nextShapes.size(); i++) {
			oldShapes.add(i, nextShapes.get(i).clone());
		}
	
		//When the game starts, fill the list with 4 random shapes
		if(GameStart) {
			for(int i = 0; i < 3; i++) {
				int randomNum = ThreadLocalRandom.current().nextInt(0, shapes.length);
				nextShapes.add(getShape(randomNum));
				
			}
			GameStart = false;
		}
		
		nextShape = nextShapes.pollFirst();
		int randomNum = ThreadLocalRandom.current().nextInt(0, shapes.length);
		nextShapes.addLast(getShape(randomNum));

		firePropertyChange("next shape", oldShapes, nextShapes);
		
		return nextShape;
	}

	public void checkIfGameOver(){
		for(int i = 0; i <10; i++ ) {
			if(board.getBoard()[0][i] != 0) {
				gameOver = true;
				System.out.println("GAME OVER");
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
		if(linesCleared >= linesToClear) {
			int oldLevel = level;
			level++;
			linesToClear = linesToClear + 5;
			firePropertyChange("level", oldLevel, level);
		}
	}
	
	public int getLevel() {
		return level;
	}
	
	public int getPoints() {
		return points;
	}
	
	public boolean running() {
		return running;
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
		fireGameField();
		System.out.println("GAME START");
		running = true;
		if(!online) 
		{
			if(GameStart) {
				GameTime.scheduleAtFixedRate(task, 0, 1000);
				thread = new Thread(this);
				thread.start(); //start thread
			}else {
				thread.notify();
			}
			
		}else {
			if(this.delegate != null) 
			{
				client = this.delegate.getClient();
				
			}
		}
	}
	
	public synchronized void startOnline() 
	{
		GameTime.scheduleAtFixedRate(task, 0, 1000);
		thread = new Thread(this);
		thread.start();
	}
	
	public void pause() {
		if(!online) {
			running = false;
			delegate.pause();
		}
	}
	
	public void resume() {
		synchronized(thread) {
			running = true;
			thread.notify();
			delegate.resume();
		}
	}

	//kan tas bort kanske
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
			
			if(!running) {
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
		firePropertyChange("shape", oldShape, currentShape);
	}
	
	public void quit() 
	{
		System.exit(1);
	}
	
	public LinkedList<Shape> GetNextShapes()
	{
		return nextShapes;
	}
	
	public int getRemovedRows() 
	{
		return linesCleared;
	}
	
	public int getTime() 
	{
		return timePassed;
	}
	
	public void setCurrentShape(Shape shape) 
	{
		currentShape = shape;
	}
	
	public void setNextShapes(LinkedList<Shape> shapes) 
	{
		nextShapes = new LinkedList<Shape>();
		for(Shape shape: shapes) 
		{
			nextShapes.addFirst(shape);
		}
	}

	public void setScore(int score) 
	{
		this.points = score;
	}
	
	public void setTime(int time) 
	{
		this.timePassed = time;
	}
	
	public void setLevel(int level) 
	{
		this.level = level;
	}
	
	public void setClearedRows(int removedRows) 
	{
		this.linesCleared = removedRows;
	}
	
	public void restart() {
		board.resetBoard();
		setLevel(1);
		setScore(0);
		linesToClear = 10;
		setClearedRows(0);
		setTime(0);
		gameOver = false;
		setFirstShape();
		fireGameField();
		resume();
	}
	
	/**
	 * Forces Listeners to update
	 */
	public void fireGameField() {
		firePropertyChange("shape", null, currentShape);
		firePropertyChange("next shape", null, nextShapes);
		firePropertyChange("points", null, points);
		firePropertyChange("points", null, points);
		firePropertyChange("time", null, timePassed);
		firePropertyChange("level", null, level);
		firePropertyChange("lines cleared", null, linesCleared);
		firePropertyChange("board", null, board);
		firePropertyChange("shape", oldShape, currentShape);
		firePropertyChange("next shape", oldShapes, nextShapes);
	}
}
