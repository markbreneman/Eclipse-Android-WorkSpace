/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 *
 * @author Jastrzab
 */
public class Haze extends AlertDialog.Builder implements DialogInterface.OnClickListener {

	public static final int DISABLED = -1;
	private HazeCallback mCallback = null;
	private int mId = 0;

	public Haze(Context context, int id, int title, int message, HazeCallback callback) {
		super(context);
		mId = id;
		mCallback = callback;
		setCancelable(false);
		if (title != DISABLED) {
			setTitle(title);
		}
		setMessage(message);
	}

	public Haze(Context context, int id, String title, String message, HazeCallback callback) {
		super(context);
		mId = id;
		mCallback = callback;
		setCancelable(false);
		if (title != null) {
			setTitle(title);
		}
		setMessage(message);
	}

	public void deploy(int positive, int negiative) {
		if (positive != DISABLED) {
			setPositiveButton(positive, this);
		}
		if (negiative != DISABLED) {
			setNegativeButton(negiative, this);
		}
		show();
	}

	public void deploy(String positive, String negiative) {
		if (positive != null) {
			setPositiveButton(positive, this);
		}
		if (negiative != null) {
			setNegativeButton(negiative, this);
		}
		show();
	}

	public void onClick(DialogInterface dialog, int which) {
		switch (which) {
			case DialogInterface.BUTTON_POSITIVE:
				mCallback.onPositive(mId);
			break;
			case DialogInterface.BUTTON_NEGATIVE:
				mCallback.onNegative(mId);
			break;
		}
	}

	public interface HazeCallback {

		void onPositive(int id);
		void onNegative(int id);

	}

}
