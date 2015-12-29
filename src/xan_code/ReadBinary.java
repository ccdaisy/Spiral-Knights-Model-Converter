package xan_code;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.threerings.export.BinaryImporter;
import com.threerings.export.XMLExporter;

public class ReadBinary {
	static File deXML;
	static File theOBJ;
	static Object object;
	@SuppressWarnings("resource")
	/**
	 * Reads a .DAT file then converts it to an XML.
	 * Does <strong>NOT</strong> include textures.
	 */
	public static File ReadToXML(File f) throws IOException { //Start it up
		try { //Add a try to make sure the file exists.
			deXML = new File(f.getName()+".xml"); //Make the new XML to export the XML to
			BinaryImporter in = new BinaryImporter(new FileInputStream(f)); //Get the BinaryImporter, setting it up for the input file
			XMLExporter out = new XMLExporter(new FileOutputStream(deXML)); //Get the XMLExporter, setting it up with the XML we made 2 lines up
			try { //Designed to allow an easy loop for conversion
				while (true) { //Loop
					out.writeObject(in.readObject()); //XML write what Binary reads
				}
			} catch (EOFException e) { //This will end the above loop
				// no problem
			} finally { //After that
				in.close(); //Close resources
				out.close();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return deXML; //Return the XML
	}

	/**
	 * Reads a .DAT file then converts it directly to an OBJ.
	 * <strong>DOES</strong> include textures.
	 */
	public static File Read(File f) throws IOException { //Set it up
		try { //Add a try to make sure the file exists.
			BinaryImporter in = new BinaryImporter(new FileInputStream(f)); //Get a BinaryImporter with the input file.
			try {
				while (true) {
					object = in.readObject();
					System.out.println(object.getClass().getName());
				}
			} catch (EOFException e) {

			} finally {
				in.close();
			}
		} catch (FileNotFoundException er) {
			er.printStackTrace();
		}
		return theOBJ;
	}
}
