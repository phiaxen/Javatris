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

	public static Shape currentShape;
	public Shape shapes[] = new Shape[7];
	public static Board board;
	private BufferedImage blocks;
	private BufferedImage[] colors = new BufferedImage[7];
	private SideInfo sideInfo;
	private Client client;
	private Boolean mulitplayer = true;
	public int level = 0;
	public int points = 0;
	public int rowsDeleted = 0;
	
	private Timer timer;
	private final int FPS = 60;
	private final int delay = 1000/FPS;
	
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
		setColors();
		setShapes();
		SpawnShape();
		board = new Board(20,10);
		
		// provisorisk l�sning =)
//		timer = new Timer(delay,this);
//		Timer t = new Timer(delay,boardView);
//		timer.start();
//		t.start();
	
	}
	
	
	public void setColors() {
		try {
			blocks = ImageIO.read(Board.class.getResource("/images/tiles.png"));
		}catch(IOException e) {
			e.printStackTrace();
		}
		for(int i=0; i<colors.length; i++){
			colors[i] = blocks.getSubimage(i*40,0, 40, 40);
		}
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
				checkFullRow();
				
				
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
	
	public void setShapes() {
		shapes[0] = new Shape(board,1,colors[0], new int[][] {
			{1,1,1,1}}); //I
	
		shapes[1] = new Shape(board,2,colors[1], new int[][] {
			{1,1,0},
			{0,1,1}}); //Z
		
		shapes[2] = new Shape(board,3,colors[2],new int[][] {
			{0,1,1},
			{1,1,0}}); //S
		
		shapes[3] = new Shape(board,4,colors[3], new int[][] {
			{0,0,1},
			{1,1,1}}); //L
		
		shapes[4]= new Shape(board,5,colors[4], new int[][] {
			{1,1,1},
			{0,0,1}}); //J
		
		shapes[5]= new Shape(board,6,colors[5], new int[][] {
			{1,1,1},
			{0,1,0}}); //T
		
		shapes[6]= new Shape(board,7,colors[6], new int[][] {
			{1,1},
			{1,1}}); //Box 
	}
	
	public void SpawnShape() {
		
		int randomNum = ThreadLocalRandom.current().nextInt(0, shapes.length);
		currentShape = new Shape(board,randomNum+1,shapes[randomNum].getBlock(),shapes[randomNum].getCoords());
		
	}
	

	public void setStaticShapes() {
	
		board.setStaticShapeInBoard(currentShape.getShape(),currentShape.x,currentShape.y,currentShape.getColor());
	
	}
	
	public static Shape getCurrentShape() {
		return currentShape;
	}
	
	// funktionen antar i b�rjan att en hel rad �r full. 
	// Om en kolumn inte �r i fylld s�tts en flagga(fullRow) till false
	// Annars kommer funktionen deleteRow ta bort raden och funktionen moveRowsDown flyttar alla �vriga rader ovanf�r ner.
	private void checkFullRow() {
		
		int row = 0;
		int col = board.getBoard()[0].length;
		boolean fullRow = true;
		
		while(row <= board.getBoard().length-1) {
			
			for(int j = 0; j < col; j++) { 
				if(board.getBoard()[row][j] == 0) {
						
						fullRow = false;
				}
			}
			if(fullRow) {
				deleteRow(row);
				moveRowsDown(row);
				if(mulitplayer) 
				{
					client.sendInt(6);
				}
				rowsDeleted++;
			}
			row++;
			fullRow= true;
		}
		
	
		
		points += scoreHandler(level,rowsDeleted);
		System.out.println("points: " + points);
		sideInfo.updateScore(points);
		rowsDeleted = 0;

	}
	
	
	private void deleteRow(int row) {
			for(int i = 0; i < board.getBoard()[0].length; i++) {
				board.getBoard()[row][i] = 0;
			}
		
	}
	
	private void moveRowsDown(int row) {
		
		for(int i=row-1; i>=0; i--) {
			for(int j=0; j<board.getBoard()[0].length; j++) {
				board.getBoard()[i+1][j] = board.getBoard()[i][j];
			}
		}
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
		
		//gameloop
		while(running) {
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
	
	private static void moveRowsUp() {
		
		for(int i=1; i<20; i++) {
			for(int j=0; j<board.getBoard()[0].length; j++) {
				board.getBoard()[i-1][j] = board.getBoard()[i][j];
			}
		}
	}
	
	public static void addRow(int collum, int color) 
	{
		moveRowsUp();
		if(collum >= 0 && collum < 10) 
		{
			for(int i = 0; i < 10; i++) 
			{
				if(i != collum)
				board.getBoard()[19][i] = color;
			}
		}
	}
}
