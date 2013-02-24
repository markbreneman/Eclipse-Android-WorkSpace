package com.example.week2_switchingviewsandShowHide;


import com.example.week2_switchingviews.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener{
	Button myButton;
	TextView myTextView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		myTextView = (TextView) this.findViewById(R.id.myTextView);
				
		myButton= (Button) this.findViewById(R.id.myButton);
		myButton.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View clickView){
//		setContentView(R.layout.activity_main_2);
		if(myTextView.getVisibility()==View.VISIBLE){
		myTextView.setVisibility(View.INVISIBLE);
		} else{
			myTextView.setVisibility(View.VISIBLE);
		}
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
