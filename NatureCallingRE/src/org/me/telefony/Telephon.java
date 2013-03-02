/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.telefony;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

/**
 *
 * @author Jastrzab
 */
public class Telephon {

	public static TelephonyManager getService(Context context) {
		return (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
	}

	public static void addStateListner(Context context, PhoneStateListener listener) {
		getService(context).listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
	}

	public static void remStateListner(Context context, PhoneStateListener listener) {
		getService(context).listen(listener, PhoneStateListener.LISTEN_NONE);
	}

}
