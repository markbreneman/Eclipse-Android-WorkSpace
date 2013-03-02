/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.me.preview.camera.window;

import android.content.Context;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import org.me.constant.Constant;
import org.me.preview.camera.CameraImplements;
import org.me.preview.camera.PreviewFormat;

/**
 *
 * @author Jastrząb
 */

public class Window extends SurfaceView implements Constant, SurfaceHolder.Callback {

	private final SurfaceHolder mSurface;
	private Camera mCamera = null;
	private Parameters mParamter;
	private boolean mChanged = false;
	private PreviewFormat mSize;
	private int mNumber = 0;

	public Window(Context context, AttributeSet attrs) {
		super(context, attrs);
		mSurface = getHolder();
		mSurface.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		mSurface.addCallback(Window.this);
		mNumber = Integer.valueOf(PreferenceManager.getDefaultSharedPreferences(context).getString(CONFIG_NAME_CAMERA_NUMBER, "0"));
	}

	public void surfaceCreated(SurfaceHolder sutface) {
		Log.d(LOG, "Preview Surface Created");
		mChanged = false;
		try {
			if (CameraImplements.isMultiCamera()) {
				mCamera = Camera.open(CameraImplements.validCamera(mNumber));
			} else {
				mCamera = Camera.open();
				if (mCamera == null) {
					mCamera = Camera.open(0);
				}
			}
			mCamera.setPreviewDisplay(sutface);
			mParamter = mCamera.getParameters();
			mSize = PreviewFormat.getPreviewSize(mParamter);
			mParamter.setPreviewSize(mSize.width, mSize.height);
			mParamter.setPreviewFormat(ImageFormat.NV21);
		} catch (Exception e) {
			Log.d(LOG, "Preview Exception", e);
		}
	}

	public void surfaceChanged(SurfaceHolder sutface, int format, int width, int height) {
		Log.d(LOG, "Preview Surface Changed");
		try {
			if (mChanged) {
				mCamera.stopPreview();
			} else {
				mChanged = true;
			}
			mCamera.setParameters(mParamter);
			mCamera.startPreview();
		} catch (Exception e) {
			Log.d(LOG, "Preview Exception", e);
		}
	}

	public void surfaceDestroyed(SurfaceHolder sutface) {
		Log.d(LOG, "Preview Surface Destroyed");
		mChanged = true;
		try {
			mCamera.stopPreview();
			mCamera.release();
			mCamera = null;
		} catch (Exception e) {
			Log.d(LOG, "Preview Exception", e);
		}
	}

}