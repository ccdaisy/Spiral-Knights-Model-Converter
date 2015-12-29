package xan_code;

/*
 * Designed by Brent "XanthicDragon"
 * 
 * Purpose: Determine file extension and run it through the code to move it to an OBJ.
 */

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;

import main.BeginConversion; //Import the conversion code. Returns a string based on the file
import xan_code.ReadBinary; //Get the .DAT reading functions

@SuppressWarnings("serial")
public class HandleFiles extends Main { //Extend main to get the log from the opener UI.
	
	static BeginConversion converter = new BeginConversion(); //Get the converter for XML files since this will also read XMLs derived from the .DAT
	@SuppressWarnings("static-access")
	public static String convert(File file, boolean isXML) { //Convert. Passes in the .DAT or .XML file, a boolean to whether or not its extension is .XML, and the FileInputStream from file
		if (isXML) { //If it's an XML
			String objst = ""; //This is the text to store the WaveFront OBJ (Model format) as a string
			try {
				objst = converter.BeginConvert(file); //Pass the XML into the java files required to read from the XML and convert it to an OBJ. They return the text from an OBJ file.
			} catch (Exception e) {
				//Exceptions are handled before, though to be safe...
				e.printStackTrace();
			}
			return objst; //Return that text to Main so I can create the file.
		} else { //We have a .DAT
			File DatXML;
			String DatText = "";
			try {
				DatXML = ReadBinary.ReadToXML(file); //Right now this actually returns the text of an XML, but that doesn't matter much at the moment.
				if (DatXML != null) {
					DatText = converter.BeginConvert(DatXML);
				} else {
					DatText = "No XML acquired!";
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return DatText;
		}
	}
}