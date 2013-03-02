/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.image.icons;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.WeakReference;
import org.me.image.constant.ImageConstant;
import org.me.image.holders.WorkHolder;
import org.me.image.templates.DataCache;
import org.me.threded.execute.Threded;

/**
 *
 * @author Jastrzab
 */
public final class ImageMatcher extends Threded<Object, Void, Bitmap> implements ImageConstant {

	private final Options mOptions = new Options();
	private WeakReference<ImageView> mImage;
	private DataCache<Bitmap> mCache;
	private String mFile;
	private float mSize;

	public ImageMatcher(ImageView image, DataCache<Bitmap> cache) {
		mImage = new WeakReference<ImageView>(image);
		mCache = cache;
	}

	private ImageView check() {
		if (mImage != null) {
			ImageView imageView = mImage.get();
			if (imageView != null) {
				if (this == get(imageView)) {
					return imageView;
				}
			}
		}
		return null;
	}

	private ImageMatcher get(ImageView imageView) {
		if (imageView != null) {
			Drawable drawable = imageView.getDrawable();
			if (drawable instanceof WorkHolder) {
				WorkHolder fetchDrawable = (WorkHolder) drawable;
				return fetchDrawable.getTask();
			}
		}
		return null;
	}

	@Override
	protected Bitmap doInBackground(Object... params) {
		mFile = (String) params[0];
		mSize = (Float) params[1];
		if (isCancelled() || check() == null) {
			return null;
		}
		if (mCache.containsKey(mFile)) {
			return mCache.get(mFile);
		} else {
			try {
				String fileType = getFileType(mFile);
				if (fileType == null) {
					return null;
				}
				if (fileType.startsWith(FILE_FORMAT_IMAGE)) {
					return getImage(mFile, mSize);
				}
			} catch (OutOfMemoryError e) {
				Log.d("Info", "Error " + e.getLocalizedMessage());
			} catch (Exception e) {
				Log.d("Info", "Exception ", e);
			}
		}
		return null;
	}

	@Override
	protected void onPostExecute(Bitmap bitmap) {
		if (bitmap != null) {
			ImageView imageView = check();
			if (imageView != null) {
				imageView.setImageBitmap(bitmap);
				if (!mCache.containsKey(mFile)) {
					try {
						mCache.put(mFile, bitmap);
					} catch (OutOfMemoryError e) {
						mCache.clear();
					}
				}
			}
		}
		mImage.clear();
	}

	public String getFileType(String file) {
		return android.webkit.MimeTypeMap.getSingleton().getMimeTypeFromExtension(getExtension(file));
	}

	public int getSample(String file, float thumbSize) throws FileNotFoundException, IOException, OutOfMemoryError {
		mOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(file, mOptions);
		int size = Math.round(Math.max(mOptions.outWidth, mOptions.outHeight) / thumbSize);
		if (size % 2 != 0) {
			size--;
		}
		return size;
	}

	public Bitmap getImage(String file, float thumbSize) throws FileNotFoundException, IOException, OutOfMemoryError {
		mOptions.inSampleSize = getSample(file, thumbSize);
		mOptions.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(file, mOptions);
	}

	public String getExtension(String file) {
		String array[] = file.split("\\.");
		return array[Math.max(0, array.length-1)];
	}

}
