
package model;

import server.*;
import java.util.TimerTask;

import java.util.LinkedList;
import java.util.Timer;

/**
 * GameEngine is a class that handles all the calculations in the game.
 * 
 * @author Philip Axenhamn
 * @author Joachim Antfolk
 * @author Andreas Greppe
 * @author Tobias Mauritzon
 * @version 2.0
 * @since 2020-03-01
 */
public class GameEngine extends AbstractModel implements Runnable {

	/*
	 * Interface that handles the communication with the game engine.
	 */
	public interface Delegate {
		Client getClient();

		void pause();

		void gameOver(int type);

		void connectionLost();
	}

	private Shape currentShape; // The shape that is currently in action
	private Shape oldShape;
	private Board board;
	private Client client;
	private Boolean online; // Change this to true for multiplayer

	// points and levelup things
	private int level = 1;
	private int points = 0;
	private int linesToClear = 10; // how many lines it takes to level up, increase by 5 for each level

	private int linesCleared = 0; // how many lines the player has cleared
	public Delegate delegate;

	private boolean running = false;

	private Thread thread;
	private final int TICKSPERSECOND = 60;

	private int timePassed = 0;
	private int oldTime = 0;
	private Timer GameTime;

	private long time = 0, lastTime = 0;

	private boolean gameOver = false;

	private final ShapeHandler shapeHandler;

	private boolean GameStart = true;

	private int speedDown = 700;
	private boolean isLoading = false;

	private Timer delayTimer;
	private boolean waitBeforeStatic;

	private final int delaysAllowed = 10;
	private int delaysCalled = 0;

	public GameEngine(Board board, Boolean online) {
		super();
		this.board = board;
		this.online = online;
		this.client = null;
		this.GameTime = new Timer();
		this.delayTimer = new Timer();
		this.shapeHandler = new ShapeHandler(board);
		setFirstShape();
	}

	TimerTask task = new TimerTask() {
		public void run() {
			if (running && !gameOver) {
				oldTime = timePassed;
				timePassed++;
				firePropertyChange("time", oldTime, timePassed);
			}
		}
	};

	public void setDelayBeforeStatic() {
		delaysCalled++;
		if (delaysCalled <= delaysAllowed) {
			delayTimer.cancel();
			delayTimer = new Timer();
			waitBeforeStatic = true;
			delayTimer.schedule(new java.util.TimerTask() {
				@Override
				public void run() {
					waitBeforeStatic = false;
				}
			}, 400);
		} else {
			waitBeforeStatic = false;
			delaysCalled = 0;
		}

	}

	/**
	 * 
	 */
	private void update() {
		Board oldBoard = board.clone();
		boolean currentState = currentShape.hasCollidedY();
		time += System.currentTimeMillis() - lastTime;
		lastTime = System.currentTimeMillis();

		CheckCollisionY();
		if (currentShape.hasCollidedY() && !waitBeforeStatic) {
			checkIfGameOver();
			setStaticShapes();
			int rowsDeleted = 0;
			int oldlinesC = linesCleared;
			firePropertyChange("collisionY", currentState, currentShape.hasCollidedY());

			for (int i = 0; i < board.getBoard().length; i++) {
				if (board.checkFullRow(i)) {
					rowsDeleted++;
					linesCleared++;
					if (online) {
						client.sendInt(currentShape.getX());
					}
				}
			}

			// Updates score if rows have been deleted
			if (rowsDeleted > 0) {
				if (linesCleared >= linesToClear) {
					levelUp();
				}
				int oldPoints = points;
				points += getScore(level, rowsDeleted);

				firePropertyChange("points", oldPoints, points);
				firePropertyChange("lines cleared", oldlinesC, linesCleared);
			}
			SpawnShape();
		}

		if ((time > currentShape.getCurrentSpeed()) && (!currentShape.hasCollidedY())) {
			currentShape.moveDown();
			time = 0;
		}
		CheckCollisionX();
		if (!currentShape.hasCollidedX()) {
			currentShape.moveDeltaX();
			firePropertyChange("movedX", 0, currentShape.getDeltaX());
			currentShape.setDeltaX(0);
		}
		currentShape.setCollidedY(false);
		currentShape.setCollidedX(false);

		firePropertyChange("board", oldBoard, board);
		firePropertyChange("shape", oldShape, currentShape);
	}

