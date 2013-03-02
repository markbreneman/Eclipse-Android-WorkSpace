/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.preview.camera;

import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.AsyncTask;
import android.util.Log;
import android.view.SurfaceHolder;
import java.util.Arrays;
import org.me.constant.Constant;

/**
 *
 * @author Jastrzab
 */
public final class CameraWindow extends AsyncTask<Object, Boolean, Object> implements Constant, Camera.PreviewCallback {

	private final ConcurentLock mFlowControl = new ConcurentLock();
	private final Context mContext;
	private SurfaceTexture mSurface;
	private SurfaceHolder mHolder;
	private CameraCallBack mCallBack = null;
	private Camera mCamera;
	private Parameters mParamter;
	private PreviewFormat mSize;
	private MotionDetection mDetection;
	private FrameBuffer mBuffer = null;
	private int[] mFrame = null;
	private static int mPercent = 0;
	private static int mEvents = 0;
	private int mRepeat = 0;
	private int mWait = 0;
	private int mStart = 1000;
	private int mFrames = 5;
	private long mTime = System.currentTimeMillis();

	public CameraWindow(Context context, CameraCallBack callBack) {
		mContext = context;
		mCallBack = callBack;
		if (CameraImplements.isSurfaceTexture()) {
			mSurface = CameraImplements.getSurfaceTexture();
		} else {
			mHolder = CameraImplements.getSurfaceHolder(context);
		}
		mDetection = new MotionDetection();
	}

	public void baginProcesing(int camera) {
		try {
			mPercent = 100;
			mEvents = 0;
			mFlowControl.setWorking(true);
			mFlowControl.close();
			execute(camera);
		} catch (Exception e) {
			Log.d(LOG, "Camera Alredy Running");
		}
	}
	
	public void setThreshold(int threshold) {
		mDetection.setThreshold(threshold);
	}

	public void setFrames(int frames) {
		mFrames = Math.max(frames, 1);
	}

	public void setStart(int start) {
		mStart = start * ONE_SECOND;
	}

	public void endProcesing() {
		mPercent = isCamera() ? 100 : 0;
		mFlowControl.setWorking(false);
		mFlowControl.open();
	}

	public Context getContext() {
		return mContext;
	}

	public Camera getCamera() {
		return mCamera;
	}

	public boolean isCamera() {
		return mCamera != null;
	}

	public static int getPercent() {
		return mPercent;
	}

	public static int getEvents() {
		return mEvents;
	}

	public static void clearEvents() {
		mEvents = 0;
	}

	@Override
	protected Object doInBackground(Object... object) {
		mCallBack.onPrepare();
		openCamera((Integer) object[0]);
		publishProgress(isCamera());
		if (mCamera == null) {
			Log.d(LOG, "No Active Camera Exit");
			mPercent = 0;
			mEvents = 0;
			return null;
		}
		RemeinWake.getLock(mContext);
		try {
			mParamter = mCamera.getParameters();
			mSize = PreviewFormat.getPreviewSize(mParamter);
			Log.d(LOG, "Preview Size: " + mSize.toString());
			mBuffer = CameraImplements.getBuffer(mParamter, mSize);
			mFrame = new int[mSize.width * mSize.height];
			mParamter.setPreviewSize(mSize.width, mSize.height);
			mParamter.setPreviewFormat(ImageFormat.NV21);
			mCamera.setParameters(mParamter);
			if (CameraImplements.isSurfaceTexture()) {
				mCamera.setPreviewTexture(mSurface);
			} else {
				mCamera.setPreviewDisplay(mHolder);
			}
			mCamera.setPreviewCallbackWithBuffer(CameraWindow.this);
			mCamera.startPreview();
			if (mStart > 0) {
				Log.d(LOG, "Wait For Start Procesing");
				mPercent = 100;
				mFlowControl.block(mStart);
			}
			mCamera.addCallbackBuffer(mBuffer.getFrame());
			Log.d(LOG, "Open Procesing Cycle");
			while (mFlowControl.isWorking()) {
				mFlowControl.block();
				if (mFlowControl.isWorking()) {
					mFlowControl.close();
					if (mRepeat <= 0 && mDetection.detect(mBuffer.getFrame(), mFrame, mSize.width, mSize.height)) {
						Arrays.fill(mFrame, 0);
						mEvents++;
						mRepeat = mFrames;
					}
					mPercent = mDetection.getPercent();
					if (mRepeat > 0) {
						mPercent = 100;
						mTime = System.currentTimeMillis();
						mWait = mCallBack.onDetect(mTime, mBuffer, CameraWindow.this, mRepeat == mFrames, mRepeat == 1, mDetection, (mFrames - mRepeat) + 1, mFrames);
						if (mWait > 0) {
							mFlowControl.block(mWait);
						}
						mRepeat--;
					}
					mCamera.addCallbackBuffer(mBuffer.getFrame());
				}
			}
			Log.d(LOG, "Close Procesing Cycle");
			mCamera.stopPreview();
		} catch (Exception e) {
			Log.d(LOG, "Exception In Camera Thread " + e.getLocalizedMessage());
		} finally {
			closeCamera();
			RemeinWake.leaveLock();
		}
		return null;
	}

	@Override
	protected void onProgressUpdate(Boolean... sukces) {
		mCallBack.onBegin(sukces[0]);
	}

	@Override
	protected void onPostExecute(Object result) {
		if (isCamera()) {
			Log.d(LOG, "Working Camera Found Closing");
			closeCamera();
		}
		if (RemeinWake.isLock()) {
			RemeinWake.leaveLock();
		}
		mCallBack.onFinish();
		mPercent = 0;
	}

	public void onPreviewFrame(byte[] arg0, Camera arg1) {
		mFlowControl.open();
	}

	private void openCamera(int camera) {
		Log.d(LOG, "Camera Created");
		try {
			if (CameraImplements.isMultiCamera()) {
				mCamera = Camera.open(CameraImplements.validCamera(camera));
			} else {
				mCamera = Camera.open();
				if (mCamera == null) {
					mCamera = Camera.open(0);
				}
			}
		} catch (Exception e) {
			mCamera = null;
			Log.d(LOG, "Can Not Connect To Camera " + e.getLocalizedMessage());
		}
	}

	private void closeCamera() {
		Log.d(LOG, "Camera Destroy");
		try {
			mCamera.release();
			mCamera = null;
		} catch (Exception e) {
			Log.d(LOG, "Can Not Close Camera " + e.getLocalizedMessage());
		}
	}

	public interface CameraCallBack {

		void onPrepare();
		void onBegin(boolean sukces);
		int onDetect(long time, FrameBuffer frame, CameraWindow camera, boolean first, boolean last, MotionDetection detector, int frameCount, int frameTotal);
		void onFinish();

	}

}
