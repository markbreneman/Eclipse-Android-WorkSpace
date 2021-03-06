/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jastrzab.move;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.naturecalling.R;
import java.io.File;
import org.me.constant.Constant;
import org.me.image.constant.ImageConstant;
import org.me.image.constant.ImageImplements;
import org.me.views.Haze;
import org.me.views.ImageAdapter;

/**
 *
 * @author Jastrzab
 */
public class Images extends Activity implements Constant, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, Haze.HazeCallback {

	private ImageAdapter mAdapter;
	private String mFolder;
	private MenuItem mDelete;
	private Deleting mDeleting;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_list);
		mAdapter = new ImageAdapter(Images.this);
		ListView list = (ListView) findViewById(R.id.list);
		list.setAdapter(mAdapter);
		list.setEmptyView(findViewById(R.id.empty));
		list.setOnItemClickListener(Images.this);
		list.setOnItemLongClickListener(Images.this);
		mFolder = new File(Environment.getExternalStorageDirectory(), DUMP_FOLDER_NAME).getPath();
		mAdapter.updateList(mFolder);
		setEnabledMenu(mAdapter.isList());
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mAdapter.clearList();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (isFinishing()) {
			if (mDeleting != null) {
				mDeleting.abort();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_images, menu);
		mDelete = menu.findItem(R.id.delete_menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if (mDeleting != null && mDeleting.isRunning()) {
			mDelete.setEnabled(false);
		} else {
			setEnabledMenu(mAdapter.isList());
		}
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.delete_menu:
					Haze haze = new Haze(Images.this, R.string.haze_delete_del_id, R.string.delete_title, R.string.delete_question, Images.this);
					haze.deploy(R.string.ok, R.string.cancel);
				return true;
			default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void onPositive(int id) {
		if (mDeleting == null) {
			mDeleting = new Deleting(Images.this);
		}
		mDeleting.execute();
	}

	public void onNegative(int id) {

	}

	public void onNothingSelected(AdapterView<?> arg0) {
		
	}

	public void onItemClick(AdapterView<?> adapter, View arg1, int position, long arg3) {
		File file = (File) adapter.getItemAtPosition(position);
		if (file != null && file.exists()) {
			try {
				ImageImplements.openFile(file, Images.this);
			} catch (Exception e) {
				Log.d(LOG, "Open File Fail " + e.getLocalizedMessage());
			}
		} else {
			mAdapter.updateList(mFolder);
			setEnabledMenu(mAdapter.isList());
		}
	}

	public boolean onItemLongClick(AdapterView<?> adapter, View arg1, int position, long arg3) {
		File file = (File) adapter.getItemAtPosition(position);
		if (file != null && file.exists()) {
			try {
				ImageImplements.sendFile(file, Images.this);
			} catch (Exception e) {
				Log.d(LOG, "Send File Fail " + e.getLocalizedMessage());
			}
		} else {
			mAdapter.updateList(mFolder);
			setEnabledMenu(mAdapter.isList());
		}
		return true;
	}

	private final class Deleting {

		private volatile boolean mRunning = false;
		private Context mContext;
		private Delete mDelete;

		public Deleting(Context context) {
			mContext = context;
		}

		public synchronized void execute() {
			if (mRunning) {
				return;
			}
			mRunning = true;
			mDelete = new Delete();
			mDelete.execute();
		}

		public boolean isRunning() {
			return mRunning;
		}

		public void abort() {
			if (mDelete != null) {
				mDelete.cancel(false);
			}
		}

		private final class Delete extends AsyncTask<Object, Integer, Object> {

			private ProgressDialog mDialog;

			@Override
			protected void onPreExecute() {
				setEnabledMenu(false);
				mDialog = new ProgressDialog(mContext);
				mDialog.setCancelable(false);
				mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				mDialog.show();
			}

			@Override
			protected Object doInBackground(Object... arg0) {
				try {
					File[] array = new File(mFolder).listFiles(ImageConstant.mDefaultFilter);
					if (array != null) {
						mDialog.setMax(array.length);
						for (int i = 0; i < array.length; i++) {
							if (isCancelled()) {
								return null;
							}
							array[i].delete();
							publishProgress(i);
						}
					}
				} catch (Exception e) {
					Log.d(LOG, "Deleting file fail " + e.getLocalizedMessage());
				}
				return null;
			}

			@Override
			protected void onProgressUpdate(Integer... item) {
				mDialog.setProgress(item[0]);
			}

			@Override
			protected void onPostExecute(Object object) {
				mRunning = false;
				mDialog.dismiss();
				mAdapter.updateList(mFolder);
				setEnabledMenu(mAdapter.isList());
			}

		}

	}

	private void setEnabledMenu(boolean enabled) {
		if (mDelete != null) {
			mDelete.setEnabled(enabled);
		}
	}

}
