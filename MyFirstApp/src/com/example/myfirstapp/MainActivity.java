package com.example.myfirstapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;


public class MainActivity extends Activity {

	public final static String EXTRA_MESSAGE ="com.example.myfirstapp.MESSAGE"; // This defines the key for intent's extra as a public constant
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	/* Called when the user clicks the Send the Message buttons*/
	public void sendMessage(View view){
		//This is where something is done to respond to the button
		Intent intent = new Intent(this, DisplayMessageActivity.class);//Create a new intent
		EditText editText = (EditText) findViewById(R.id.edit_message);//find the edit message
		String message = editText.getText().toString();//Get the edit message as a string
		intent.putExtra(EXTRA_MESSAGE, message);//store the content  as keyvalue pairs called extras.The putExtra() method takes the key name in the first parameter and the value in the second parameter.
		startActivity(intent);
	}
	
	
}
