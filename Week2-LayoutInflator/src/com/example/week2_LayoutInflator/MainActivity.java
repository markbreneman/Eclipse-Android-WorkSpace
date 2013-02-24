package com.example.week2_LayoutInflator;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {

     Button changeButton;
     TextView textView;
     TextView otherTextView;

     LayoutInflater inflater;

     View mainView;
     View otherView;

     @Override
     public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        inflater = LayoutInflater.from(this);
        mainView = inflater.inflate(R.layout.activity_main, null);
        otherView = inflater.inflate(R.layout.activity_main_2, null);
        
        changeButton = (Button) mainView.findViewById(R.id.MainButton);
        changeButton.setOnClickListener(this);
        
        textView = (TextView) mainView.findViewById(R.id.MainTextView);
        otherTextView = (TextView) otherView.findViewById(R.id.OtherTextView);

        otherTextView.setText("Some Dynamic Text");
        setContentView(mainView);        
     }

     public void onClick(View v) {
          setContentView(otherView);
     }
}