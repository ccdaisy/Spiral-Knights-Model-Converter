//Modified from the old program, this will directly convert as opposed to create a UI and such.

package main;

import java.io.IOException;

public class BeginConversion {
	
	@SuppressWarnings("static-access")
	public static String BeginConvert(String xtxt) throws IOException {
    	XMLReader reader = new XMLReader();
    	HandleTableData handle = new HandleTableData();
    	double[][] triangles = reader.getTriangles(xtxt);
    	int[] indices = reader.getIndices(xtxt);
    	if (triangles != null && indices != null && triangles.length > 0 && indices.length > 0) {
    		System.out.println("OBJ acquired");
    		String OBJ = handle.ConvertToOBJ(triangles, indices);
    		return OBJ;
    	} else {
    		return "";
    	}
    }
}
