package server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Handles the input from the server to the client in a seperate thread so it can both recive and
 * output data without either processes blocking eachother.
 * @author adde_
 *
 */
public class ConnectionHandler implements Runnable
{

	private BufferedReader indata;
	

	public ConnectionHandler(Socket server) throws IOException 
	{
		indata = new BufferedReader(new InputStreamReader(server.getInputStream()));
	}
	
	@Override
	public void run() 
	{
		try 
		{
			while(true) 
			{
				String serverOutput = indata.readLine();	
				//Kod som kommunicerar till gameEngine läggs till här
				int code =  stringToInt(serverOutput);
				
				switch(code)
				{
					case 0: case 1: case 2: case 3: case 4: case 5: case 6: case 7: case 8: case 9:
						addRow(code); break;
					case 10:
						win(); break;
					case 11:
						start(); break;
					case 15:
						System.out.println("Says " + serverOutput); break;
				}
			}

		}
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally 
		{
			try 
			{
				indata.close();
			}
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	
	public int stringToInt(String s) 
	{
		int num;
		try {
		   num = Integer.parseInt(s);
		}
		catch (NumberFormatException e)
		{
		   num = 15;
		}
		return num;	
	}
	
	public void start() 
	{
		System.out.println("Start game");
		//add code here
	}
	
	public void win() 
	{
		System.out.println("You won");
		//add code here
	}
	
	public void addRow(int n) 
	{

		System.out.println("Add row with opening " + n);
		//add code here
	}

}
