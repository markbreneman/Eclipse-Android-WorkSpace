package com.example.week2_cameraintentexercise;

//import com.example.week2_cameraintentexercise.R;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	
	Button myButton;
	
	public static final String PASSING_DATA = "PASSING";
	public static final String PASSING_BACK= "PASSING_BACK";
	
	public static final int OTHER_ACTIVITY= 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		myButton = (Button) findViewById(R.id.button1);
		myButton.setOnClickListener(this);
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}
	
	@Override
	public void onClick(View v) {
		Log.v("MainActivity","Button was clicked");	
		Intent i = new Intent(this,OtherActivity.class);
		i.putExtra(MainActivity.PASSING_DATA, "Here is the data I am passing");
		startActivityForResult(i, OTHER_ACTIVITY);	
		
		//This Launches a web Browser.
//		Intent intent = new Intent(Intent.ACTION_VIEW); 
//		Uri uri = Uri.parse("http://thesis.itp.io"); 
//		intent.setData(uri); 
//		startActivity(intent);
		
		//This Launches a Text Message with the prepopulated variables
//		Intent smsIntent = new Intent(Intent.ACTION_VIEW);
//		smsIntent.setType("vnd.android-dir/mms-sms");
//		smsIntent.putExtra("address", "17174756812");
//		smsIntent.putExtra("sms_body","FAAAARTTS");
//		startActivity(smsIntent);

//		Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//		startActivityForResult(i, CAMERA_RESULT);
		
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent dataIntent) {
		super.onActivityResult(requestCode, resultCode, dataIntent);
		if (requestCode == OTHER_ACTIVITY){
			if(resultCode == RESULT_OK){
				if(dataIntent.hasExtra(PASSING_BACK)){
					String message = dataIntent.getStringExtra(PASSING_BACK);
				Toast t = Toast.makeText(this, message, Toast.LENGTH_LONG);
				t.show();
			}
			}
	}
	}
	
}
