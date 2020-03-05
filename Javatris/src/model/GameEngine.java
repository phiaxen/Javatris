
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

		void pause();

		void gameOver(int type);

		void connectionLost();
		
		void sendInt(int number);
	}

	// shape and board
	private Shape currentShape; // The shape that is currently in action
	private Shape oldShape;
	private Board board;

	// points and levelup
	private int level = 1;
	private int points = 0;
	private int linesToClear = 10;
	private int linesCleared = 0;

	// thread and update
	private Thread thread;
	private boolean running = false;
	private final int TICKSPERSECOND = 60;
	private int timePassed = 0;
	private int oldTime = 0;
	private long time = 0, lastTime = 0;
	private int speedDown = 700;

	// timers
	private Timer gameTime;
	private Timer delayTimer;
	private boolean waitBeforeStatic;
	private final int delaysAllowed = 10;
	private int delaysCalled = 0;

	// game state
	private boolean gameStart = true;
	private boolean gameOver = false;
	private boolean isLoading = false;
	private boolean online = false;

	private final ShapeHandler shapeHandler;
	public Delegate delegate;

	public GameEngine(Board board, Boolean online) {
		super();
		this.board = board;
		this.online = online;
		this.gameTime = new Timer();
		this.delayTimer = new Timer();
		this.shapeHandler = new ShapeHandler(board);
		setCurrentShape();
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

	/**
	 * Sets a flag to true, starts a timer and when the timer has finnished, the
	 * flag is set to false.
	 */
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
	 * Updates the state of the current shape.
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
						delegate.sendInt(currentShape.getX());
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
			setCurrentShape();
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

	/**
	 * Sets the current shape and change the speed down to be the right speed for
	 * the current level.
	 */
	private void setCurrentShape() {
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

	/**
	 * tells the game to stop and display an error message.
	 */
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
				delegate.sendInt(10);
			} else if (type == 2) {
				gameOver = true;
			}

		}
		delegate.gameOver(type);
	}

	/**
	 * When the current shape has collided, this function is called to store the
	 * shape in the board.
	 */
	private void setStaticShapes() {
		board.setStaticShapeInBoard(currentShape.getCoords(), currentShape.getX(), currentShape.getY(),
				currentShape.getColor());
	}

	/**
	 * Returns the current shape
	 * 
	 * @return currentShape the shape in action
	 */
	public Shape getCurrentShape() {
		return currentShape;
	}

	/**
	 * Increases level by one, updates the speed and lines to clear.
	 */
	private void levelUp() {
		int oldLevel = level;
		level++;
		firePropertyChange("level", oldLevel, level);
		updateLinesToClear();
		updateSpeedDown();
	}

	/**
	 * Updates how many lines that the player needs to clear in order to level up.
	 */
	private void updateLinesToClear() {
		linesToClear = 10 + (level - 1) * 5;
	}

	/**
	 * Updates the speed the shape travels downwards. Based on the current level.
	 */
	private void updateSpeedDown() {
		if (speedDown > 120) {
			speedDown = 700 - 20 * (level - 1);
		}
	}

	/**
	 * Returns the current level
	 * 
	 * @return level the current level
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * Returns the current score
	 * 
	 * @return points the current scor
	 */
	public int getPoints() {
		return points;
	}

	/**
	 * Calculates the score based on current level and the amount of lines cleared
	 * 
	 * @return this function returns the calculated score
	 */
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

	/**
	 * Sets the isLoading flag to true.
	 */
	public void setIsLoading() {
		isLoading = true;
	}

	/**
	 * Starts the game. If it is the first time,a timer starts and a thread is
	 * created and started. Otherwise the thread is notified. If the flag online is
	 * true, the client is recieved.
	 */
	public synchronized void start() {
		fireGameField();
		updateLinesToClear();
		updateSpeedDown();
		isLoading = false;
		if (!online) {
			running = true;
			if (gameStart) {
				gameTime.scheduleAtFixedRate(task, 0, 1000);
				thread = new Thread(this);
				thread.start(); // start thread
				gameStart = false;
			} else {
				synchronized (thread) {
					thread.notify();
				}
			}
		}
	}

	/**
	 * Starts a game for multiplayer.
	 */
	public synchronized void startOnline() {
		running = true;
		if (gameStart) {
			gameTime.scheduleAtFixedRate(task, 0, 1000);
			thread = new Thread(this);
			thread.start(); // start thread
			gameStart = false;
		} else {
			synchronized (thread) {
				thread.notify();
			}

		}
	}

	/**
	 * Pauses the game
	 */
	public void pause() {
		if (!online) {
			running = false;
			delegate.pause();
		}
	}

	/**
	 * Pauses the game
	 */
	public void resume() {
		if (!online) {
			synchronized (thread) {
				running = true;
				thread.notify();
			}
		}
	}

	/**
	 * Kills the thread and exits the game.
	 */
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

	// This function gets called when the thread starts.
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
					if (!gameOver) {
						update();
					}
					deltaTime--;
				}
			}
		}
		stop();
	}

	/**
	 * Tells the game to display a gameover menu.
	 */
	public void quit() {
		if (delegate != null && !running) {
			delegate.gameOver(3);
		}
	}

	/**
	 * @return Returns a list of the next 3 shapes.
	 */
	public LinkedList<Shape> GetNextShapes() {
		return shapeHandler.getNextShapes();
	}

	/**
	 * @return Returns the amount of row cleared.
	 */
	public int getRemovedRows() {
		return linesCleared;
	}

	/**
	 * @return Returns the time passed.
	 */
	public int getTime() {
		return timePassed;
	}

	/**
	 * Gets the GameEngines shapeHandler.
	 * 
	 * @return : the GameEngines shapeHandler
	 */
	public ShapeHandler getShapeHandler() {
		return shapeHandler;
	}

	/**
	 * Sets a saved shape to the current shape.
	 * 
	 * @param shape : the shape to be the current shape
	 */
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

	/**
	 * Sets the current score.
	 * 
	 * @param score : the score-value 
	 */
	public void setScore(int score) {
		this.points = score;
	}
	
	/**
	 * Sets the current time.
	 * 
	 * @param time : the time. 
	 */
	public void setTime(int time) {
		this.timePassed = time;
	}

	/**
	 * Sets the level.
	 * 
	 * @param level : the level. 
	 */
	public void setLevel(int level) {
		this.level = level;
	}

	/**
	 * Sets the amount of lines that has been removed.
	 * 
	 * @param removedRows : the amount of rows removed. 
	 */
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

	/**
	 * Resets all game values and restarts the game.
	 */
	public void restart() {
		if (!isLoading) {
			board.resetBoard();
			setLevel(1);
			setScore(0);
			linesToClear = 10;
			setClearedRows(0);
			setTime(0);
			setCurrentShape();
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