	/**
	 * Adds a row with one open spot, used for versus-mode
	 * 
	 * @param column : Where the row should have an open spot
	 * @param color  : Color of the row
	 */
	public void addRow(int column, int color) {
		Board oldBoard = board.clone();
		board.addRow(column, color);
		firePropertyChange("board", oldBoard, board);
	}

	/**
	 * Check if the current shape has collided sideways
	 */
	private void CheckCollisionX() {
		for (int i = 0; i < currentShape.getCoords().length; i++) {
			for (int j = 0; j < currentShape.getCoords()[0].length; j++) {
				if (currentShape.getCoords()[i][j] != 0) {
					if (board.getBoard()[currentShape.getY() + i][currentShape.getX() + j
							+ currentShape.getDeltaX()] != 0) {
						currentShape.setCollidedX(true);
					}
				}
			}
		}
	}

	/**
	 * Check if the current shape has collided downwards
	 */
	private void CheckCollisionY() {
		if ((currentShape.getY() + currentShape.getCoords().length > 19)) {
			currentShape.setCollidedY(true);
		} else {
			for (int i = 0; i < currentShape.getCoords().length; i++) {
				for (int j = 0; j < currentShape.getCoords()[0].length; j++) {
					if (currentShape.getCoords()[i][j] != 0) {
						if (board.getBoard()[currentShape.getY() + i + 1][j + currentShape.getX()] != 0) {
							currentShape.setCollidedY(true);
						}
					}
				}
			}
		}
	}

	/*
	 * this function is called only when the game starts, only once
	 */
	public void setFirstShape() {
		currentShape = shapeHandler.nextShape();
	}

	public void SpawnShape() {
		currentShape = shapeHandler.nextShape();
		currentShape.changeNormalSpeed(speedDown);
	}

	/**
	 * Check if the player has lost
	 */
	private void checkIfGameOver() {
		for (int i = 0; i < 10; i++) {
			if (board.getBoard()[0][i] != 0) {
				gameOver = true;
			}
		}
		if (gameOver && !online) {
			gameOver(0);
		} else if (gameOver && online) {
			gameOver(1);
		}
	}

	public void connectionLost() {
		if (!gameOver) {
			running = false;
			delegate.gameOver(3);
		}
	}

	/**
	 * If loss has occured notify client and delegate
	 * 
	 * @param type : 0 if game over in solo game, 1 if player has lost, 2 for win
	 */
	public void gameOver(int type) {
		running = false;
		if (online) {
			if (type == 1) {
				client.sendInt(10);
			} else if (type == 2) {
				gameOver = true;
			}

		}
		delegate.gameOver(type);
	}

	public void setStaticShapes() {
		board.setStaticShapeInBoard(currentShape.getCoords(), currentShape.getX(), currentShape.getY(),
				currentShape.getColor());
	}

	public Shape getCurrentShape() {
		return currentShape;
	}

	private void levelUp() {
		int oldLevel = level;
		level++;
		firePropertyChange("level", oldLevel, level);
		updateLinesToClear();
		updateSpeedDown();
	}

	private void updateLinesToClear() {
		linesToClear = 10 + (level - 1) * 5;
	}

