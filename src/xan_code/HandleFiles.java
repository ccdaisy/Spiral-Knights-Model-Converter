package xan_code;

/*
 * Designed by Brent Duanne
 * 
 * Purpose: Determine file extension and run it through the code to move it to an OBJ or ASCII AutoDesk FBX.
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;

import main.BeginConversion; //Import the conversion code. Returns a string based on the file
import xan_code.dathandler.ReadBinary;

public class HandleFiles {
	static BeginConversion converter = new BeginConversion();
	static String DatText = "";
	@SuppressWarnings("static-access")
	public static String convert(File file, boolean isXML) {
		if (isXML) {
			String xml = "";
			String obj = "";
			try {
				xml = new Scanner(file).useDelimiter("\\Z").next();
				obj = converter.BeginConvert(xml);
			} catch (FileNotFoundException e) { //This should never happen.
				obj = "";
				e.printStackTrace();
			} catch (IOException e) { //This shouldn't either.
				obj = "";
				e.printStackTrace();
			}
			return obj;
		} else {
			try {
				DatText = ReadBinary.Read(FileUtils.openInputStream(file));
			} catch (IOException e) {
				DatText = "An unexpected error has occured while reading UTF Data from the .DAT!";
			}
			return DatText;
		}
	}
}