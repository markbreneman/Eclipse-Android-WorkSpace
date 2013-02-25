package com.example.promedia_cameramediastore;

import java.io.File;

import com.example.promedia_cameraintent.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.widget.ImageView;

public class MainActivity extends Activity {
	
	final static int CAMERA_RESULT=0;
	ImageView imv;
	String imageFilePath;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
				
		imageFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/testImages.jpg";
		Log.v("onCreate", imageFilePath);
		File imageFile = new File(imageFilePath);
		Uri imageFileUri = Uri.fromFile(imageFile);
		
		Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		i.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageFileUri); // This line seems to be causing errors outside of the BMPFactory
		
		startActivityForResult(i, CAMERA_RESULT);
		
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent intent){
		super.onActivityResult(requestCode, resultCode, intent);
		
		if(resultCode == RESULT_OK){
			//displays image without saving it to storage?
//			Bundle extras = intent.getExtras();
//			Bitmap bmp = (Bitmap) extras.get("data");	
			
			Log.v("resultcode", "resultOK");
		
			//Get a reference to the ImageView item in the layout
			ImageView imv = (ImageView) findViewById(R.id.ReturnedImageView); 
			
			//Get Display Information
			Display currentDisplay = getWindowManager().getDefaultDisplay();
			int dw = currentDisplay.getWidth();
			int dh = currentDisplay.getHeight();
			
			//Load Dimension of Image only *not the whole image
			BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();//this creates the BitmapFactory.options	
			bmpFactoryOptions.inJustDecodeBounds=true; // This looks at just the bounds
			//create a bitmap using just the bounds
			Bitmap bmp = BitmapFactory.decodeFile(imageFilePath, bmpFactoryOptions);
			//Calculate the height & width of the image relative to the screen as a ratio
			int heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight/(float)dh);
			int widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth/(float)dw);
			
			Log.v("Height Ratio", ""+heightRatio);
			Log.v("Width Ratio", ""+widthRatio);
			
			//If Both ratios are greater than 1, one of the sides  the image is greater than the screen
			if (heightRatio>1 && widthRatio>1){
				if (heightRatio>widthRatio){
					//Height is greater scale according to height ratio
					bmpFactoryOptions.inSampleSize = heightRatio;	
				}
				//Width is greater scale according to width ratio
				else{bmpFactoryOptions.inSampleSize = widthRatio;}
			}
			
			//Fully look at the image
			bmpFactoryOptions.inJustDecodeBounds = false;
			bmp = BitmapFactory.decodeFile(imageFilePath, bmpFactoryOptions);
			
			//Display the Image
			imv.setImageBitmap(bmp);
		}
		else{
			Log.v("resultcode", "resultNOTOK");
		}
	}
	

}
