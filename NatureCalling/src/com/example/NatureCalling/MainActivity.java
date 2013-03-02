package com.example.NatureCalling;

//import android.R;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import com.jwetherell.motion_detection.MotionDetectionActivity.DetectionThread;
import com.jwetherell.motion_detection.data.GlobalData;
import com.jwetherell.motion_detection.data.Preferences;
import com.jwetherell.motion_detection.detection.AggregateLumaMotionDetection;
import com.jwetherell.motion_detection.detection.LumaMotionDetection;
import com.jwetherell.motion_detection.detection.RgbMotionDetection;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
	
	private static final String TAG = "MotionDetectionActivity";
	
	SurfaceView cameraView;
	SurfaceHolder surfaceHolder;
	Camera camera;
	boolean inPreview = false;
	
	Button startButton;
	TextView countdownTextView;
	
	Handler timerUpdateHandler;
	boolean timerRunning = false;
	int currentTime = 10;
	public static final int configuration= 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
	
	    cameraView = (SurfaceView) this.findViewById(R.id.CameraView);
		surfaceHolder = cameraView.getHolder();
		surfaceHolder.addCallback(this);
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		
		//OPEN UP PREFERENCE AND DETERMINE WHICH DETECTOR TYPE TO USE
//		if (Preferences.USE_RGB) 
//	            detector = new RgbMotionDetection();
//	        } else if (Preferences.USE_LUMA) {
//	            detector = new LumaMotionDetection();
//	        } else {
//	            // Using State based (aggregate map)
//	            detector = new AggregateLumaMotionDetection();
//	        }
		
		
		
		//Setup Countdown Text
		countdownTextView = (TextView) findViewById(R.id.CountDownTextView);
		//Setup StartButton
		startButton = (Button) findViewById(R.id.CountDownButton);
		startButton.setOnClickListener(this);
		//Setup Handler for Timer
		timerUpdateHandler = new Handler();
	}
	
	
	  @Override
	  //Declare function for what happens on Configuration changes ex. screen rotation.
	    public void onConfigurationChanged(Configuration newConfig) {
	        super.onConfigurationChanged(newConfig);
	    }
	 
	  @Override
	  //When user leaves this specific activity;
	    public void onPause() {
	        super.onPause();

	        camera.setPreviewCallback(null);
	        if (inPreview) camera.stopPreview();
	        inPreview = false;
	        camera.release();
	        camera = null;
	        
	      //Update the gallery - mb;
	        this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"+ Environment.getExternalStorageDirectory())));
	    }
	  
	  @Override
	    //After the app is launched and where it starts 
	    //when coming back from paused
	  public void onResume() {
	        super.onResume();
	        camera = Camera.open();
	    }
	
	  private PreviewCallback previewCallback = new PreviewCallback() {

	        @Override
	        public void onPreviewFrame(byte[] data, Camera cam) {
	            if (data == null) return;
	            Camera.Size size = cam.getParameters().getPreviewSize();
	            if (size == null) return;
	            
	            //This is a check to make sure that the phone is not in motion
	            //it prevents the motion detector from capturing data while moving.
	            if (!GlobalData.isPhoneInMotion()) {
	                DetectionThread thread = new DetectionThread(data, size.width, size.height);
	                thread.start();
	            }
	        }
	    };

	
	

	
	public void onClick(View v) {
		if(!timerRunning){
			timerRunning = true;
			timerUpdateHandler.post(timerUpdateTask);
		}
	}
	
	private Runnable timerUpdateTask = new Runnable(){
			public void run(){
				if(currentTime >1){
					currentTime--;
					timerUpdateHandler.postDelayed(timerUpdateTask, 1000);
					
//					Toast t = Toast.makeText(MainActivity.this, "Motion Detection will Start in "+Integer.toString(currentTime)+"secs", Toast.LENGTH_LONG);
//				    ToastExpander.showFor(t, currentTime*1000);
				}
				else{
					camera.takePicture(null,null, MainActivity.this);
					timerRunning = false;
					currentTime = 10;
				}
			countdownTextView.setText("Motion Detection will Start in"+currentTime);
			}
    };	
	
	@Override
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

	
	//SWITCHING TO SETTINGS
	public void settingsClicked(View v){
		Log.v("SettingsClicked","SettingsClicked");	
		Intent i = new Intent(this,ConfigurationActivity.class);
//		i.putExtra(MainActivity.PASSING_DA, "Here is the data I am passing");
		startActivityForResult(i, configuration);	
	}

	
	
}

