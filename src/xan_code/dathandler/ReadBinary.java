package xan_code.dathandler;

import xan_code.Main;

import java.io.IOException;
import java.io.InputStream;
import java.io.DataInputStream;
import java.util.zip.InflaterInputStream;

@SuppressWarnings("serial")
public class ReadBinary extends Main {
	//This will be the code we're focusing on for my "Speedcode".
	
	//There's gonna be a LOT of stuff to do!
	
	
	protected static InputStream _base;
	protected static DataInputStream _in;
	
    public static final int MAGIC_NUMBER = 0xFACEAF0E;
    public static final short VERSION = 0x1002;
    public static final short COMPRESSED_FORMAT_FLAG = 0x1000;
    
    
	public static String Read(InputStream in) {
		_base = in;
		_in = new DataInputStream(_base = in); //Start by creating the DataInputStream
		//Do a check
		boolean ProperDAT = Validate();
		String item = "Unexpected error during DAT read!";
		if (ProperDAT) {
			try {
				short flags = _in.readShort();
				boolean compressed = (flags & COMPRESSED_FORMAT_FLAG) != 0;
				if (compressed) {
					_in = new DataInputStream(new InflaterInputStream(_base)); //Uncompress if compressed
				}
		        
				item = _in.readUTF();
				//Note - Above just returns a "sorta-english" version of the dat.
				
			} catch (IOException e) {
				log.append("[ERROR] - IOException thrown! (Line 70, ReadBinary.java)\n");
			}
		} else {
			log.append("[ERROR] - Invalid .DAT file!\n");
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


