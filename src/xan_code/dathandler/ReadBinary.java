package xan_code.dathandler;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

import xan_code.Main;
import xan_code.dathandler.BinaryImporter;
import main.BeginConversion;

@SuppressWarnings("serial")
public class ReadBinary extends Main {
	protected static String XMLFile;
	
	@SuppressWarnings("static-access")
	public static String Read(File file, byte[] bts) throws IOException {
		File temp = new File(file.getPath()+".tmp");
		String converted = "";
		BinaryImporter in = new BinaryImporter(new FileInputStream(file));
		XMLExporter out = new XMLExporter(new FileOutputStream(temp));
		try {
			Object importedObject = in.readObject();
			out.writeObject(importedObject);
		} catch (IOException e) {
			log.append("Error!\n");
			e.printStackTrace();
		} finally {
			in.close();
			converted = BeginConversion.BeginConvert(new Scanner(temp).useDelimiter("\\Z").next());
			out.close();
			temp.delete();
		}
		return converted;
	}
}
