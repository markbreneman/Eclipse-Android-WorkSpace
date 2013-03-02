/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.preview.camera;

import android.os.ConditionVariable;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author Jastrzab
 */
public class ConcurentLock extends ConditionVariable {

	private volatile boolean mWork = false;
	private final AtomicBoolean mLock = new AtomicBoolean(false);

	public void setWait(boolean block) {
		mLock.set(block);
	}

	public boolean getWait() {
		return mLock.get();
	}

	public boolean isWaiting() {
		return mLock.compareAndSet(false, true);
	}

	public void setWorking(boolean work) {
		mWork = work;
	}

	public boolean isWorking() {
		return mWork;
	}

}
