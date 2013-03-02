/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.preview.camera;

import android.content.Context;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import java.util.concurrent.atomic.AtomicInteger;
import org.me.constant.Constant;

/**
 *
 * @author Jastrzab
 */
public class RemeinWake implements Constant {

	private static final AtomicInteger mLock = new AtomicInteger(0);
	private static WakeLock mWakeLock = null;

	public static void getLock(Context context) {
		synchronized (mLock) {
			if (mWakeLock == null) {
				PowerManager power = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
				mWakeLock = power.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "RemeinWake");
				mWakeLock.setReferenceCounted(false);
			}
			if (mLock.getAndIncrement() == 0) {
				Log.d(LOG, "Acquire Wake Lock");
				mWakeLock.acquire();
			}
		}
	}

	public static void leaveLock() {
		synchronized (mLock) {
			if (mLock.decrementAndGet() < 0) {
				mLock.set(0);
			}
			if (mLock.get() == 0 && mWakeLock != null && mWakeLock.isHeld()) {
				Log.d(LOG, "Releas Wake Lock");
				mWakeLock.release();
			}
		}
	}

	public static boolean isLock() {
		return mLock.get() > 0;
	}

}
