package server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

/**
 * The Server Creates one ClienterHandler for each client connected.
 * It reads the input the server receives from each client on their on thread.
 * It uses the clients list to communicate with the other active ClientHandler threads.
 * @author Andreas Greppe
 * @since 2020-02-29
 *
 */
public class ClientHandler implements Runnable
{
	
	private PrintWriter writer;
	private BufferedReader reader;
	private ArrayList<ClientHandler> clients;

	public ClientHandler(Socket client, ArrayList<ClientHandler> clients) throws IOException 
	{
		this.clients = clients;
		
		reader = new  BufferedReader(new InputStreamReader(client.getInputStream()));
		writer = new PrintWriter(client.getOutputStream(), true);

	}

	@Override
	public void run()
	{
		try 
		{
			while(true) 
			{
				String message = reader.readLine();
				writer.println("client says:" + message);
				if (message.startsWith("msg"))
				{
					int firstSpace = message.indexOf(" ");
					if (firstSpace != -1) 
					{
						toOther(message.substring(firstSpace+1));
					}
				}
			}
		}
		catch( IOException e)
		{
			//h
			exit();
		}

	}
	
	/**
	 * Sends a message to all other clients connected to the server
	 * @param message the message string that is sent
	 */
	private void toOther(String message) 
	{
		for(ClientHandler oClient : clients) 
		{
			if(oClient != this) 
			{
				oClient.writer.println(message);
			}
		}
	}
	
	/**
	 * Removes the ClientHandler from the clients list, and messages the other client that to interrupt the online match,
	 * then puts the thread to sleep.
	 */
	private void exit() 
	{
		clients.remove(this);
		toOther("12");
		try 
		{
			while(true) 
			{
				Thread.sleep(10000);
			}
		}
		catch (InterruptedException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * gets the writer of the client handler, is used if the server should directly communication with the clients
	 * @return returns the clients handlers writer.
	 */
	PrintWriter getWriter() 
	{
		return writer;
	}
}
