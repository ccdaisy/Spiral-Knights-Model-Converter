package xan_code;

/*
 * Designed by Brent Duanne
 * 
 * Purpose: Determine file extension and run it through the code to move it to an OBJ.
 * 
 * Should I utilize the other code for this one? It would be very hacky, but would probably work.
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import main.BeginConversion; //Import the conversion code. Returns a string based on the file

public class HandleFiles {
	static BeginConversion converter = new BeginConversion();
	static final String NotReadyText = "Remember that .DAT exporting is not ready yet! Please use Spiral Spy (http://spiral.onyxbits.de/download) to select your desired model and convert it to an XML.";
	@SuppressWarnings("static-access")
	public static String convert(File file, boolean isXML) {
		if (isXML) {
			String xml = "";
			String obj = "";
			try {
				xml = new Scanner(file).useDelimiter("\\Z").next();
				obj = converter.BeginConvert(xml);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				obj = "";
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				obj = "";
				e.printStackTrace();
			}
			return obj;
		} else {
			return NotReadyText;
		}
	}
}