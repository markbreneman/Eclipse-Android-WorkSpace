/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.image.constant;

import android.util.Log;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Comparator;

/**
 *
 * @author Jastrzab
 */
public interface ImageConstant {

	public static final String LOG = "Image Selector";
	public static final String FILE_EXT_UNLOCK = ".jpg";
	public static final String FILE_MIME = "image/jpeg";
	public static final String FILE_FORMAT_IMAGE = "image";

	public static final FilenameFilter mDefaultFilter = new FilenameFilter() {

		public boolean accept(File dir, String name) {
			return (dir.canWrite() && name.endsWith(FILE_EXT_UNLOCK));
		}

	};

	public static final Comparator<File> mDefaultSort = new Comparator<File>() {

		public int compare(File o1, File o2) {
			long f1 = o1.lastModified();
			long f2 = o2.lastModified();
			try {
				f1 = Long.valueOf(o1.getName().substring(0, 13));
				f2 = Long.valueOf(o2.getName().substring(0, 13));
			} catch (Exception e) {
				Log.d(LOG, e.getLocalizedMessage());
			}
			if (f1 > f2) {
				return -1;
			} else if (f1 < f2) {
				return +1;
			} else {
				return 0;
			}
		}
		
	};

}
