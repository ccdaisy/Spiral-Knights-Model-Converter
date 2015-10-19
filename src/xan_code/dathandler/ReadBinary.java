package xan_code.dathandler;

import xan_code.Main;

import java.io.IOException;
import java.io.InputStream;
import java.io.DataInputStream;
import java.lang.reflect.Method;
import java.util.zip.InflaterInputStream;

@SuppressWarnings("serial")
public class ReadBinary extends Main {
	//There's gonna be a LOT of stuff to do!
	
	protected static InputStream _base;
	protected static DataInputStream _in;
	protected static Object _object;
	protected static Object ReadObject;
	protected static Class<?> clazz;
	protected static Method _reader;
	protected static Importer impr;
	
    public static final int MAGIC_NUMBER = 0xFACEAF0E;
    public static final short VERSION = 0x1002;
    public static final short COMPRESSED_FORMAT_FLAG = 0x1000;
    
    
	public static String Read(InputStream in) {
		_base = in; //Set a global variable to the InputStream
		_in = new DataInputStream(_base = in); //Start by creating the DataInputStream
		boolean ProperDAT = Validate(); //Validate if the Magic number and version are correct - Used to differentiate
		//between a Spiral Knights DAT and some other binary.
		String item = "Unexpected error during DAT read!"; //Since I always return the item variable,
		//I can set it up like this since any errors in my try/catch clause will not change this variable.
		if (ProperDAT) { //The .DAT is the correct type.
			try {
				short flags = _in.readShort(); //Flag for the compression system
				boolean compressed = (flags & COMPRESSED_FORMAT_FLAG) != 0; //Check if the compression flag
				//and the format flag are not 0
				if (compressed) { //If they are not 0 then uncompress with the InflaterInputStream
					_in = new DataInputStream(new InflaterInputStream(_base)); //Uncompress if compressed
				}
		        
				_object = _in.getClass();
				
				item = "DAT reading not ready!";
				
			} catch (IOException e) {
				log.append("[ERROR] - IOException thrown! (Line 70, ReadBinary.java)\n"); //Report line on opener GUI
				//Do I want to break? Probably not. The program can function fine without the break here.
			}
		} else {
			log.append("[ERROR] - Invalid .DAT file!\n"); //Put this message in the log screen on the opener GUI
		}
		return item;
	}
	
	public static boolean Validate() {
		boolean correctMagic = false;
		boolean correctVersion = false;
		try {
			correctMagic = _in.readInt() == MAGIC_NUMBER;
			correctVersion = _in.readShort() == VERSION;
		} catch (IOException e) {
			//Do nothing, I'll handle this elsewhere.
			log.append("IOException thrown! (Line 86, ReadBinary.java)");
		}
		return correctMagic && correctVersion || true;
	}
}