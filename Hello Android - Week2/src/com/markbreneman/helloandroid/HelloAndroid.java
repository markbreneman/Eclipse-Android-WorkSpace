package com.markbreneman.helloandroid;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class HelloAndroid extends Activity implements OnClickListener{
	TextView fartTextView;
	Button fartButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hello_android);
		
		fartTextView = (TextView) findViewById(R.id.fart_TextView);
		fartButton = (Button) findViewById(R.id.fart_Button);
		
		fartButton.setOnClickListener(this);
		Log.v("onCreate", "Created!");
		
	}


	@Override
	public void onClick(View clickedView) {
		fartTextView.setText("You Farted!");
		Toast toast=Toast.makeText(this, "FFFFFFAAAARRRTTTTTS", Toast.LENGTH_LONG);
		toast.show();
		Log.v("FartButtonPressed", "farted!");
		
		
	}
	

	
}
