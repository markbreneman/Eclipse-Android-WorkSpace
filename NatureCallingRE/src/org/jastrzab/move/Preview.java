/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jastrzab.move;

import android.app.Activity;
import android.os.Bundle;
import com.naturecalling.R;

/**
 *
 * @author Jastrzab
 */
public class Preview extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preview_layout);
	}

	@Override
	protected void onPause() {
		super.onPause();
		finish();
	}

}
