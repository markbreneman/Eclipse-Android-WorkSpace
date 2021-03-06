/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jastrzab.move;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import com.naturecalling.R;
import org.me.constant.Constant;
import org.me.options.widget.TresholdSelector;
import org.me.preview.camera.CameraImplements;

/**
 *
 * @author Jastrzab
 */
public class Detector extends PreferenceActivity implements Constant, Preference.OnPreferenceClickListener, TresholdSelector.TresholdSelectorCallback {

	private Preference mThreshold;
	private ListPreference mCamera;
	private ListPreference mStart;
	private Preference mPreview;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.options_detector);
		mThreshold = findPreference("selectThreshold");
		mThreshold.setOnPreferenceClickListener(Detector.this);
		mCamera = (ListPreference) findPreference("camera_number");
		mStart = (ListPreference) findPreference("start_delay");
		mPreview = findPreference("showPreview");
		mPreview.setOnPreferenceClickListener(Detector.this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		switch (Engine.getState()) {
			case Active.SERVICE_CHANGING:
			case Active.SERVICE_ENABLED:
				mCamera.setEnabled(false);
				mStart.setEnabled(false);
				mPreview.setEnabled(false);
			break;
			case Active.SERVICE_DISABLED:
				mCamera.setEnabled(CameraImplements.isMultiCamera());
				mStart.setEnabled(true);
				mPreview.setEnabled(true);
			break;
		}
	}

	public boolean onPreferenceClick(Preference preference) {
		if (preference.getKey().equals("selectThreshold")) {
			TresholdSelector treshold = new TresholdSelector(Detector.this, Detector.this);
			treshold.setTreshold(preference.getSharedPreferences().getInt(CONFIG_NAME_THRESHOLD, THRESHOLD_DEFAULT));
			treshold.show();
		} else if (preference.getKey().equals("showPreview")) {
			startActivity(new Intent(Detector.this, Preview.class));
		}
		return true;
	}

	public void onCancel(TresholdSelector dialog) {
		
	}

	public void onOk(TresholdSelector dialog, int threshold) {
		Editor edit = PreferenceManager.getDefaultSharedPreferences(Detector.this).edit();
		edit.putInt(CONFIG_NAME_THRESHOLD, threshold);
		edit.commit();
		if (Engine.isWorking()) {
			startService(Active.getTresholdIntent(Detector.this, threshold));
		}
	}

}
