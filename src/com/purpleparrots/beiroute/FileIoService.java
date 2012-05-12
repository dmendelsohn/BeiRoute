package com.purpleparrots.beiroute;

import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class FileIoService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public OutputStreamWriter getOutputStream(String localPath) throws FileNotFoundException {
		return new OutputStreamWriter(openFileOutput(localPath, MODE_PRIVATE));
		
	}
	
	public InputStreamReader getInputStream(String localPath) throws FileNotFoundException {
		return new InputStreamReader(openFileInput(localPath));
	}

}
