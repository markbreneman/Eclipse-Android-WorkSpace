package com.example.week2_SaveState;


import com.example.savestate_week2.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class MainActivity extends Activity implements OnClickListener{

	EditText myEditText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		myEditText = (EditText) this.findViewById(R.id.editText1);
		if (savedInstanceState != null && savedInstanceState.containsKey("editText1")){
			myEditText.setText(savedInstanceState.getString("editText1"));
			Log.v("MainActivity","on create, populating editText1 with:" + savedInstanceState.getString("editText1"));
		}
				
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState){
	outState.putString("editText1", myEditText.getText().toString());
	Log.v("MainActivity","editText1: " + myEditText.getText().toString());
	}

	@Override
	public void onClick(View clickedView) {
		
	}

}
