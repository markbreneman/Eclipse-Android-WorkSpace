/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.telefony;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import org.me.constant.Constant;

/**
 *
 * @author Jastrzab
 */
public class CallState extends PhoneStateListener implements Constant {

	private Context mContext;
	private boolean mCallState = false;

	public CallState(Context context) {
		mContext = context;
	}

	@Override
	public void onCallStateChanged(int state, String incomingNumber) {
		switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:
				mCallState = true;
			break;
			case TelephonyManager.CALL_STATE_IDLE:
				mCallState = false;
			break;
		}
	}

	public boolean isCallState() {
		return mCallState;
	}

	public void addListner() {
		Telephon.addStateListner(mContext, CallState.this);
	}

	public void remListner() {
		Telephon.remStateListner(mContext, CallState.this);
	}

}
