/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.constant;

/**
 *
 * @author Jastrzab
 */
public interface Constant {

	public static final String LOG = "Motion Detector";
	public static final String NEW_LINE = "\n\r";
	public static final String COMMA = ", ";
	public static final String PROCENT = "%";

	public enum Period {

		NULL (0),
		SECOUND (1000),
		TEN_SEC (10000),
		MINUTE (60000),
		HOUR (3600000);

		private final int value;

		Period(int value) {
		   this.value = value;
		}

		public int value() {
			return value;
		}

	}

	public static final int BRIGHTNESS = 150;
	public static final int PIXEL_THRESHOLD = 20;
	public static final int THRESHOLD_DEFAULT = 20;
	public static final float TOTAL_DIVIDER = 2.5f;

	public static final int ONE_SECOND = 1000;
	public static final int ONE_MINUTE = ONE_SECOND * 60;
	public static final int ONE_HOUR = ONE_MINUTE * 60;
	public static final int ONE_DAY = ONE_HOUR * 24;
	public static final int ONE_WEEK = ONE_DAY * 7;
	public static final int ONE_YEAR = ONE_DAY * 365;
	public static final int DEFAULT_CACHE_TIME = ONE_MINUTE;

	public static final String CONFIG_NAME_SIGNAL_SOUND = "signal_sound";
	public static final String CONFIG_NAME_CAMERA_NUMBER = "camera_number";
	public static final String CONFIG_NAME_START_DELAY = "start_delay";
	public static final String CONFIG_NAME_FRAME_COUNT = "frame_count";
	public static final String CONFIG_NAME_SAVE_FRAME = "save_frame";
	public static final String CONFIG_NAME_PLAY_SIGNAL = "play_signal";
	public static final String CONFIG_NAME_THRESHOLD = "threshold_value";
	public static final String CONFIG_NAME_WAIT_NEXT = "wait_next";
	public static final String CONFIG_NAME_WAIT_FRAME = "wait_frame";
	public static final String CONFIG_NAME_INDEX_GALERY = "index_galery";
	public static final String CONFIG_NAME_SAVE_LOGS = "save_logs";
	public static final String CONFIG_NAME_AUTO_START = "auto_start";
	public static final String CONFIG_NAME_DELETE_ON_START = "delete_on_start";

	public static final String DUMP_FILE_EXT = ".jpg";
	public static final String DUMP_FOLDER_NAME = "Motion Detector";

	public static final String DUMP_LOGS_EXT = ".log";

}
