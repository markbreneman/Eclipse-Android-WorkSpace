/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.media;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;
import org.me.constant.Constant;

/**
 *
 * @author Jastrzab
 */
public class SignalPlayer implements Constant {

	private MediaPlayer mPlayer;
	private AudioManager mManager;
	private final Context mContext;
	private int mVolume = 0;
	private String mSound = null;

	public SignalPlayer(Context context) {
		mContext = context;
	}

	public synchronized void create(String sound) {
		try {
			mSound = sound;
			mManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
			if (mManager != null) {
				mVolume = mManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION);
				mManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, mManager.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION), 0);
			}
			mPlayer = new MediaPlayer();
			if (mPlayer != null) {
				if (mSound != null) {
					mPlayer.setDataSource(mContext, Uri.parse(mSound));
				} else {
					mPlayer.setDataSource(mContext, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
				}
				mPlayer.setAudioStreamType(AudioManager.STREAM_NOTIFICATION);
				mPlayer.setLooping(false);
				mPlayer.prepare();
			}
		} catch (Exception e) {
			Log.i(LOG, "Create Player " + e.getLocalizedMessage());
		}
	}

	public synchronized void destroy() {
		try {
			if (mManager != null) {
				mManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, mVolume, 0);
				mManager = null;
			}
			if (mPlayer != null) {
				mPlayer.stop();
				mPlayer.release();
				mPlayer = null;
			}
		} catch (Exception e) {
			Log.d(LOG, "Destroy Player " + e.getLocalizedMessage());
		}
	}

	public void play() {
		if (mPlayer != null && !mPlayer.isPlaying()) {
			mPlayer.start();
		}
	}

}
