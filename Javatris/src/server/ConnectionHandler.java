package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Handles the input from the server to the client in a separate thread so it
 * can both receive and output data without either processes blocking each
 * other.
 * 
 * @author Andreas Greppe
 * @since 2020-02-29
 *
 */
public class ConnectionHandler implements Runnable {
	/*
	 * Interface that handles the communication with the game engine.
	 */
	public interface Delegate {
		void addRow(int row);

		void gameOver(int type);

		void start();

		void pause();

		void quit();

		void connectionLost();
	}

	private BufferedReader indata;
	public Delegate delegate;

	public ConnectionHandler(Socket server) throws IOException {
		indata = new BufferedReader(new InputStreamReader(server.getInputStream()));
	}

	@Override
	public void run() {
		try {
			while (true) {
				String serverOutput = indata.readLine();
				// Kod som kommunicerar till gameEngine läggs till här
				int code = stringToInt(serverOutput);
				switch (code) {
				case 0:
				case 1:
				case 2:
				case 3:
				case 4:
				case 5:
				case 6:
				case 7:
				case 8:
				case 9:
					addRow(code);
					break;
				case 10:
					gameOver();
					System.out.println("game over");
					break;
				case 11:
					start();
					break;
				case 12:
					connectionLost();
					System.out.println("connection lost");
					break;
				case 15:
					System.out.println("Says " + serverOutput);
					break;
				default:
					break;
				}
			}

		} catch (IOException e) {
			connectionLost();
			// e.printStackTrace();
		} finally {
			try {
				indata.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	/**
	 * Converts a string cotaining number to a integer, if the string does not
	 * contain any numbers the number 15 is instead returned
	 * 
	 * @param s the string that is converted to a int
	 * @return the string converted to a int
	 */
	public int stringToInt(String s) {
		int num;
		try {
			num = Integer.parseInt(s);
		} catch (NumberFormatException e) {
			num = 15;
		}
		return num;
	}

	/**
	 * Uses the delegate interaface to call the start function from game engine and
	 * starts game.
	 */
	public void start() {
		System.out.println("Start game");
		if (delegate != null) {
			delegate.start();
		}
	}

	/**
	 * Uses the delegate interface to call the stop function from game engine and
	 * stops the game.
	 */
	public void stop() {
		System.out.println("stop game");
		if (delegate != null) {
			delegate.pause();
		}
	}

	/**
	 * Uses the delegate interface to call the quit function from game engine and
	 * exits the application.
	 */
	public void connectionLost() {
		if (delegate != null) {
			delegate.connectionLost();
		}
	}

	/**
	 * Does nothing for now, Should communicate with the game engine to display a
	 * victory screen
	 */
	public void gameOver() {
		System.out.println("You won");
		delegate.gameOver(2);
	}

	/**
	 * Uses the delagate interface to communication with game engine adding a row to
	 * the board with a oppening a the postion n
	 * 
	 * @param n the position with a opening in the added row
	 */
	public void addRow(int n) {
		System.out.println("Add row with opening " + n);
		if (delegate != null) {
			delegate.addRow(n);
		}
	}

}