	private void updateSpeedDown() {
		if (speedDown > 120) {
			speedDown = 700 - 20 * (level - 1);
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

	/**
	 * Returns wheter or not the game is online
	 * 
	 * @return true if online. else false
	 */
	public boolean getOnline() {
		return online;
	}

	private int getScore(int level, int rows) {
		switch (rows) {
		case 1:
			return 40 * level;
		case 2:
			return 100 * level;
		case 3:
			return 300 * level;
		case 4:
			return 1200 * level;
		default:
			return 0;
		}
	}

	public void setIsLoading() {
		isLoading = true;
	}

	public synchronized void start() {
		fireGameField();
		updateLinesToClear();
		updateSpeedDown();
		isLoading = false;
		if (!online) {
			running = true;
			if (GameStart) {
				System.out.println("GAME START");
				GameTime.scheduleAtFixedRate(task, 0, 1000);
				thread = new Thread(this);
				thread.start(); // start thread
				GameStart = false;
			} else {
				synchronized (thread) {
					thread.notify();
				}
			}

		} else {
			if (this.delegate != null) {
				System.out.println("got client");
				client = this.delegate.getClient();
			}
		}
	}

	public synchronized void startOnline() {
		running = true;
		if (GameStart) {
			System.out.println("GAME ONLINE START1");
			GameTime.scheduleAtFixedRate(task, 0, 1000);
			thread = new Thread(this);
			thread.start(); // start thread
			GameStart = false;
		} else {
			System.out.println("GAME ONLINE START2");
			synchronized (thread) {
				thread.notify();
			}

		}
	}

	public void pause() {
		if (!online) {
			running = false;
			delegate.pause();
		}
	}

	public void resume() {
		if (!online) {
			synchronized (thread) {
				running = true;
				thread.notify();
			}
		}
	}

	// kan tas bort kanske
	private synchronized void stop() {
		if (!running) {
			return;
		}
		running = false;

		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.exit(1);
	}

	// This function gets called when we start the thread.
	@Override
	public void run() {

		long lastTime = System.nanoTime();
		double ns = 1000000000 / TICKSPERSECOND;
		double deltaTime = 0;

		while (!Thread.interrupted()) {

			if (!running) {
				try {
					synchronized (thread) {
						thread.wait();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else {
				try {
					Thread.sleep(1); // sleep in 1ms
				} catch (InterruptedException e) {
					break;
				}

				long now = System.nanoTime();
				deltaTime += (now - lastTime) / ns;
				lastTime = now;

				if (deltaTime >= 1) {
					tick();
					deltaTime--;
				}
			}
		}
		stop();
	}

	// everything in game that updates
	private void tick() {
		if (!gameOver)
			update();
	}

	public void quit() {
		if (delegate != null && !running) {
			delegate.gameOver(3);
		}
	}

	public LinkedList<Shape> GetNextShapes() {
		// return nextShapes;
		return shapeHandler.getNextShapes();
	}

	public int getRemovedRows() {
		return linesCleared;
	}

	public int getTime() {
		return timePassed;
	}

	/**
	 * Gets the GameEngines shapeHandler
	 * 
	 * @return : the GameEngines shapeHandler
	 */
	public ShapeHandler getShapeHandler() {
		return shapeHandler;
	}

	public void setCurrentShape(Shape shape) {
		currentShape = shape;
	}

	/**
	 * Sets the list of shapes in shapehandler
	 * 
	 * @param shapes : new list of shapes
	 */
	public void setNextShapes(LinkedList<Shape> shapes) {
		shapeHandler.setNextShapes(shapes);
	}

	public void setScore(int score) {
		this.points = score;
	}

	public void setTime(int time) {
		this.timePassed = time;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public void setClearedRows(int removedRows) {
		this.linesCleared = removedRows;
	}

	/**
	 * Sets the online flag to true or false
	 * 
	 * @param online : true for online, false for offline
	 */
	public void setOnline(boolean online) {
		this.online = online;
	}

	public void restart() {
		if (!isLoading) {
			board.resetBoard();
			setLevel(1);
			setScore(0);
			linesToClear = 10;
			setClearedRows(0);
			setTime(0);
			setFirstShape();
		}
		gameOver = false;
		start();
	}

	/**
	 * Forces all listeners to update
	 */
	public void fireGameField() {
		firePropertyChange("points", null, points);
		firePropertyChange("time", null, timePassed);
		firePropertyChange("level", null, level);
		firePropertyChange("lines cleared", null, linesCleared);
		firePropertyChange("board", null, board);
		firePropertyChange("shape", oldShape, currentShape);
		shapeHandler.updateListeners();
	}
}
