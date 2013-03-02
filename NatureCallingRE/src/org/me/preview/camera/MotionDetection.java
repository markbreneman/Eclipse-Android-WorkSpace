/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.preview.camera;

import org.me.constant.Constant;


/**
 *
 * @author Jastrzab
 */
public class MotionDetection implements Constant {

	private int mThreshold = THRESHOLD_DEFAULT;
	private int mPercent = 0;
	private long differentPixels;
	private long totalPixels;
	private long empty;
	private int luma;
	private int newPixel;
	private int oldPixel;

	private int validation(int input) {
		input = Math.max(input, 0);
		return Math.min(input, 255);
	}

	public int luminance(int pixel) {
		pixel = pixel + BRIGHTNESS;
		pixel = Math.max(0xff & pixel, 0);
		return Math.min(pixel, 255);
	}

	private int percent(float first, float secound) {
		return Math.round((Math.min(first, secound) / Math.max(first, secound)) * 100f);
	}

	public void setThreshold(int threshold) {
		mThreshold = threshold;
	}

	public int getThreshold() {
		return mThreshold;
	}

	public int getPercent() {
		return mPercent;
	}

	public boolean detect(byte[] current, int[] past, int width, int height) {
		differentPixels = 0;
		totalPixels = 0;
		empty = 0;
		for (int j = 0, yp = 0; j < height; j++) {
			for (int i = 0; i < width; i++, yp++) {
				luma = luminance(current[yp]);
				newPixel = validation((0xff & luma));
				oldPixel = validation((0xff & past[yp]));
				empty = empty + oldPixel;
				past[yp] = luma;
				totalPixels++;
				if (Math.abs(newPixel - oldPixel) > PIXEL_THRESHOLD) {
					differentPixels++;
				}
			}
		}
		mPercent = percent(Math.min(differentPixels, divideTotal(totalPixels)), divideTotal(totalPixels));
		return mPercent > mThreshold && empty > 0;
	}

	private long divideTotal(long total) {
		return (long) ((float) total / TOTAL_DIVIDER);
	}

}