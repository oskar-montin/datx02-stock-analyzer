package util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

import controller.Settings;

public class IO {
	
	public static void writeToFile(String content, String fileName) {
		FileOutputStream fop = null;
		File file;
 
		try {
 
			file = new File(fileName);
			fop = new FileOutputStream(file);
 
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
 
			// get the content in bytes
			byte[] contentInBytes = content.getBytes();
 
			fop.write(contentInBytes);
			fop.flush();
			fop.close();
 
			System.out.println("Wrote to file: "+fileName);
 
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fop != null) {
					fop.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Saves the object to the file with the filename
	 * @return true if successful
	 */
	public static boolean save(String fileName, Serializable object) {
		try{
			OutputStream file = new FileOutputStream( fileName );
			OutputStream buffer = new BufferedOutputStream( file );
			ObjectOutput output = new ObjectOutputStream( buffer );
			try{
				output.writeObject(object);
			}
			finally{
				output.close();
			}
		}  
		catch(IOException ex){
			return false;
		}
		return true;
		
	}
	
	public static Serializable loadFromFile(String filename) {
		Serializable s;
		try{
			InputStream file = new FileInputStream( filename );
			InputStream buffer = new BufferedInputStream( file );
			ObjectInput input = new ObjectInputStream ( buffer );
			try{
				s = (Serializable) input.readObject();
			}
			finally{
				input.close();
			}
			return s;
		}
		catch(ClassNotFoundException ex){
			//fLogger.log(Level.SEVERE, "Cannot perform input. Class not found.", ex);
		}
		catch(IOException ex){
			//fLogger.log(Level.SEVERE, "Cannot perform input.", ex);
		}
		return null;
		
	}
}
