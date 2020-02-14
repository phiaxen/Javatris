package server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

/*
 * A Separate thread of the server that handles a specific client
 */
public class ClientHandler implements Runnable
{
	private Socket client;
	private PrintWriter writer;
	private BufferedReader reader;
	private ArrayList<ClientHandler> clients;

	public ClientHandler(Socket client, ArrayList<ClientHandler> clients) throws IOException 
	{
		this.client = client;
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
				//writer.println("client says:" + message);
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
			System.out.println("Client disconnected");
			System.out.println("Exiting..");
			toOther("12");
		}

	}
	
	/*
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
	
	/*
	 * gets the writer of the client handler, is used if the server should directly communication with the clients
	 * @return returns the clients handlers writer.
	 */
	PrintWriter getWriter() 
	{
		return writer;
	}
}
