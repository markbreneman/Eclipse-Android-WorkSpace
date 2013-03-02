/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jastrzab.move;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import org.me.constant.Constant;

/**
 *
 * @author Jastrzab
 */
public class Active extends BroadcastReceiver implements Constant {

	public static final String ACTION_SERVICE_CHANGE = "org.jastrzab.move.ACTION_SERVICE_CHAGE";
	public static final String ACTION_ACTIVITY_CHANGE = "org.jastrzab.move.ACTION_ACTIVITY_CHANGE";
	public static final String ACTION_TRESHOLD_CHANGE = "org.jastrzab.move.ACTION_TRESHOLD_CHANGE";
	public static final String ACTION_TRESHOLD_VALUE = "org.jastrzab.move.ACTION_TRESHOLD_VALUE";
	public static final String ACTION_WAIT_CHANGE = "org.jastrzab.move.ACTION_WAIT_CHANGE";
	public static final String ACTION_WAIT_VALUE = "org.jastrzab.move.ACTION_WAIT_VALUE";
	public static final int SERVICE_ENABLED = 0x1;
	public static final int SERVICE_DISABLED = 0x2;
	public static final int SERVICE_CHANGING = 0x3;
	private ActiveChaged mActiveChaged = null;

	public Active(ActiveChaged activeChaged) {
		mActiveChaged = activeChaged;
	}

	public static Intent getActicityIntent(Context context) {
		return new Intent(ACTION_SERVICE_CHANGE);
	}

	public static Intent getServiceIntent(Context context, Bundle bundle) {
		Intent mService = new Intent(context, Engine.class);
		mService.setAction(ACTION_ACTIVITY_CHANGE);
		if (bundle != null) {
			mService.putExtras(bundle);
		}
		return mService;
	}

	public static Intent getTresholdIntent(Context context, int treshold) {
		Intent mService = new Intent(context, Engine.class);
		mService.setAction(ACTION_TRESHOLD_CHANGE);
		mService.putExtra(ACTION_TRESHOLD_VALUE, treshold);
		return mService;
	}

	public static Intent getWaitIntent(Context context, int wait) {
		Intent mService = new Intent(context, Engine.class);
		mService.setAction(ACTION_WAIT_CHANGE);
		mService.putExtra(ACTION_WAIT_VALUE, wait);
		return mService;
	}

	private IntentFilter getReciverFilter() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(ACTION_SERVICE_CHANGE);
		return filter;
	}

	public void registerReceiver(Context context) {
		context.getApplicationContext().registerReceiver(Active.this, getReciverFilter());
	}

	public void removeReceiver(Context context) {
		try {
			context.getApplicationContext().unregisterReceiver(Active.this);
		} catch (IllegalArgumentException e) {
			Log.d(LOG, "Unregister Receiver");
		}
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(ACTION_SERVICE_CHANGE)) {
			mActiveChaged.onActiveChange();
		}
	}

	public interface ActiveChaged {

		void onActiveChange();

	}

}
