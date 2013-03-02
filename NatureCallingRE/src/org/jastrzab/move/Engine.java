/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jastrzab.move;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;
import com.naturecalling.R;
import org.me.constant.Constant;
import org.me.database.Database;
import org.me.media.FrameSaver;
import org.me.media.SignalPlayer;
import org.me.preview.camera.CameraWindow;
import org.me.preview.camera.FrameBuffer;
import org.me.preview.camera.MotionDetection;
import org.me.telefony.CallState;

/**
 *
 * @author Jastrząb
 */
public class Engine extends Service implements Constant, CameraWindow.CameraCallBack {

	private CameraWindow mCamera;
	private final Switcher mSwitcher = new Switcher();
	private volatile static boolean mWorking = false;
	private volatile static int mState = Active.SERVICE_DISABLED;
	private SharedPreferences mPreferences;
	private SignalPlayer mPlayer;
	private FrameSaver mSaver;
	private boolean mFrame = false;
	private boolean mSignal = true;
	private boolean mLogs = false;
	private boolean mDelete = true;
	private int mWatiFrame = 0;
	private int mWatiNext = 0;
	private boolean mIndex = false;
	private Database mDatabase;
	private CallState mCallState;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		setWorking(true);
		setState(Active.SERVICE_CHANGING);
		mCallState = new CallState(Engine.this);
		mCallState.addListner();
		mPreferences = PreferenceManager.getDefaultSharedPreferences(Engine.this);
		mFrame = mPreferences.getBoolean(CONFIG_NAME_SAVE_FRAME, false);
		mSignal = mPreferences.getBoolean(CONFIG_NAME_PLAY_SIGNAL, true);
		mLogs = mPreferences.getBoolean(CONFIG_NAME_SAVE_LOGS, false);
		mWatiFrame = Integer.valueOf(mPreferences.getString(CONFIG_NAME_WAIT_FRAME, "0"));
		mWatiNext = Integer.valueOf(mPreferences.getString(CONFIG_NAME_WAIT_NEXT, "0")) * ONE_SECOND;
		mIndex = mPreferences.getBoolean(CONFIG_NAME_INDEX_GALERY, false);
		mDelete = mPreferences.getBoolean(CONFIG_NAME_DELETE_ON_START, true);
		if (mLogs) {
			mDatabase = new Database(Engine.this);
			mDatabase.onCreate();
		}
		if (mSignal) {
			mPlayer = new SignalPlayer(Engine.this);
			mPlayer.create(mPreferences.getString(CONFIG_NAME_SIGNAL_SOUND, null));
		}
		if (mFrame) {
			mSaver = new FrameSaver();
		}
		mCamera = new CameraWindow(Engine.this, Engine.this);
		Log.d(LOG, "Engine Service Start");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mCallState.remListner();
		mCamera.endProcesing();
		mSwitcher.setSwitchOff();
		if (mSignal) {
			mPlayer.destroy();
		}
		if (mLogs) {
			mDatabase.onDestroy();
		}
		setWorking(false);
		Log.d(LOG, "Engine Service Stop");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (intent != null && intent.getAction() != null) {
			if (intent.getAction().equals(Active.ACTION_ACTIVITY_CHANGE)) {
				if (mSwitcher.isSwitchOn()) {
					setState(Active.SERVICE_CHANGING);
					stopSelf();
				} else {
					mSwitcher.setSwitchOn();
					mCamera.setThreshold(mPreferences.getInt(CONFIG_NAME_THRESHOLD, THRESHOLD_DEFAULT));
					mCamera.setStart(Integer.valueOf(mPreferences.getString(CONFIG_NAME_START_DELAY, "5")));
					mCamera.setFrames(Integer.valueOf(mPreferences.getString(CONFIG_NAME_FRAME_COUNT, "1")));
					mCamera.baginProcesing(Integer.valueOf(mPreferences.getString(CONFIG_NAME_CAMERA_NUMBER, "0")));
				}
			} else if (intent.getAction().equals(Active.ACTION_TRESHOLD_CHANGE)) {
				mCamera.setThreshold(intent.getIntExtra(Active.ACTION_TRESHOLD_VALUE, THRESHOLD_DEFAULT));
			} else if (intent.getAction().equals(Active.ACTION_WAIT_CHANGE)) {
				mWatiNext = intent.getIntExtra(Active.ACTION_WAIT_VALUE, 0) * ONE_SECOND;
			} else {
				setState(Active.SERVICE_DISABLED);
				stopSelf();
			}
		} else {
			setState(Active.SERVICE_DISABLED);
			stopSelf();
		}
		return super.onStartCommand(intent, flags, startId);
	}

	private void setWorking(boolean working) {
		mWorking = working;
	}

	public static boolean isWorking() {
		return mWorking;
	}

	private void setState(int state) {
		mState = state;
		sendBroadcast(Active.getActicityIntent(Engine.this));
	}

	public static int getState() {
		return mState;
	}

	public void onBegin(boolean sukces) {
		Toast.makeText(getApplicationContext(), sukces ? R.string.detector_on : R.string.detector_not_on, Toast.LENGTH_SHORT).show();
		if (sukces) {
			setState(Active.SERVICE_ENABLED);
		} else {
			setState(Active.SERVICE_DISABLED);
			stopSelf();
		}
	}

	public int onDetect(long time, FrameBuffer frame, CameraWindow camera, boolean first, boolean last, MotionDetection detector, int frameCount, int frameTotal) {
		if (mFrame) {
			mSaver.saveFrame(Engine.this, time, frame, first, last, mIndex, frameCount, frameTotal);
		}
		if (mSignal && first) {
			if (!mCallState.isCallState() && mPlayer != null) {
				mPlayer.play();
			}
		}
		if (mLogs && first) {
			mDatabase.insertDatabase(detector.getPercent(), detector.getThreshold());
		}
		return last ? mWatiNext : mWatiFrame;
	}

	public void onFinish() {
		setState(Active.SERVICE_DISABLED);
		Toast.makeText(getApplicationContext(), R.string.detector_off, Toast.LENGTH_SHORT).show();
	}

	public void onPrepare() {
		if (mLogs && mDelete) {
			mDatabase.deleteAll();
		}
	}

}
