// Reads through SpiralSpy XML files and gives back indices and triangles.

package main;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.*;
import org.xml.sax.InputSource;

public class XMLReader {
	
	static double x1 = 0;
	static double x2 = 0;
	static double x3 = 0;

	static double y1 = 0;
	static double y2 = 0;
	static double y3 = 0;

	static double z1 = 0;
	static double z2 = 0;
	static double z3 = 0;
	
	public static InputSource loadXMLFromString(String xml) throws Exception
	{
	    InputSource is = new InputSource(new StringReader(xml));
	    return is;
	}
	
	public int[] getIndices(String file) {
		int[] indices = null;
		try {
        	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(loadXMLFromString(file));
            Element docEle = doc.getDocumentElement();
            NodeList indList = docEle.getElementsByTagName("indices");
            if (indList != null && indList.getLength() > 0) {
            	indices = new int[indList.getLength()];
            	for (int i = 0; i < indList.getLength(); i++) {
            		String index = indList.item(i).getChildNodes().item(0).getNodeValue();
            		String[] INX = index.split(", ");
            		for (int j = 0; j < indList.getLength(); j++) {
            			String IND = INX[j];
            			indices[i] = Integer.parseInt(IND)+1;
            		}
            	}
            }
    	} catch (Exception e) {
        	System.out.println("Unexpected error while getting indices!");
    	}
		return indices;
	}
	
	public boolean checkForValidXML(String file) {
		boolean Valid = false;
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(loadXMLFromString(file));
			Element docEle = doc.getDocumentElement();
			NodeList triList = docEle.getElementsByTagName("java class");
			if (triList != null) {
				Valid = true;
			}
		} catch (Exception e) {
			Valid = false;
    	}
		return Valid;
	}
 
	public double[][] getTriangles(String file) {
		double[][] triangleGroup = null;
    	try {
    		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        	DocumentBuilder db = dbf.newDocumentBuilder();
        	Document doc = db.parse(loadXMLFromString(file));
            Element docEle = doc.getDocumentElement();
            NodeList triList = docEle.getElementsByTagName("triangle");
            if (triList != null && triList.getLength() > 0) {
            	triangleGroup = new double[triList.getLength()][8];
            	for (int i = 0; i < triList.getLength(); i++) {
            		Element va = (Element) triList.item(i);
        			NodeList va1 = va.getElementsByTagName("v1");
        			NodeList va2 = va.getElementsByTagName("v2");
        			NodeList va3 = va.getElementsByTagName("v3");
        			//.getChildNodes().item(0)
        			String pv1 = va1.item(0).getChildNodes().item(0).getNodeValue();
        			String pv2 = va2.item(0).getChildNodes().item(0).getNodeValue();
        			String pv3 = va3.item(0).getChildNodes().item(0).getNodeValue();
        			
        			String v1[] = pv1.split(", ");
        			String v2[] = pv2.split(", ");
        			String v3[] = pv3.split(", ");
        			for (int j = 0; j < 3; j++) {
        				double k = Double.parseDouble(v1[j]);
        				if (j == 0) {
        					x1 = k;
        				} else if (j == 1) {
        					y1 = k;
        				} else if (j >= 2) {
        					z1 = k;
        				}
        			}
        			for (int j = 0; j < 3; j++) {
        				double k = Double.parseDouble(v2[j]);
        				if (j == 0) {
        					x2 = k;
        				} else if (j == 1) {
        					y2 = k;
        				} else if (j >= 2) {
        					z2 = k;
        				}
        			}
        			for (int j = 0; j < 3; j++) {
        				double k = Double.parseDouble(v3[j]);
        				if (j == 0) {
        					x3 = k;
        				} else if (j == 1) {
        					y3 = k;
        				} else if (j >= 2) {
        					z3 = k;
        				}
        			}
        			double[] items = {x1, y1, z1, x2, y2, z2, x3, y3, z3};
        			triangleGroup[i] = items;
            	}
            }
    	} catch(Exception e) {
        	System.out.println("Unexpected error while getting triangles! Error: "+e);
    	}
    	return triangleGroup;
	}
}