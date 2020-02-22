package model;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;

/*
 * A class created to manage the save data for the game, it can both save and loads game states
 * It uses static methods so a instance of the class is not required.
 */
public class SaveManager {

	/*
	 * Saves the data to a file with the parameter filename as the files name.
	 * @param data the that is saved
	 * @param fileName the name of file that the data is saved in
	 */
	public static void saveFile(Serializable data, String fileName ) throws Exception 
	{
		try (ObjectOutputStream outputStream = new ObjectOutputStream(Files.newOutputStream(Paths.get(fileName))))
		{	//hej
			outputStream.writeObject(data);
		}
	}
	
	/*
	 * reads the data from the selected file and loads it
	 * @param fileName the name of file that the data is saved in
	 */
	public static void loadFile(String fileName ) throws Exception 
	{
		try (ObjectInputStream inputStream = new ObjectInputStream(Files.newInputStream(Paths.get(fileName))))
		{
			inputStream.readObject();
		}
	}
}
