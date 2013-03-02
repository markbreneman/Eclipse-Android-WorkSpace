/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.media;

import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.YuvImage;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import org.me.constant.Constant;
import org.me.preview.camera.FrameBuffer;

/**
 *
 * @author Jastrzab
 */
public class FrameSaver implements Constant {

	private static final String FILE_NAME_FORMAT = "%s_%d_%d%s";
	private String mFolder = null;
	private List<String> mFiles = new ArrayList<String>();

	public FrameSaver() {
		createFolder();
	}

	public void saveFrame(Context context, long time, FrameBuffer frame, boolean first, boolean last, boolean index, int frameCount, int frameTotal) {
		FileOutputStream output = null;
		File file = null;
		try {
			if (first) {
				mFiles.clear();
			}
			file = getFile(time, frameCount, frameTotal);
			output = new FileOutputStream(file);
			YuvImage yuvImage = new YuvImage(frame.getFrame(), ImageFormat.NV21, frame.getWidth(), frame.getHeight(), null);
			yuvImage.compressToJpeg(frame.getRect(), 100, output);
			mFiles.add(file.getPath());
		} catch (Exception e) {
			Log.d(LOG, "Exception ", e);
		} finally {
			try {
				output.close();
			} catch (Exception e) {
				Log.d(LOG, "Exception ", e);
			}
			if (file != null) {
				file.setLastModified(time);
			}
		}
		if (index && last) {
			MediaScannerConnection.scanFile(context.getApplicationContext(), mFiles.toArray(new String[mFiles.size()]), null, null);
		}
	}

	private File getFile(long time, int frameCount, int frameTotal) {
		return new File(mFolder, String.format(FILE_NAME_FORMAT, time, frameCount, frameTotal, DUMP_FILE_EXT));
	}

	private void createFolder() {
		File file = new File(Environment.getExternalStorageDirectory(), DUMP_FOLDER_NAME);
		if (!file.exists()) {
			file.mkdirs();
		}
		mFolder = file.getPath();
	}

	public static boolean mediaMount() {
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}

}
