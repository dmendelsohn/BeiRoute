package com.purpleparrots.beiroute;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Hashtable;
import java.util.StringTokenizer;

import com.google.gson.Gson;

public abstract class AndroidSerializable {
	
	private static Gson g = new Gson();
	private static FileIoService fileIo = new FileIoService();
	
	private static void serialize(int id, AndroidSerializable obj) throws IOException {
		String json = g.toJson(obj);
		String FILENAME = "" + id;
		OutputStreamWriter osw = fileIo.getOutputStream(FILENAME);
		g.toJson(obj, osw);
		osw.close();
	}
	
	public static void serializeTable(Hashtable<Integer, AndroidSerializable> table, String tableName) throws IOException {
		String s = "";
		for (int k: table.keySet()) {
			serialize(k, table.get(k));
			s += "" + k + " ";
		}
		OutputStreamWriter osw = fileIo.getOutputStream(tableName);
		osw.write(s);
		osw.close();
	}
	
	private static AndroidSerializable deserialize(int id) throws IOException {
		String FILENAME = "" + id;
		InputStreamReader isr = fileIo.getInputStream(FILENAME);
		AndroidSerializable obj = g.fromJson(isr, AndroidSerializable.class);
		isr.close();
		return obj;
	}
	
	public static Hashtable<Integer, AndroidSerializable> deserializeTable(String tableName) throws IOException {
		Hashtable<Integer, AndroidSerializable> table = new Hashtable<Integer, AndroidSerializable>();
		InputStreamReader isr = fileIo.getInputStream(tableName);
		String s = "";
		int a = isr.read();
		while (a != -1) {
			s += (char) a;
		}
		StringTokenizer st = new StringTokenizer(s);
		int total = st.countTokens();
		int[] id = new int[total];
		for (int i = 0; i < total; i++) {
			int k = Integer.parseInt(st.nextToken());
			table.put(k, deserialize(k));
		}
		return table;
	}
}
