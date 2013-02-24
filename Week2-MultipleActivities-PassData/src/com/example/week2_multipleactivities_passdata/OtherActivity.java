package com.example.week2_multipleactivities_passdata;
//import com.example.week2_multipleactivities.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;


public class OtherActivity extends Activity {
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.other_activity);
		
		Intent i = this.getIntent();
		if(i.hasExtra(MainActivity.PASSING_DATA)){
			String s = i.getStringExtra(MainActivity.PASSING_DATA);
			Toast t= Toast.makeText(this, s, Toast.LENGTH_SHORT);
			t.show();		
		}
	}

}
