/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.views;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import com.naturecalling.R;
import org.me.database.Database;
import org.me.image.constant.ImageConstant;
import org.me.options.widget.TresholdSelector;

/**
 *
 * @author Jastrzab
 */
public class EventsAdapter extends BaseAdapter implements ImageConstant {

	private static final SimpleDateFormat mDateFormat = new SimpleDateFormat();
	private Context mContext;
	private LayoutInflater mInflater;
	private String mFormat;
	private Cursor mCursor;
	private int mCount = 0;

	public EventsAdapter(Context context) {
		mContext = context;
		mInflater = LayoutInflater.from(context);
		mFormat = context.getText(R.string.event_datatime_format).toString();
	}

	public void updateList(Database database) {
		try {
			if (Database.validCursor(mCursor)) {
				mCursor.close();
			}
			mCursor = database.retriveDatabase();
		} catch (Exception e) {
			Log.d("EventsAdapter", "Listing Events Fail " + e.getLocalizedMessage());
		}
		if (Database.validCursor(mCursor)) {
			mCount = mCursor.getCount();
			notifyDataSetChanged();
		}
	}

	public void clearList() {
		if (Database.validCursor(mCursor)) {
			mCursor.close();
		}
	}

	public int getCount() {
		return mCount;
	}

	public boolean isList() {
		return mCount > 0;
	}

	public Cursor getItem(int position) {
		if (Database.validCursor(mCursor) && mCursor.moveToPosition(position)) {
			return mCursor;
		}
		return null;
	}

	public long getItemId(int position) {
		return position;
	}

	public class ViewHolder {
		TextView title;
		TextView sensitivity;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.event_item, null);
			holder = new ViewHolder();
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.sensitivity = (TextView) convertView.findViewById(R.id.sensitivity);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (Database.validCursor(mCursor) && mCursor.moveToPosition(position)) {
			holder.title.setText(formatDate(mCursor.getLong(mCursor.getColumnIndex(Database.DATA_COLUMN)), mFormat));
			holder.sensitivity.setText(TresholdSelector.translateProgress(mContext, Math.abs(mCursor.getInt(mCursor.getColumnIndex(Database.THRESHOLD_COLUMN)) - 50)));
		}
		return convertView;
	}

	public static String formatDate(long data, String format) {
		mDateFormat.applyLocalizedPattern(format);
		return mDateFormat.format(data);
	}

}
