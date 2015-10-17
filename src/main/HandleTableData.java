//Picks up special string from XMLReader and converts it into a String OBJ.

package main;

import java.io.IOException;

public class HandleTableData {
	static String obj = "#Brought to you by Brent \"XanthicDragon\" Duanne\n\ng Model\n\n";
	
	private static int count(String str, String findStr) {
		int lastIndex = 0;
		int count = 0;
		while (lastIndex != -1) {
			lastIndex = str.indexOf(findStr,lastIndex);
			if (lastIndex != -1) {
				count++;
				lastIndex += findStr.length();
			}
		}
		return count;
	}
	
	public static String ConvertToOBJ(double[][] triangles, int[] indices) throws IOException {
		int indLen = indices.length;
		for (int i = 0; i < triangles.length; i++) {
			if (i < indLen) {
				double[] tris = triangles[indices[i]];
				double x1;
				double y1;
				double z1;
				
				double x2;
				double y2;
				double z2;
				
				double x3;
				double y3;
				double z3;
				
				x1 = tris[0];
				y1 = tris[1];
				z1 = tris[2];
				
				x2 = tris[3];
				y2 = tris[4];
				z2 = tris[5];
				
				x3 = tris[6];
				y3 = tris[7];
				z3 = tris[8];
				obj = obj + "v "+x1+" "+y1+" "+z1+"\n";
				obj = obj + "v "+x2+" "+y2+" "+z2+"\n";
				obj = obj + "v "+x3+" "+y3+" "+z3+"\n";
			} else {
				double[] tris = triangles[i];
				double x1;
				double y1;
				double z1;
				
				double x2;
				double y2;
				double z2;
				
				double x3;
				double y3;
				double z3;
				
				x1 = tris[0];
				y1 = tris[1];
				z1 = tris[2];
				
				x2 = tris[3];
				y2 = tris[4];
				z2 = tris[5];
				
				x3 = tris[6];
				y3 = tris[7];
				z3 = tris[8];
				obj = obj + "v "+x1+" "+y1+" "+z1+"\n";
				obj = obj + "v "+x2+" "+y2+" "+z2+"\n";
				obj = obj + "v "+x3+" "+y3+" "+z3+"\n";
			}
		}
		obj = obj + "\nusemtl none\n\n";
		HandleIndicesGraphics graphics = new HandleIndicesGraphics();
		int[][] texC = graphics.PngGet();
		if (texC != null) {
			int TC = count(obj, "v");
			int x = 1;
			int y = 1;
			for (int i = 0; i <= TC; i++) {
				if (i % 3 == 0 && i > 0) {
					int a = i-2;
					int b = i-1;
					int c = i-0;
					if (i < texC.length) {
						x = texC[i][0];
						y = texC[i][1];
					}
					obj = obj + "f "+a+"/"+x+" "+b+"/"+y+" "+c+"/1\n";
				}
			}
		} else {
			int TC = count(obj, "v");
			for (int i = 0; i <= TC; i++) {
				if (i % 3 == 0 && i > 0) {
					int a = i-2;
					int b = i-1;
					int c = i-0;
					obj = obj + "f "+a+" "+b+" "+c+"\n";
				}
			}
		}
		return obj;
	}
}