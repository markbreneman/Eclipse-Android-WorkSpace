/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.me.core;

import android.os.Handler;
import java.util.concurrent.RejectedExecutionException;

/**
 *
 * @author Jastrząb
 */

public abstract class RepeatTask<Parametr> implements Runnable {

	private static enum Running {YES, NO};
	private long mCycleTime;
	private final Handler mHandler = new Handler();
	private volatile Running mRunnung = Running.NO;
	private Parametr mParamtr;

	public RepeatTask(Parametr paramtr) {
		mParamtr = paramtr;
	}

	public void start(final int statTime, final int cycleTime) throws RejectedExecutionException {
		if (mRunnung == Running.YES) {
			return;
		}
		mRunnung = Running.YES;
		mCycleTime = cycleTime;
		mHandler.postDelayed(this, statTime);
	}

	public void stop() {
		mRunnung = Running.NO;
		mHandler.removeCallbacks(this);
	}


	public void run() {
		execute(mParamtr);
		if (mRunnung == Running.YES) {
			mHandler.postDelayed(this, mCycleTime);
		}
	}

	protected abstract void execute(Parametr paramtr);

}
