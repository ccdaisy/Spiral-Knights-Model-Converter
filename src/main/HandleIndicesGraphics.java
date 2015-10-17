package main;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLDecoder;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;


public class HandleIndicesGraphics {
	static int area;
	static int spaceholder;
	public String GetCurrentDirectory() {
		String a = "";
		String path = HandleIndicesGraphics.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		try {
			a = URLDecoder.decode(path, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return a;
	}
	
	public int[][] PngGet() throws IOException {
		int[][] texcoords = null;
		InputStream in2;
		String path = GetCurrentDirectory();
		String PTH = path+"Tex.png";
		File tex = new File(PTH);
		if (tex.exists()) {
			try {
				in2 = new FileInputStream(tex);
				BufferedImage in = ImageIO.read(in2);
				int width = in.getWidth();
				int height = in.getHeight();
				area = width*height;
				texcoords = new int[width*height][2];
				int f = 0;
				for (int x = 0; x < width; x++) {
					for (int y = 0; y < height; y++) {
						int RGBA = in.getRGB(x, y);
						byte[] rgba = ByteBuffer.allocate(4).putInt(RGBA).array();
						int R = rgba[0];
						int G = rgba[1];
						int B = rgba[2];
						int A = rgba[3];
						spaceholder = R+G+B+A;
						int[] xy = {x, y};
						texcoords[f] = xy;
						f++;
					}
				}
			} catch (Exception e) {
				//Nothing
				System.out.println("Error while getting image! ("+e+")");
			}
		} else {
			System.out.println("No Tex.png file found. Ignoring textures.");
			return null;
		}
		return texcoords;
	}
}