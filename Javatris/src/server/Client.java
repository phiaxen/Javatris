package server;
import java.net.*;
import java.io.*;

public class Client 
{
	private static final String IP = "127.0.0.1";
	private static final int PORT = 6969;
	private boolean ready;
	static PrintWriter output;

	public static void main(String[] args) throws UnknownHostException, IOException 
	{
		Socket cSocket = new Socket(IP, PORT);
		ConnectionHandler handler = new ConnectionHandler(cSocket);
		BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
		output =  new PrintWriter(cSocket.getOutputStream(), true);
		
		new Thread(handler).start();
		
		while (true) 
		{
			System.out.println("--");
			String cmd = keyboard.readLine();
			
			if (cmd.equals("exit"))
			{
				 break;
			}
			else if (cmd.equals("ready")) 
			{
				sendInt(11);
			}
			else if (cmd.equals("lose")) 
			{
				sendInt(10);
			}
			else if (cmd.startsWith("add")) 
			{
				int firstSpace = cmd.indexOf(" ");
				if (firstSpace != -1) 
				{
					sendInt(Integer.parseInt(cmd.substring(firstSpace+1)));
				}
			}
			else 
			{
				output.println(cmd);
			}

		}
		
		cSocket.close();
		System.exit(0);
		
	}
	
	/*
	 *  Sends an integer to the other client via the server
	 */
	private static void sendInt(int n)
	{
		output.println("msg " + n);
	}
	

}
