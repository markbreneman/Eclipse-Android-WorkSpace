package com.example.NatureCalling;

import android.util.Log;
import android.widget.Toast;

public class ToastExpander {
	
	public static final String TAG = "ToastExpander";

	public static void showFor(final Toast aToast, final long durationInMilliseconds) {

		aToast.setDuration(Toast.LENGTH_SHORT);
		
		Thread t = new Thread() {
			long timeElapsed = 0l;
			
			public void run() {
				try {
					while (timeElapsed <= durationInMilliseconds) {
						long start = System.currentTimeMillis();
						aToast.show();
						sleep(1750);
						timeElapsed += System.currentTimeMillis() - start;
					}
				} catch (InterruptedException e) {
					Log.e(TAG, e.toString());
				}
			}
		};
		t.start();
	}
}