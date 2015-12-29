//Modified from the old program, this will directly convert as opposed to create a UI and such.

package main;

import java.io.File;
import java.io.IOException;

import org.w3c.dom.Document;

public class BeginConversion {
	
	@SuppressWarnings("static-access")
	public static String BeginConvert(File xmlf) throws IOException {
    	XMLReader reader = new XMLReader();
    	HandleTableData handle = new HandleTableData();
    	double[][] triangles = reader.getTriangles(xmlf);
    	int[] indices = reader.getIndices(xmlf);
    	if (triangles != null && indices != null && triangles.length > 0 && indices.length > 0) {
    		System.out.println("OBJ acquired");
    		String OBJ = handle.ConvertToOBJ(triangles, indices);
    		return OBJ;
    	} else {
    		return "";
    	}
    }
	
	@SuppressWarnings("static-access")
	public static String BeginConvert(Document doc) throws IOException {
		XMLReader reader = new XMLReader();
    	HandleTableData handle = new HandleTableData();
    	double[][] triangles = reader.getTriangles(doc);
    	int[] indices = reader.getIndices(doc);
    	if (triangles != null && indices != null && triangles.length > 0 && indices.length > 0) {
    		System.out.println("OBJ acquired");
    		String OBJ = handle.ConvertToOBJ(triangles, indices);
    		return OBJ;
    	} else {
    		return "";
    	}
	}
}