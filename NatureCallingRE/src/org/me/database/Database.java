/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.me.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.io.FileWriter;
import org.me.constant.Constant;
import org.me.options.widget.TresholdSelector;
import org.me.views.EventsAdapter;

/**
 *
 * @author Jastrząb
 */
public final class Database extends SQLiteOpenHelper implements Constant {

	public static final long OPERATION_FAIL = -1;
	public static final String SINGLE = "1";
	public static final String EMPTY_STRING = "";
	public static final String TABLE_NAME = "Logs";
	public static final String ID_COLUMN = "_id";
	public static final String PROCENT_COLUMN = "procent";
	public static final String THRESHOLD_COLUMN = "threshold";
	public static final String DATA_COLUMN = "data";
	public static final String[] ALL_COLUMN = {"_id", "procent", "threshold", "data"};
	public static final String DATE_SORT_DESC = "data DESC";
	public static final String DATE_SORT_ASC = "data ASC";
	public static final String DATABASE_NAME = "Logs.db";
	public static final int DATABASE_VERSION = 2;
	private SQLiteDatabase mDatabase = null;
	private final ContentValues mInsertValues = new ContentValues();

	public Database(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public void onCreate() {
		synchronized (this) {
			if (mDatabase == null || (mDatabase != null && !mDatabase.isOpen())) {
				try {
					mDatabase = getWritableDatabase();
				} catch (Exception e) {
					mDatabase = null;
					Log.w("Database", "createDatabase " + e.getLocalizedMessage());
				}
			}
		}
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, procent INTEGER, threshold INTEGER, data INTEGER)");
		database.execSQL("CREATE INDEX IF NOT EXISTS IDX_TYPE ON " + TABLE_NAME + " (" + THRESHOLD_COLUMN + ")");
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(database);
	}

	public void onDestroy() {
		synchronized (this) {
			if (mDatabase != null && mDatabase.isOpen()) {
				try {
					mDatabase.close();
					mDatabase = null;
					SQLiteDatabase.releaseMemory();
				} catch (Exception e) {
					Log.w("Database", "destroyDatabase " + e.getLocalizedMessage());
				}
			}
		}
	}

	public static boolean validCursor(Cursor cursor) {
		return (cursor != null && !cursor.isClosed());
	}

	public SQLiteDatabase getDatabase() {
		return mDatabase;
	}

	public boolean deleteAll() {
		if (mDatabase != null) {
			try {
				return mDatabase.delete(TABLE_NAME, null, null) != 0;
			} catch (Exception e) {
				Log.w("Database", "deleteAll " + e.getLocalizedMessage());
			}
		}
		return false;
	}

	public long insertDatabase(int procent, int threshold) {
		if (mDatabase != null) {
			try {
				mInsertValues.clear();
				mInsertValues.put(Database.PROCENT_COLUMN, procent);
				mInsertValues.put(Database.THRESHOLD_COLUMN, threshold);
				mInsertValues.put(Database.DATA_COLUMN, System.currentTimeMillis());
				return mDatabase.insert(TABLE_NAME, EMPTY_STRING, mInsertValues);
			} catch (Exception e) {
				Log.w("Database", "insertDatabase " + e.getLocalizedMessage());
			}
		}
		return OPERATION_FAIL;
	}

	public Cursor retriveDatabase() {
		if (mDatabase != null) {
			try {
				return mDatabase.query(Database.TABLE_NAME, Database.ALL_COLUMN, null, null, null, null, Database.DATE_SORT_DESC);
			} catch (Exception e) {
				Log.w("Database", "retriveDatabase " + e.getLocalizedMessage());
			}
		}
		return null;
	}

	public boolean saveDatabase(Context context, FileWriter writer, String format) {
		if (mDatabase != null) {
			try {
				Cursor cursor = mDatabase.query(Database.TABLE_NAME, Database.ALL_COLUMN, null, null, null, null, Database.DATE_SORT_DESC);
				if (validCursor(cursor) && cursor.moveToFirst()) {
					int count = cursor.getCount();
					do {
						writer.append(EventsAdapter.formatDate(cursor.getLong(cursor.getColumnIndex(Database.DATA_COLUMN)), format));
						writer.append(COMMA);
						writer.append(TresholdSelector.translateProgress(context, Math.abs(cursor.getInt(cursor.getColumnIndex(Database.THRESHOLD_COLUMN)) - 50)));
						writer.append(NEW_LINE);
						count--;
					} while (cursor.moveToNext());
					return count == 0;
				}
			} catch (Exception e) {
				Log.w("Database", "saveDatabase " + e.getLocalizedMessage());
			}
		}
		return false;
	}

}
