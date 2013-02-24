package com.example.promedia_cameraintent;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

public class MainActivity extends Activity {
	
	final static int CAMERA_RESULT=0;
	ImageView imv;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		String imageFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/testImages.jpg";
		Log.v("onCreate", imageFilePath);
		File imageFile = new File(imageFilePath);
		Uri imageFileUri = Uri.fromFile(imageFile);
		
		Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		i.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageFileUri);
		startActivityForResult(i, CAMERA_RESULT);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent intent){
		super.onActivityResult(requestCode, resultCode, intent);
		
		if(resultCode == RESULT_OK){
			Bundle extras = intent.getExtras();
			Bitmap bmp = (Bitmap) extras.get("data");
			Log.v("resultcode", "resultok");	
			imv = (ImageView) findViewById(R.id.ReturnedImageView);
			imv.setImageBitmap(bmp);
		}
		else{
			Log.v("resultcode", "resultNOTOK");
		}
	}
	

}
