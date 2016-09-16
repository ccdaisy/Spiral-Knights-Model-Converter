package xan_code;

import java.awt.image.BufferedImage;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.Set;

import javax.imageio.ImageIO;

import com.threerings.export.*;
import com.threerings.math.Quaternion;
import com.threerings.math.Vector3f;
import com.threerings.opengl.geometry.config.GeometryConfig;
import com.threerings.opengl.model.config.*;
import com.threerings.opengl.model.config.ArticulatedConfig;
import com.threerings.opengl.model.config.ArticulatedConfig.MeshNode;
import com.threerings.opengl.model.config.ArticulatedConfig.Node;
import com.threerings.opengl.model.config.ModelConfig;
import com.threerings.opengl.model.config.ModelConfig.MeshSet;
import com.threerings.opengl.model.config.ModelConfig.VisibleMesh;
import com.threerings.opengl.renderer.config.ClientArrayConfig;

public class DATRead {
	
	private static boolean success = true;
	
	private static int modelcount = 0;
	
	private static short[] Indices = null;
	
	private static VisibleMesh[] msh = new VisibleMesh[500];
	private static Vector3f[] translations = new Vector3f[500];
	private static Quaternion[] rotations = new Quaternion[500];
	private static int mshi = 0; //Shitty way of doing things, I'll fix this later.
	
	private static String header = "#Brought to you by Brent \"Xan\" D.\n\nusemtl ExportMTL\no model";
	private static String obj = header;
	private static String[] objs = null;
	private static int currentOBJ = 0;
	private static int HIGHEST_INDEX = 0;
	
	/**
	 * A method used to get a string of the model type.
	 * @param imp The implementation
	 * @return A string name of the model type
	 */
	@SuppressWarnings("unused")
	private static String getImplementationType(ModelConfig.Implementation imp, boolean IncludeImported) {
		String type = imp.toString();
		try {
			ArticulatedConfig mdl = (ArticulatedConfig) imp;
			type = "Articulated";
		} catch (ClassCastException e) {
			//This is OK
		}
		try {
			CompoundConfig mdl = (CompoundConfig) imp;
			type = "Compound";
		} catch (ClassCastException e) {
			//This is OK
		}
		try {
			ConditionalConfig mdl = (ConditionalConfig) imp;
			type = "Conditional";
		} catch (ClassCastException e) {
			//This is OK
		}
		try {
			ScriptedConfig mdl = (ScriptedConfig) imp;
			type = "Scripted";
		} catch (ClassCastException e) {
			//This is OK
		}
		try {
			StaticConfig mdl = (StaticConfig) imp;
			type = "Static";
		} catch (ClassCastException e) {
			//This is OK
		}
		/*try {
			GeneratedStaticConfig mdl = (GeneratedStaticConfig) imp;
			type = "GeneratedStatic";
		} catch (ClassCastException e) {
			//This is OK
		}*/
		try {
			MergedStaticConfig mdl = (MergedStaticConfig) imp;
			type = "MergedStatic";
		} catch (ClassCastException e) {
			//This is OK
		}
		try {
			StaticSetConfig mdl = (StaticSetConfig) imp;
			type = "StaticSet";
		} catch (ClassCastException e) {
			//This is OK
		}
		
		if (IncludeImported == true) {
			try {
				ModelConfig.Imported mdl = (ModelConfig.Imported) imp;
				type = getImplementationType(imp, false);
			} catch (ClassCastException e) {
				//This is OK
			}
		}
		
		//Now it's these two special snowflakes
		try {
			ModelConfig.Derived mdl = (ModelConfig.Derived) imp;
			type = "Derived";
		} catch (ClassCastException e) {
			//This is OK
		}
		try {
			ModelConfig.Schemed mdl = (ModelConfig.Schemed) imp;
			type = "Schemed";
		} catch (ClassCastException e) {
			//This is OK
		}
		return type;
	}
	
