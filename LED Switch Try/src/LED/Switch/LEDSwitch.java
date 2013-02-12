package LED.Switch;

//this is the start of the copy code

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.android.future.usb.UsbAccessory;
import com.android.future.usb.UsbManager;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class Main extends Activity implements Runnable, OnSeekBarChangeListener  {
	
	private static final String TAG = "DemoKit";

	private static final String ACTION_USB_PERMISSION = "com.saltapps.helloADK.action.USB_PERMISSION";

	private UsbManager mUsbManager;					// This class allows you to access the state of USB and communicate with USB devices.
	private PendingIntent mPermissionIntent;		// By giving a PendingIntent to another application, you are granting it the right to perform
													// the operation you have specified as if the other application was yourself 
	private boolean mPermissionRequestPending;
	private SeekBar ledSeekBar;
	
	UsbAccessory mAccessory;
	ParcelFileDescriptor mFileDescriptor;
	FileInputStream mInputStream;
	FileOutputStream mOutputStream;

	public static final byte LED_SERVO_COMMAND = 2;

	private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (ACTION_USB_PERMISSION.equals(action)) {
				synchronized (this) {
					UsbAccessory accessory = UsbManager.getAccessory(intent);
					if (intent.getBooleanExtra(
							UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
						openAccessory(accessory);
					} else {
						Log.d(TAG, "permission denied for accessory "
								+ accessory);
					}
					mPermissionRequestPending = false;
				}
			} else if (UsbManager.ACTION_USB_ACCESSORY_DETACHED.equals(action)) {
				UsbAccessory accessory = UsbManager.getAccessory(intent);
				if (accessory != null && accessory.equals(mAccessory)) {
					closeAccessory();
				}
			}
		}
	};
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);

		mUsbManager = UsbManager.getInstance(this);
		mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(
				ACTION_USB_PERMISSION), 0);
		IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
		filter.addAction(UsbManager.ACTION_USB_ACCESSORY_DETACHED);
		registerReceiver(mUsbReceiver, filter);

		if (getLastNonConfigurationInstance() != null) {
			mAccessory = (UsbAccessory) getLastNonConfigurationInstance();
			openAccessory(mAccessory);
		}

		setContentView(R.layout.main);
		ledSeekBar = (SeekBar) findViewById(R.id.led_seek_bar);
    	ledSeekBar.setOnSeekBarChangeListener(this);
    	ledSeekBar.setMax(255);
    	
    }
    
    @Override
	public Object onRetainNonConfigurationInstance() {
		if (mAccessory != null) {
			return mAccessory;
		} else {
			return super.onRetainNonConfigurationInstance();
		}
	}
    
    @Override
	public void onResume() {
		super.onResume();

		Intent intent = getIntent();
		if (mInputStream != null && mOutputStream != null) {
			return;
		}

		UsbAccessory[] accessories = mUsbManager.getAccessoryList();
		UsbAccessory accessory = (accessories == null ? null : accessories[0]);
		if (accessory != null) {
			if (mUsbManager.hasPermission(accessory)) {
				openAccessory(accessory);
			} else {
				synchronized (mUsbReceiver) {
					if (!mPermissionRequestPending) {
						mUsbManager.requestPermission(accessory,
								mPermissionIntent);
						mPermissionRequestPending = true;
					}
				}
			} // if/else 2
		} else {
			Log.d(TAG, "mAccessory is null");
		}	// if/else 1
	}	// onResume
    
    @Override
	public void onPause() {
		super.onPause();
		closeAccessory();
	}	// onPause

	@Override
	public void onDestroy() {
		unregisterReceiver(mUsbReceiver);
		super.onDestroy();
	}	// onDestroy
	
	private void openAccessory(UsbAccessory accessory) {
		mFileDescriptor = mUsbManager.openAccessory(accessory);
		if (mFileDescriptor != null) {
			mAccessory = accessory;
			FileDescriptor fd = mFileDescriptor.getFileDescriptor();
			mInputStream = new FileInputStream(fd);
			mOutputStream = new FileOutputStream(fd);
			Thread thread = new Thread(null, this, "DemoKit");
			thread.start();
			Log.d(TAG, "accessory opened");
			enableControls(true);
		} else {
			Log.d(TAG, "accessory open fail");
		}	// if/else
	}	// openAccessory

	private void enableControls(boolean b) {
		
	}	// enableControls
	
	private void closeAccessory() {
		enableControls(false);

		try {
			if (mFileDescriptor != null) {
				mFileDescriptor.close();
			}
		} catch (IOException e) {
		} finally {
			mFileDescriptor = null;
			mAccessory = null;
		}	// try/catch
	}	// closeAccessory
	
	public void sendCommand(byte command, byte target, int value) {	// sends message to accessory
		byte[] buffer = new byte[3];
		if (value > 255)
			value = 255;

		buffer[0] = command;
		buffer[1] = target;
		buffer[2] = (byte) value;
		if (mOutputStream != null && buffer[1] != -1) {
			try {
				mOutputStream.write(buffer);
			} catch (IOException e) {
				Log.e(TAG, "write failed", e);
			}
		}
	}
	
	public void run() {
		int ret = 0;
		byte[] buffer = new byte[16384];
		int i;

		while (ret >= 0) {
			try {
				ret = mInputStream.read(buffer);
			} catch (IOException e) {
				break;
			}	// try/catch

			i = 0;
			while (i < ret) {
				int len = ret - i;

				switch (buffer[i]) {
				default:
					Log.d(TAG, "unknown msg: " + buffer[i]);
					i = len;
					break;
				}	// switch
			}	// while 2

		}	// while 1
	}	// run()	

	@Override
	public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
		sendCommand(LED_SERVO_COMMAND,(byte)0,(byte)progress);
	}	// onProgressChanged

	@Override
	public void onStartTrackingTouch(SeekBar arg0) {
	}	// onStartTrackingTouch

	@Override
	public void onStopTrackingTouch(SeekBar arg0) {
	}	// onStopTrackingTouch
	
}	// Class
