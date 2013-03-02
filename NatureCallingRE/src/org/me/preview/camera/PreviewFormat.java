/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.preview.camera;

import android.graphics.Rect;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.me.constant.Constant;

/**
 *
 * @author Jastrzab
 */
public class PreviewFormat implements Constant, Serializable {

	private static final long serialVersionUID = 1348975023984398340L;
	private static final int MAX_PIXEL = 640 * 480;
	private static final float SUFRACE_RATIO = 1.5f;
	private static final String TO_STRING = "%d x %d";
	private static Comparator<Size> mSort = new Comparator<Size>() {

		@Override
		public int compare(Size aPattern, Size bPattern) {
			int aPixels = aPattern.height * aPattern.width;
			int bPixels = bPattern.height * bPattern.width;
			if (bPixels < aPixels) {
				return -1;
			}
			if (bPixels > aPixels) {
				return 1;
			}
			return 0;
		}

	};
	public int width = 0;
	public int height = 0;

	public PreviewFormat() {
		this(0, 0);
	}

	public PreviewFormat(PreviewFormat format) {
		this(format.width, format.height);
	}

	public PreviewFormat(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public void setFormat(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public Rect getRect() {
		return new Rect(0, 0, width, height);
	}

	@Override
	public String toString() {
		return String.format(TO_STRING, width, height);
	}

	public static PreviewFormat getPreviewSize(Parameters parameters) {
		if (parameters != null) {
			Size currentSize = parameters.getPreviewSize();
			PreviewFormat newSize = new PreviewFormat(currentSize.width, currentSize.height);
			List<Size> allSize = parameters.getSupportedPreviewSizes();
			if (allSize != null) {
				Collections.sort(allSize, mSort);
				float tempDiff = Float.POSITIVE_INFINITY;
				for (Size size : allSize) {
					boolean isPortrait = size.width < size.height;
					int isFlippedWidth = isPortrait ? size.height : size.width;
					int isFlippedHeight = isPortrait ? size.width : size.height;
					float newRatio = (float) isFlippedWidth / (float) isFlippedHeight;
					float newDiff = Math.abs(newRatio - SUFRACE_RATIO);
					if ((size.width * size.height) <= MAX_PIXEL && newDiff < tempDiff) {
						newSize.setFormat(size.width, size.height);
						tempDiff = newDiff;
					}
				}
			}
			return newSize;
		}
		return null;
	}

	public static PreviewFormat getMinSize(Parameters parameters) {
		if (parameters != null) {
			Size currentSize = parameters.getPreviewSize();
			PreviewFormat newSize = new PreviewFormat(currentSize.width, currentSize.height);
			for (Size size : parameters.getSupportedPreviewSizes()) {
				newSize.setFormat(Math.min(currentSize.width, size.width), Math.min(currentSize.height, size.height));
			}
			return newSize;
		}
		return null;
	}

}
