package com.example.promedia_timelapsed;

//import android.R;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import com.example.promedia_timerbased.R;

import android.app.Activity;
import android.content.ContentValues;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener, SurfaceHolder.Callback, Camera.PictureCallback{
	
	SurfaceView cameraView;
	SurfaceHolder surfaceHolder;
	Camera camera;
	
	Button startStopButton;
	TextView countdownTextView;
	
	Handler timerUpdateHandler;
	boolean timelapseRunning = false;
	int currentTime = 0;
	public static final int SECONDS_BETWEEN_PHOTOS = 60; //one minute
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		cameraView = (SurfaceView) this.findViewById(R.id.CameraView);
		surfaceHolder = cameraView.getHolder();
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		surfaceHolder.addCallback(this);
		
		countdownTextView = (TextView) findViewById(R.id.CountDownTextView);
		startStopButton = (Button) findViewById(R.id.CountDownButton);
		startStopButton.setOnClickListener(this);
		timerUpdateHandler = new Handler();
	}
	
	
	public void onClick(View v) {
		if(!timelapseRunning){
			startStopButton.setText("Stop");
			timelapseRunning = true;
			timerUpdateHandler.post(timerUpdateTask);
		} else {
			startStopButton.setText("Start");
			timelapseRunning = false;
			timerUpdateHandler.removeCallbacks(timerUpdateTask);
		}
	}
		private Runnable timerUpdateTask = new Runnable(){
			public void run(){
				if(currentTime <SECONDS_BETWEEN_PHOTOS){
					currentTime++;
				}
				else{
					camera.takePicture(null,null,null, MainActivity.this);
					currentTime = 0;
				}
				timerUpdateHandler.postDelayed(timerUpdateTask, 1000);
			countdownTextView.setText(""+currentTime);
			}
		};
	
	
	@	
	Override
	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		camera.startPreview();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		camera = Camera.open();
		try{
			camera.setPreviewDisplay(holder);
			Camera.Parameters parameters=camera.getParameters();
			
			if(this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE){
				
				parameters.set("orientation", "portrait");
				
				camera.setDisplayOrientation(90);
			}
			else{
				parameters.set("orientation", "landscape");
				camera.setDisplayOrientation(0);
			}
			
	
			camera.setParameters(parameters);
//			camera.setPreviewDisplay(holder);
		}
		catch(IOException exception){
			camera.release();
			Log.v("MainActivity", exception.getMessage());
		}
		camera.startPreview();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		camera.stopPreview();
		camera.release();
	}

	public void onPictureTaken(byte[] data, Camera camera){
		Uri imageFileUri = getContentResolver().insert(Media.EXTERNAL_CONTENT_URI, new ContentValues());
	try{
		OutputStream imageFileOS = getContentResolver().openOutputStream(imageFileUri);
		imageFileOS.write(data);
		imageFileOS.flush();
		imageFileOS.close();
	}catch (FileNotFoundException e){
		Toast FNF = Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT);
		FNF.show();
	}catch (IOException e){
		Toast IOE = Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT);
		IOE.show();
	}
	camera.startPreview();
	}
}
