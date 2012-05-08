package com.purpleparrots.beiroute;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class FileIoService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public int onStartCommand (Intent intent, int flags, int startId) {
		return 0;
	}
	
	public OutputStreamWriter getOutputStream(String localPath) throws FileNotFoundException {
		Log.w("FileIoService.getOutputStream()", localPath);
		FileOutputStream fos = openFileOutput(localPath, MODE_PRIVATE);
		Log.w("FileIoService.getOutputStream()", fos.toString());
		OutputStreamWriter osw = new OutputStreamWriter(fos);
		Log.w("FileIoService.getOutputStream()", osw.toString());
		return osw;
		
	}
	
	public InputStreamReader getInputStream(String localPath) throws FileNotFoundException {
		return new InputStreamReader(openFileInput(localPath));
	}

}
