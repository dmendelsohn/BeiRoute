package com.purpleparrots.beiroute;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;

import com.google.gson.Gson;

public abstract class AndroidSerializable {
	
	private String directory;
	private static Gson g = new Gson();
	private static FileIoService fileIo = new FileIoService();
	
	public static void serialize(int id, AndroidSerializable obj) throws IOException {
		String json = g.toJson(obj);
		String FILENAME = obj.directory + "/" + id;
		
		FileOutputStream fos;
		fos = fileIo.getOutputStream(FILENAME);
		fos.write(json.getBytes());
		fos.close();
	}
	
	public static void serializeAll(Hashtable<Integer, AndroidSerializable> table) {
		
	}
	
	public static AndroidSerializable deserialize(int id) {
		return null;
	}
	
	public static Hashtable<Integer, AndroidSerializable> deserializeAll() {
		return null;
	}
}