	/**
	 * Construct a new .DAT Reading process.
	 * This method takes in a binary file and uses {@link com.threerings.export.BinaryImporter} to read it.
	 * @param dat The .DAT file
	 */
	public DATRead (File dat) {
		//reset everything
		HIGHEST_INDEX = 0;
		mshi = 0;
		currentOBJ = 0;
		modelcount = 0;
		msh = new VisibleMesh[500];
		translations = new Vector3f[500];
		rotations = new Quaternion[500];
		objs = null;
		//k
		try {
			@SuppressWarnings("resource")
			BinaryImporter importer = new BinaryImporter(new FileInputStream(dat));
			try {
				while (true) {
					Object obj = importer.readObject();
					ModelConfig mcf = (ModelConfig) obj;
					ModelConfig.Implementation imp = mcf.implementation;
					//Fields to string
					//Note: offsets and strides are in "skin" parameter of implementation
					String impType = getImplementationType(imp, true);
					success = true;
					if (impType == "Articulated") {
						Logger.AppendLn("Got model. Type=Articulated");
						getArticulatedData(imp, dat);
						
					} else if (impType == "Compound") {
						Logger.AppendLn("Got model. Type=Compound");
						getCompoundData(imp, dat);
						
					} else if (impType == "Conditional") {
						Logger.AppendLn("Got model. Type=Conditional");
						Logger.AppendLn("WARNING: Not ready to read conditional models!");
						success = false;
						return;
					} else if (impType == "Scripted") {
						Logger.AppendLn("Got script. (Implementation of the .DAT is Scripted.)");
						Logger.AppendLn("Scripts cannot be converted to models.");
						success = false;
						return;
					} else if (impType == "Static") {
						Logger.AppendLn("Got model. Type=Static");
						getStaticData(imp, dat);
						
					} else if (impType == "Derived") {
						Logger.AppendLn("Got model. Type=Derived");
						getDerivedData(imp, dat);
						
					} else if (impType == "Schemed") {
						Logger.AppendLn("Got model. Type=Schemed");
						getSchemedData(imp, dat);
						
					} else if (impType == "StaticSet") {
						Logger.AppendLn("Got model. Type=StaticSet");
						getStaticSetData(imp, dat);
						
					} else if (impType == "MergedStatic") {
						getMergedStaticData(imp, dat);
						
					} else {
						Logger.AppendLn("Got model. Type=Imported");
						Logger.AppendLn("If this type is achieved, it means it carries a model reference.");
						Logger.AppendLn("We'll try load this reference for you.");
						getImportedData(imp, dat, "imported");
						success = false;
						return;
					}
					
				}
			} catch (EOFException e) {
				//this is fine
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected static BufferedImage getImageFrom(File image) {
		try {
			return ImageIO.read(image);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * A currently unused method that will soon output an AutoDesk FBX file.
	 * @param writeTo the file to write data to
	 */
	public static void ConvertDatToFBX(File writeTo) {
		
	}
	
	/**
	 * Reads the data inside of the model and converts it to the WaveFront OBJ format.
	 * @param writeTo the file to write data to
	 */
	public static void ConvertDatToOBJ(File writeTo) {
		if (!success) {
			Logger.AppendLn("Unable to export model.");
			return;
		}
 		
		Logger.AppendLn("Writing file(s)...");
		
		if (!writeTo.isDirectory()) {
			writeTo.mkdirs();
		}
		
		String newObjTxt = "";
		
		for (int i = 0; i < objs.length; i++) {
			if (objs[i] != null) {
				//writeToFile(new File(writeTo.getPath() + "\\model_number_"+i+".obj"), objs[i]);
				newObjTxt = newObjTxt + objs[i];
			} else {
				break;
			}
		}
		
		File[] f = new File(writeTo.getPath()).listFiles();
		File test = new File(writeTo.getPath() + "/ExportedModel_"+f.length+".obj");
		if (test.exists()) {
			writeToFile(new File(writeTo.getPath() + "/ExportedModel2_"+f.length+".obj"), newObjTxt);
		} else {
			writeToFile(new File(writeTo.getPath() + "/ExportedModel_"+f.length+".obj"), newObjTxt);
		}
		
		Logger.AppendLn("Exported!");
		
		obj = header; //Reset the data
		modelcount = 0;
	}
	
	/**
	 * Appends the OBJ text to itself, this is good for multiple-model pieces
	 */
	@SuppressWarnings("unused")
	protected static void AppendToOBJ(Vector3f trs, Quaternion rot, int[] Strides, int[] Offsets, FloatBuffer[] Buffers) {
		if (!success) {
			Logger.AppendLn("Unable to export model.");
			return;
		}
 		obj = header+currentOBJ+"\n";
 		
 		/*
 		 *	int[] Strides			= {s_uv, s_va, s_no, s_vt};
		 *	int[] Offsets			= {o_uv, o_va, o_no, o_vt};
		 *	FloatBuffer[] Buffers	= {f_uv, f_va, f_no, f_vt};
 		 */
 		
 		int s_uv = Strides[0];
 		int s_va = Strides[1];
 		int s_no = Strides[2];
 		int s_vt = Strides[3];
 		
 		int o_uv = Offsets[0];
 		int o_va = Offsets[1];
 		int o_no = Offsets[2];
 		int o_vt = Offsets[3];
 		
 		FloatBuffer f_uv = Buffers[0];
 		FloatBuffer f_va = Buffers[1];
 		FloatBuffer f_no = Buffers[2];
 		FloatBuffer f_vt = Buffers[3];
		
 		System.out.println("Appending OBJ");
		System.out.println("Vert stride: "+s_vt);
		System.out.println("Vert offset: "+o_vt);
		System.out.println("UV stride: "+s_uv);
		System.out.println("UV offset: "+o_uv);
		System.out.println("Normals stride: "+s_no);
		System.out.println("Normals offset: "+o_no);
		System.out.println("VertAttrib stride: "+s_va);
		System.out.println("VertAttrib offset: "+o_va);
		
		Vector3f trans = new Vector3f(0f, 0f, 0f);
		try {
			trans = rot.transform(trs);
		} catch (NullPointerException e) {
			//if this happens, we have no rotation/translation.
		}
		
		
		int triangles = 0;
		int counted_verts = 0;
		int stride = s_vt / 4;
		int offset = o_vt / 4;
		for (int i = 0; i < f_vt.capacity(); i++) {
			try {
				int index = (int) Indices[i];
				int t = index*stride;
				int a = t+offset+0;
				int b = t+offset+1;
				int c = t+offset+2;
				//Blender uses "x, z, y". Need to reformat. Use x, z, -y
				//obj = obj + "v "+f_vt.get(a)+" "+f_vt.get(b)+" "+f_vt.get(c)+"\n";
				obj = obj + "v "+(f_vt.get(a) + trans.x)+" "+(f_vt.get(c) + trans.z)+" "+ (-(f_vt.get(b) + trans.y)) +"\n";
				counted_verts++;
				triangles++;
			} catch (ArrayIndexOutOfBoundsException e) {
				
			}
		}
		
		obj = obj + "\n\n";
		
		stride = s_uv / 4;
		offset = o_uv / 4;
		
		for (int i = 0; i < f_uv.capacity(); i++) {
			try {
				int index = (int) Indices[i];
				int t = index*stride;
				int a = t+offset+0;
				int b = t+offset+1;
				obj = obj + "vt "+f_uv.get(a)+" "+f_uv.get(b)+"\n";
			} catch (ArrayIndexOutOfBoundsException e) {
				
			}
		}
		
		obj = obj + "\n\n";
		
		int highest_index = 0;
		for (int i = 0; i < f_vt.capacity(); i++) {
			try {
				int index = ((int) Indices[i]) + 1;
				int i3 = index* 3;
				int i2 = i3 - 1;
				int i1 = i3 - 2;
				
				if (i3 > highest_index) {
					highest_index = i3;
				}
				
				if (i3 <= triangles) {
					i1 += HIGHEST_INDEX;
					i2 += HIGHEST_INDEX;
					i3 += HIGHEST_INDEX;
					obj = obj + "f "+i1+"/"+i1+" "+i2+"/"+i2+" "+i3+"/"+i3+"\n";
				} else {
					break;
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				
			}
		}
		for (int i = 0; i < (Indices.length-highest_index); i++) {
			int a = i + highest_index + 1;
			int b = a + 1;
			int c = a + 2;
			if (c <= triangles && c <= f_uv.capacity()) {
				a += HIGHEST_INDEX;
				b += HIGHEST_INDEX;
				c += HIGHEST_INDEX;
				obj = obj + "f "+a+"/"+a+" "+b+"/"+b+" "+c+"/"+c+"\n";
			}
		}
		
		HIGHEST_INDEX+=counted_verts;
		
		obj = obj + "\n\n\n\n";
		
		objs[modelcount] = obj;
		
		currentOBJ++;
	}
	
	protected static void ResetNodes() {
		msh = new VisibleMesh[500];
		mshi = 0;
	}
	
	protected static void Tree(Node[] nodes) {
		for (int i = 0; i < nodes.length; i++) {
			Node n = nodes[i];
			try {
				MeshNode node = (MeshNode) n;
				msh[mshi] = node.visible;
				translations[mshi] = node.invRefTransform.extractTranslation().mult(-1);
				rotations[mshi] = node.invRefTransform.extractRotation().invert();
				mshi++;
			} catch (ClassCastException e) {
				//This is ok
			}
			
			if (n.children.length > 0) {
				Tree(n.children);
			} 
		}
	}
	
	@SuppressWarnings("unused")
	protected static void getImportedData(ModelConfig.Implementation imp, File dat, String prevType) {
		boolean worked = false;
		Logger.AppendLn();
		ModelConfig.Imported i2 = null;
		try {
			i2 = (ModelConfig.Imported) imp;
			worked = true;
		} catch (ClassCastException e) {
			//This is sort-of expected.
			worked = false;
		}
		if (!worked) {
			Logger.AppendLn("Oh no!\n");
			Logger.AppendLn("Unfortunately, this model could not be converted.");
			Logger.AppendLn("This means that the \"model\" probably isn't an actual 3D model, meaning one of two things:");
			Logger.AppendLn("1: You've imported something that isn't a model at all (Script, Animation, etc)");
			Logger.AppendLn("2: This IS a model, but it does not contain geometry. (Effect, Particle, Skybox, etc) - These kinds of models are usually attached to other models. Try a different file.");
			success = false;
			return;
		}
		try {
			ArticulatedConfig i = (ArticulatedConfig) i2;
			worked = true;
			Logger.AppendLn("Model proxy created. Transforming "+prevType+" instance into articulated instance...");
			getArticulatedData(imp, dat);
		} catch (ClassCastException e) {
			//This is expected
			worked = false;
		}
		try {
			StaticConfig i = (StaticConfig) i2;
			worked = true;
			Logger.AppendLn("Model proxy created. Transforming "+prevType+" instance into static instance...");
			getStaticData(imp, dat);
		} catch (ClassCastException e) {
			//This is expected
			worked = false;
		}
		try {
			StaticSetConfig i = (StaticSetConfig) i2;
			worked = true;
			Logger.AppendLn("Model proxy created. Transforming "+prevType+" instance into static set instance...");
			getStaticSetData(imp, dat);
		} catch (ClassCastException e) {
			//This is expected
			worked = false;
		}
		if (!worked) {
			Logger.AppendLn("Oh no!\n");
			Logger.AppendLn("Unfortunately, this model could not be converted.");
			Logger.AppendLn("This means that the \"model\" probably isn't an actual 3D model, meaning one of two things:");
			Logger.AppendLn("1: You've imported something that isn't a model at all (Script, Animation, etc)");
			Logger.AppendLn("2: This IS a model, but it does not contain geometry. (Effect, Particle, Skybox, etc) - These kinds of models are usually attached to other models. Try a different file.");
			success = false;
			return;
		}
	}
	
	protected static void getMergedStaticData(ModelConfig.Implementation imp, File dat) {
		MergedStaticConfig cmp = (MergedStaticConfig) imp; //This is pretty much the same as compound, it even uses CompoundConfig
		objs = new String[500];
		Logger.AppendLn("Loaded merged static implementation.");
		String datPath = dat.getPath();
		for (int i = 0; i < datPath.length()-6; i++) {
			String piece = datPath.substring(i, i+6);
			if (piece.equalsIgnoreCase("/rsrc/") || piece.equalsIgnoreCase("\\rsrc\\")) {
				datPath = datPath.substring(0, i+6);
				break;
			}
		}
		
		datPath = datPath.replace("\\", "/");
		
		
		String n = cmp.models[0].model.getName();
		System.out.println(n);
		int i = n.lastIndexOf("/")+1;
		String p = n.substring(0, i);
		String f = n.substring(i);
		
		File newDat = new File(datPath+p, f);
		try {
			@SuppressWarnings("resource")
			BinaryImporter importer = new BinaryImporter(new FileInputStream(newDat));
			Object obj = importer.readObject();
			ModelConfig mcf = (ModelConfig) obj;
			ModelConfig.Implementation i2 = mcf.implementation;
			if (i2 instanceof ArticulatedConfig) {
				Logger.AppendLn("Model proxy created. Transforming merged static instance into articulated instance...");
				getArticulatedData(i2, newDat);
			} else if (i2 instanceof StaticConfig) {
				Logger.AppendLn("Model proxy created. Transforming merged static instance into static instance...");
				getStaticData(i2, newDat);
			} else if (i2 instanceof CompoundConfig) {
				Logger.AppendLn("Model proxy created. Transforming merged static instance into compound instance...");
				getCompoundData(i2, newDat);
			} else if (i2 instanceof ModelConfig.Schemed) {
				Logger.AppendLn("Model proxy created. Transforming merged static instance into schemed instance...");
				getSchemedData(i2, newDat);
			} else if (i2 instanceof ModelConfig.Derived) {
				Logger.AppendLn("Model proxy created. Transforming merged static instance into derived instance...");
				getDerivedData(i2, newDat);
			} else if (i2 instanceof MergedStaticConfig) {
				Logger.AppendLn("Model proxy created. Transforming merged static instance into (another) merged static instance...");
				getMergedStaticData(i2, newDat);
			} else if (i2 instanceof StaticSetConfig) {
				Logger.AppendLn("Model proxy created. Transforming merged static instance into static set instance...");
				getStaticSetData(i2, newDat);
			} else {
				try { 
					Logger.AppendLn("Model proxy is being created...");
					getImportedData(i2, newDat, "merged static");
				} catch (ClassCastException e) {
					//moo
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected static void getCompoundData(ModelConfig.Implementation imp, File dat) {
		CompoundConfig cmp = (CompoundConfig) imp;
		objs = new String[500];
		Logger.AppendLn("Loaded compound implementation.");
		String datPath = dat.getPath();
		for (int i = 0; i < datPath.length()-6; i++) {
			String piece = datPath.substring(i, i+6);
			if (piece.equalsIgnoreCase("/rsrc/") || piece.equalsIgnoreCase("\\rsrc\\")) {
				datPath = datPath.substring(0, i+6);
				break;
			}
		}
		
		datPath = datPath.replace("\\", "/");
		
		
		String n = cmp.models[0].model.getName();
		System.out.println(n);
		int i = n.lastIndexOf("/")+1;
		String p = n.substring(0, i);
		String f = n.substring(i);
				
		File newDat = new File(datPath+p, f);
		try {
			@SuppressWarnings("resource")
			BinaryImporter importer = new BinaryImporter(new FileInputStream(newDat));
			Object obj = importer.readObject();
			ModelConfig mcf = (ModelConfig) obj;
			ModelConfig.Implementation i2 = mcf.implementation;
			if (i2 instanceof ArticulatedConfig) {
				Logger.AppendLn("Model proxy created. Transforming compound instance into articulated instance...");
				getArticulatedData(i2, newDat);
			} else if (i2 instanceof StaticConfig) {
				Logger.AppendLn("Model proxy created. Transforming compound instance into static instance...");
				getStaticData(i2, newDat);
			} else if (i2 instanceof CompoundConfig) {
				Logger.AppendLn("Model proxy created. Transforming compound instance into (another) compound instance...");
				getCompoundData(i2, newDat);
			} else if (i2 instanceof ModelConfig.Schemed) {
				Logger.AppendLn("Model proxy created. Transforming compound instance into schemed instance...");
				getSchemedData(i2, newDat);
			} else if (i2 instanceof ModelConfig.Derived) {
				Logger.AppendLn("Model proxy created. Transforming compound instance into derived instance...");
				getDerivedData(i2, newDat);
			} else if (i2 instanceof MergedStaticConfig) {
				Logger.AppendLn("Model proxy created. Transforming compound instance into merged static instance...");
				getMergedStaticData(i2, newDat);
			} else if (i2 instanceof StaticSetConfig) {
				Logger.AppendLn("Model proxy created. Transforming compound instance into static set instance...");
				getStaticSetData(i2, newDat);
			} else {
				try { 
					Logger.AppendLn("Model proxy is being created...");
					getImportedData(i2, newDat, "compound");
				} catch (ClassCastException e) {
					//moo
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected static void getSchemedData(ModelConfig.Implementation imp, File dat) {
		ModelConfig.Schemed sch = (ModelConfig.Schemed) imp;
		objs = new String[500];
		Logger.AppendLn("Loaded schemed implementation.");
		String datPath = dat.getPath();
		for (int i = 0; i < datPath.length()-6; i++) {
			String piece = datPath.substring(i, i+6);
			if (piece.equalsIgnoreCase("/rsrc/") || piece.equalsIgnoreCase("\\rsrc\\")) {
				datPath = datPath.substring(0, i+6);
				break;
			}
		}
		
		datPath = datPath.replace("\\", "/");
		
		
		String n = sch.models[0].model.getName();
		System.out.println(n);
		int i = n.lastIndexOf("/")+1;
		String p = n.substring(0, i);
		String f = n.substring(i);
				
		File newDat = new File(datPath+p, f);
		try {
			@SuppressWarnings("resource")
			BinaryImporter importer = new BinaryImporter(new FileInputStream(newDat));
			Object obj = importer.readObject();
			ModelConfig mcf = (ModelConfig) obj;
			ModelConfig.Implementation i2 = mcf.implementation;
			if (i2 instanceof ArticulatedConfig) {
				Logger.AppendLn("Model proxy created. Transforming schemed instance into articulated instance...");
				getArticulatedData(i2, newDat);
			} else if (i2 instanceof StaticConfig) {
				Logger.AppendLn("Model proxy created. Transforming schemed instance into static instance...");
				getStaticData(i2, newDat);
			} else if (i2 instanceof CompoundConfig) {
				Logger.AppendLn("Model proxy created. Transforming schemed instance into compound instance...");
				getCompoundData(i2, newDat);
			} else if (i2 instanceof ModelConfig.Schemed) {
				Logger.AppendLn("Model proxy created. Transforming schemed instance into (another) schemed instance...");
				getSchemedData(i2, newDat);
			} else if (i2 instanceof ModelConfig.Derived) {
				Logger.AppendLn("Model proxy created. Transforming schemed instance into derived instance...");
				getDerivedData(i2, newDat);
			} else if (i2 instanceof MergedStaticConfig) {
				Logger.AppendLn("Model proxy created. Transforming schemed instance into merged static instance...");
				getMergedStaticData(i2, newDat);
			} else if (i2 instanceof StaticSetConfig) {
				Logger.AppendLn("Model proxy created. Transforming schemed instance into static set instance...");
				getStaticSetData(i2, newDat);
			} else {
				try { 
					Logger.AppendLn("Model proxy is being created...");
					getImportedData(i2, newDat, "schemed");
				} catch (ClassCastException e) {
					//moo
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected static void getDerivedData(ModelConfig.Implementation imp, File dat) {
		ModelConfig.Derived der = (ModelConfig.Derived) imp;
		objs = new String[500];
		Logger.AppendLn("Loaded derived implementation.");
		String datPath = dat.getPath();
		for (int i = 0; i < datPath.length()-6; i++) {
			String piece = datPath.substring(i, i+6);
			if (piece.equalsIgnoreCase("/rsrc/") || piece.equalsIgnoreCase("\\rsrc\\")) {
				datPath = datPath.substring(0, i+6);
				break;
			}
		}
		
		datPath = datPath.replace("\\", "/");
		
		
		String n = der.model.getName();
		int i = n.lastIndexOf("/")+1;
		String p = n.substring(0, i);
		String f = n.substring(i);
				
		File newDat = new File(datPath+p, f);
		try {
			@SuppressWarnings("resource")
			BinaryImporter importer = new BinaryImporter(new FileInputStream(newDat));
			Object obj = importer.readObject();
			ModelConfig mcf = (ModelConfig) obj;
			ModelConfig.Implementation i2 = mcf.implementation;
			if (i2 instanceof ArticulatedConfig) {
				Logger.AppendLn("Model proxy created. Transforming derived instance into articulated instance...");
				getArticulatedData(i2, newDat);
			} else if (i2 instanceof StaticConfig) {
				Logger.AppendLn("Model proxy created. Transforming derived instance into static instance...");
				getStaticData(i2, newDat);
			} else if (i2 instanceof CompoundConfig) {
				Logger.AppendLn("Model proxy created. Transforming derived instance into compound instance...");
				getCompoundData(i2, newDat);
			} else if (i2 instanceof ModelConfig.Schemed) {
				Logger.AppendLn("Model proxy created. Transforming derived instance into schemed instance...");
				getSchemedData(i2, newDat);
			} else if (i2 instanceof ModelConfig.Derived) {
				Logger.AppendLn("Model proxy created. Transforming derived instance into (another) derived instance...");
				getDerivedData(i2, newDat);
			} else if (i2 instanceof MergedStaticConfig) {
				Logger.AppendLn("Model proxy created. Transforming derived instance into merged static instance...");
				getMergedStaticData(i2, newDat);
			} else if (i2 instanceof StaticSetConfig) {
				Logger.AppendLn("Model proxy created. Transforming derived instance into static set instance...");
				getStaticSetData(i2, newDat);
			} else {
				try { 
					Logger.AppendLn("Model proxy is being created...");
					getImportedData(i2, newDat, "derived");
				} catch (ClassCastException e) {
					//moo
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected static void getStaticSetData(ModelConfig.Implementation imp, File dat) {
		StaticSetConfig sc = (StaticSetConfig) imp;
		objs = new String[500];
		Logger.AppendLn("Loaded static set implementation.");
		int msize = sc.meshes.size();
		
		MeshSet[] meshsets = new MeshSet[msize];
		Set<String> keys_set = sc.meshes.keySet();
		Object[] keys_o = keys_set.toArray();
		String[] keys = new String[keys_o.length];
		for (int i = 0; i < keys_o.length; i++) {
			keys[i] = String.valueOf(keys_o[i]);
		}
		for (int i = 0; i < msize; i++) {
			if (i < keys.length) {
				meshsets[i] = sc.meshes.get(keys[i]);
			} else {
				break;
			}
		}
		//We have our data, now we need to get the highest amount of meshes inside
		int meshsize = 1;
		for (int i = 0; i < msize; i++) {
			meshsize = Math.max(meshsize, meshsets[i].visible.length);
		}
		
		//Now we make our dual-array of VisibleMesh
		VisibleMesh[][] meshes = new VisibleMesh[msize][meshsize];
		
		for (int i = 0; i < msize; i++) {
			meshes[i] = meshsets[i].visible;
		}
		
		//And now we read
		
		for (int q = 0; q < msize; q++) {
			for (int i = 0; i < meshsize && i < meshes[q].length; i++) {
				if (meshes[q] != null && meshes[q][i] != null) {
					GeometryConfig geo = meshes[q][i].geometry;
					GeometryConfig.Stored sgeo = (GeometryConfig.Stored) geo;
					GeometryConfig.IndexedStored igeo = (GeometryConfig.IndexedStored) geo;
					
					
					ClientArrayConfig uvs;
					ClientArrayConfig normals;
					ClientArrayConfig verts;
					if (ClientArrayHasData(sgeo.vertexArray) && ClientArrayHasData(sgeo.texCoordArrays[0])) {
						uvs = sgeo.texCoordArrays[0];
						normals = sgeo.normalArray;
						verts = sgeo.vertexArray;
					} else {
						uvs = igeo.texCoordArrays[0];
						normals = igeo.normalArray;
						verts = igeo.vertexArray;
					}
					
					int s_uv = uvs.stride;
					int s_va = 0;				//No bones in static models
					int s_no = normals.stride;
					int s_vt = verts.stride;
					
					int o_uv = uvs.offset;
					int o_va = 0;				//No bones in static models
					int o_no = normals.offset;
					int o_vt = verts.offset;
					
					FloatBuffer f_uv = uvs.floatArray;
					FloatBuffer f_va = null;	//No bones in static models
					FloatBuffer f_no = normals.floatArray;
					FloatBuffer f_vt = verts.floatArray;
					
					int[] Strides			= {s_uv, s_va, s_no, s_vt};
					int[] Offsets			= {o_uv, o_va, o_no, o_vt};
					FloatBuffer[] Buffers	= {f_uv, f_va, f_no, f_vt};
					
					Indices = new short[igeo.indices.capacity()];
					igeo.indices.get(Indices);
					AppendToOBJ(new Vector3f(0, 0, 0), new Quaternion(1, 0, 0, 0), Strides, Offsets, Buffers); //Append the current data
					modelcount++;
				}
			}
		}
		Logger.AppendLn("Handled "+meshes.length+" model(s), each containing "+meshsize+" sub-models.");
		Logger.AppendLn("Mesh information acquired! Ready to convert.");
	}
	
	protected static void getStaticData(ModelConfig.Implementation imp, File dat) {
		StaticConfig sc = (StaticConfig) imp;
		Logger.AppendLn("Loaded static implementation.");
		VisibleMesh[] meshes = sc.meshes.visible;
		Logger.AppendLn("Loaded mesh.");
		HIGHEST_INDEX = 0;
		objs = new String[500];
		for (int i = 0; i < meshes.length; i++) {
			GeometryConfig geo = meshes[i].geometry;
			GeometryConfig.Stored sgeo = (GeometryConfig.Stored) geo;
			GeometryConfig.IndexedStored igeo = (GeometryConfig.IndexedStored) geo;
			
			
			ClientArrayConfig uvs;
			ClientArrayConfig normals;
			ClientArrayConfig verts;
			if (ClientArrayHasData(sgeo.vertexArray) && ClientArrayHasData(sgeo.texCoordArrays[0])) {
				uvs = sgeo.texCoordArrays[0];
				normals = sgeo.normalArray;
				verts = sgeo.vertexArray;
			} else {
				uvs = igeo.texCoordArrays[0];
				normals = igeo.normalArray;
				verts = igeo.vertexArray;
			}
			
			int s_uv = uvs.stride;
			int s_va = 0;				//No bones in static models
			int s_no = normals.stride;
			int s_vt = verts.stride;
			
			int o_uv = uvs.offset;
			int o_va = 0;				//No bones in static models
			int o_no = normals.offset;
			int o_vt = verts.offset;
			
			FloatBuffer f_uv = uvs.floatArray;
			FloatBuffer f_va = null;	//No bones in static models
			FloatBuffer f_no = normals.floatArray;
			FloatBuffer f_vt = verts.floatArray;
			
			int[] Strides			= {s_uv, s_va, s_no, s_vt};
			int[] Offsets			= {o_uv, o_va, o_no, o_vt};
			FloatBuffer[] Buffers	= {f_uv, f_va, f_no, f_vt};
			
			Indices = new short[igeo.indices.capacity()];
			igeo.indices.get(Indices);
			AppendToOBJ(new Vector3f(0, 0, 0), new Quaternion(1, 0, 0, 0), Strides, Offsets, Buffers); //Append the current data
			modelcount++;
		}
		Logger.AppendLn("Handled 1 model containing "+meshes.length+" sub-models.");
		Logger.AppendLn("Mesh information acquired! Ready to convert.");
	}
	
	protected static void getArticulatedData(ModelConfig.Implementation imp, File dat) {
		ArticulatedConfig art = (ArticulatedConfig) imp;
		Logger.AppendLn("Loaded articulated implementation.");
		VisibleMesh[] meshes = art.skin.visible;
		Node[] nodes = art.root.children;
		Logger.AppendLn("Loaded mesh.");
		HIGHEST_INDEX = 0;
		ResetNodes();
		if (meshes.length == 0) {
			Tree(nodes);
			meshes = msh;
		}
		int smdl = 0;
		objs = new String[500];
		for (int i = 0; i < meshes.length; i++) {
			if (meshes[i] == null) {
				break; //Expected.
			}
			smdl++;
			GeometryConfig geo = meshes[i].geometry;
			GeometryConfig.Stored sgeo = (GeometryConfig.Stored) geo;
			GeometryConfig.IndexedStored igeo = (GeometryConfig.IndexedStored) geo;
			
			ClientArrayConfig uvs;
			ClientArrayConfig attrib;
			ClientArrayConfig normals;
			ClientArrayConfig verts;
			if (ClientArrayHasData(sgeo.vertexArray) && ClientArrayHasData(sgeo.texCoordArrays[0])) {
				uvs = sgeo.texCoordArrays[0];
				attrib = sgeo.vertexAttribArrays != null ? (sgeo.vertexAttribArrays.length > 0 ? sgeo.vertexAttribArrays[0] : new ClientArrayConfig()) : null;
				normals = sgeo.normalArray;
				verts = sgeo.vertexArray;
			} else {
				uvs = igeo.texCoordArrays[0];
				attrib = igeo.vertexAttribArrays != null ? (igeo.vertexAttribArrays.length > 0 ? igeo.vertexAttribArrays[0] : new ClientArrayConfig()) : null;
				normals = igeo.normalArray;
				verts = igeo.vertexArray;
			}
			
			int s_uv = uvs.stride;
			int s_va = attrib != null ? attrib.stride : 0;
			int s_no = normals.stride;
			int s_vt = verts.stride;
			
			int o_uv = uvs.offset;
			int o_va = attrib != null ? attrib.offset : 0;
			int o_no = normals.offset;
			int o_vt = verts.offset;
			
			FloatBuffer f_uv = uvs.floatArray;
			FloatBuffer f_va = attrib != null ? attrib.floatArray : null;
			FloatBuffer f_no = normals.floatArray;
			FloatBuffer f_vt = verts.floatArray;
			
			int[] Strides			= {s_uv, s_va, s_no, s_vt};
			int[] Offsets			= {o_uv, o_va, o_no, o_vt};
			FloatBuffer[] Buffers	= {f_uv, f_va, f_no, f_vt};
			
			Indices = new short[igeo.indices.capacity()];
			igeo.indices.get(Indices);
			AppendToOBJ(translations[i], rotations[i], Strides, Offsets, Buffers); //Append the current data
			modelcount++;
		}
		Logger.AppendLn("Handled 1 model containing "+Math.min(smdl, meshes.length)+" sub-models.");
		Logger.AppendLn("Mesh information acquired! Ready to convert.");
	}
	
	
	protected static boolean ClientArrayHasData(ClientArrayConfig cfg) {
		if (cfg.floatArray.limit() > 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * Used for writing data into files.
	 * @param writeTo The file to write to
	 * @param contents The contents of the file as a String
	 */
	protected static void writeToFile(File writeTo, String contents) {
        FileWriter fileWriter;
        try {
            fileWriter = new FileWriter(writeTo);
            fileWriter.write(contents);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
    }
}