package com.example.promedia_cameramediastore;

import java.io.FileNotFoundException;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//

public class MainActivity extends Activity {
	
	final static int CAMERA_RESULT=0;
	Uri imageFileUri;
	
	// UI Elements
	ImageView returnedImageView;
	Button takePictureButton;
	Button saveDataButton;
	TextView titleTextView;
	TextView descriptionTextView;
	EditText titleEditText;
	EditText descriptionEditText;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		returnedImageView = (ImageView) findViewById(R.id.ReturnedImageView);
		takePictureButton = (Button) findViewById(R.id.TakePictureButton);
		saveDataButton = (Button) findViewById(R.id.SaveDataButton);
		titleTextView = (TextView) findViewById(R.id.TitleTextView);
		descriptionTextView = (TextView) findViewById(R.id.DescriptionTextView);
		titleEditText = (EditText) findViewById(R.id.TitleEditText);
		descriptionEditText = (EditText) findViewById(R.id.DescriptionEditText);
		
		// Set all except takePictureButton to not be visible initially
	    // View.GONE is invisible and doesn't take up space in the layout
		returnedImageView.setVisibility(View.GONE);
		saveDataButton.setVisibility(View.GONE);
		titleTextView.setVisibility(View.GONE);
		descriptionTextView.setVisibility(View.GONE);
		titleEditText.setVisibility(View.GONE);
		descriptionEditText.setVisibility(View.GONE);
		
		takePictureButton.setOnClickListener(new OnClickListener(){
			public void onClick(View V){
				//Add a new record without the bitmap
				//returns the URI of the new record
				imageFileUri = getContentResolver().insert(Media.EXTERNAL_CONTENT_URI, new ContentValues());
				Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				i.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageFileUri); // This line seems to be causing errors outside of the BMPFactory
				startActivityForResult(i, CAMERA_RESULT);
			}
		});
		
		saveDataButton.setOnClickListener(new OnClickListener(){
			public void onClick(View V){
				//Update Media-store
				ContentValues contentValues = new ContentValues(3);
				contentValues.put(Media.DISPLAY_NAME, titleEditText.getText().toString());
				contentValues.put(Media.DESCRIPTION, titleEditText.getText().toString());
				getContentResolver().update(imageFileUri, contentValues, null,null);
				//Alert User
				Log.v("ImagePropertiesUpdate","UPDATED");
				Toast t =Toast.makeText(MainActivity.this, "Image Properties Updated", Toast.LENGTH_SHORT);
				t.show();
				
				//Hide UI element and reveal Picture
				takePictureButton.setVisibility(View.VISIBLE);
				returnedImageView.setVisibility(View.GONE);
				saveDataButton.setVisibility(View.GONE);
				titleTextView.setVisibility(View.GONE);
				descriptionTextView.setVisibility(View.GONE);
				titleEditText.setVisibility(View.GONE);
				descriptionEditText.setVisibility(View.GONE);
			}});
			}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent intent){
		super.onActivityResult(requestCode, resultCode, intent);
		
		if(resultCode == RESULT_OK){
			
			//Camera is back
			Log.v("resultcode", "resultOK");
			
			//Turn on UI Buttons
			returnedImageView.setVisibility(View.VISIBLE);
			saveDataButton.setVisibility(View.VISIBLE);
			titleTextView.setVisibility(View.VISIBLE);
			descriptionTextView.setVisibility(View.VISIBLE);
			titleEditText.setVisibility(View.VISIBLE);
			descriptionEditText.setVisibility(View.VISIBLE);
			//Turn off Take Picture
			takePictureButton.setVisibility(View.GONE);
			
			//Scale the Image
			int dw = 200;
			int dh = 200;
			
			try{
			
			
			//Load Dimension of Image only *not the whole image
			BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();//this creates the BitmapFactory.options	
			bmpFactoryOptions.inJustDecodeBounds=true; // This looks at just the bounds
			//create a bitmap using just the bounds
			Bitmap bmp = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageFileUri),null, bmpFactoryOptions);
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
			bmp = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageFileUri),null, bmpFactoryOptions);
			
			//Display the Image
			returnedImageView.setImageBitmap(bmp);
		}
			
		catch(FileNotFoundException e){
			Log.v("ERROR", e.toString());
		}
	}
  }
}
