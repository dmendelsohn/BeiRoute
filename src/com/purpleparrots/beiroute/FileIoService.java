package com.purpleparrots.beiroute;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class FileIoService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public FileOutputStream getOutputStream(String localPath) throws FileNotFoundException {
		return openFileOutput(localPath, MODE_PRIVATE);
	}
	
	public FileInputStream getInputStream(String localPath) throws FileNotFoundException {
		return openFileInput(localPath);
	}

}
