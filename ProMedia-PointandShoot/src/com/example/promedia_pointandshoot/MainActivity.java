package com.example.promedia_pointandshoot;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import android.app.Activity;
import android.content.ContentValues;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener, SurfaceHolder.Callback, Camera.PictureCallback {
	
	SurfaceHolder surfaceHolder;
	SurfaceView cameraView;
	Camera camera;
	//For constraining preview window
//	public static final int LARGEST_WIDTH = 200;
//	public static final int LARGEST_HEIGHT = 200;
	

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		
		setContentView(R.layout.activity_main);
		
		cameraView = (SurfaceView) this.findViewById(R.id.CameraView);
		
		surfaceHolder = cameraView.getHolder();
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		surfaceHolder.addCallback(this);
		
		cameraView.setFocusable(true);
		cameraView.setFocusableInTouchMode(true);
		cameraView.setClickable(true);
		cameraView.setOnClickListener(this);
	
	}
	
	public void onClick(View v){
		camera.takePicture(null, null, null, this);
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
			
			//LISTING APPLICABLE CAMERA SIZES
//			int bestHeight = 0;
//			int bestWidth = 0;
//			
//			List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();
//			if (previewSizes.size()>1){
//				Iterator<Camera.Size> cei = previewSizes.iterator();
//				while(cei.hasNext())
//				{
//					Camera.Size aSize = cei.next();
//					Log.v("Camera", "checking"+ aSize.width + "x " + aSize.height);
//					if (aSize.width > bestWidth && aSize.width <= LARGEST_WIDTH && aSize.height > bestHeight && aSize.height <= LARGEST_HEIGHT){
//						bestWidth = aSize.width;
//						bestHeight = aSize.height;
//					}
//				}
//			}
//			if(bestHeight != 0 && bestWidth !=0 ){
//				Log.v("camera", "Width" + bestWidth + "x" + bestHeight);
//				parameters.setPreviewSize(bestWidth, bestHeight);
//				cameraView.setLayoutParams(new LinearLayout.LayoutParams(bestWidth, bestHeight));
//			}
//			
				
			
			//DEMONSTRATING COLOR EFFECTS
//			List<String> colorEffects = parameters.getSupportedColorEffects();
//			Iterator<String> cei = colorEffects.iterator();
//			while(cei.hasNext())
//			{String currentEffect = cei.next();
//			Log.v("camera", "Checking" + currentEffect);
//			if(currentEffect.equals(Camera.Parameters.EFFECT_SEPIA)){
//				Log.v("camera", "usingSolarize");
//				parameters.setColorEffect(Camera.Parameters.EFFECT_SEPIA);
//				break;
//				}	
//			}
			
			//Demonstrating AutoFlash Setting
//			parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);

	
			
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
	
	
}
