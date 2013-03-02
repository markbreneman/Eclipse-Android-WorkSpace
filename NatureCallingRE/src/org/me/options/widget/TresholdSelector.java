/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.options.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import com.naturecalling.R;
import org.me.constant.Constant;

public class TresholdSelector extends Dialog implements Constant, View.OnClickListener, SeekBar.OnSeekBarChangeListener {

	private static final int MAX = 100;
	private static final int MAX_SLIDE = 49;
	private final View mView;
	private final TresholdSelectorCallback mCallback;
	private final SeekBar mSeek;
	private final TextView mValue;
	private final Button mOk;
	private final Button mCancel;

	public TresholdSelector(Context context) {
		this(context, null);
	}

	public TresholdSelector(Context context, TresholdSelectorCallback callback) {
		super(context, R.style.Dialog);
		mCallback = callback;
		setTitle(R.string.threshold_title);
		mView = LayoutInflater.from(context).inflate(R.layout.threshold_input_layout, null);
		mValue = (TextView) mView.findViewById(R.id.threshold_value);
		mSeek = (SeekBar) mView.findViewById(R.id.threshold);
		mSeek.setMax(MAX_SLIDE);
		mSeek.setOnSeekBarChangeListener(TresholdSelector.this);
		mOk = (Button) mView.findViewById(R.id.threshold_input_button_ok);
		mOk.setOnClickListener(TresholdSelector.this);
		mCancel = (Button) mView.findViewById(R.id.threshold_input_button_cancel);
		mCancel.setOnClickListener(TresholdSelector.this);
		setContentView(mView);
	}

	public void onClick(View view) {
		if (view.getId() == R.id.threshold_input_button_ok) {
			if (mCallback != null) {
				mCallback.onOk(TresholdSelector.this, MAX - getProgress());
			}
		} else if (view.getId() == R.id.threshold_input_button_cancel) {
			if (mCallback != null) {
				mCallback.onCancel(TresholdSelector.this);
			}
		}
		dismiss();
	}

	public void setTreshold(int treshold) {
		setProgress(MAX - treshold);
	}

	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		setProgressText(progress);
	}

	private void setProgressText(int progress) {
		mValue.setText(translateProgress(getContext(), progress));
	}

	public static String translateProgress(Context context, int progress) {
		if (progress <= 10) {
			return context.getText(R.string.detector_sensitivity_low).toString();
		} else if (progress > 10 && progress < 30) {
			return context.getText(R.string.detector_sensitivity_medium).toString();
		} else if (progress >= 30 && progress < 45) {
			return context.getText(R.string.detector_sensitivity_high).toString();
		} else {
			return context.getText(R.string.detector_sensitivity_very_high).toString();
		}
	}

	private void setProgress(int progress) {
		mSeek.setProgress(progress - 50);
	}

	private int getProgress() {
		return mSeek.getProgress() + 50;
	}

	public void onStartTrackingTouch(SeekBar arg0) {

	}

	public void onStopTrackingTouch(SeekBar arg0) {

	}

	public interface TresholdSelectorCallback {

		void onCancel(TresholdSelector dialog);
		void onOk(TresholdSelector dialog, int threshold);
		
	}

}