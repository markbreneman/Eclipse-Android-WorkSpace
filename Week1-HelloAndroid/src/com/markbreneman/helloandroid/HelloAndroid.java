package com.markbreneman.helloandroid;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class HelloAndroid extends Activity implements OnClickListener {

	TextView fartTextView;
	Button fartButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hello_android);
		
		fartTextView = (TextView) findViewById(R.id.fart_TextView);
		fartButton = (Button) findViewById(R.id.fart_Button);
		fartButton.setOnClickListener(this);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_hello_android, menu);
		return true;
	}

	@Override
	public void onClick(View clickedView) {
		fartTextView.setText("FAAAAAAAAAAARRRRRRTTTTS");
		
		
	}

}
