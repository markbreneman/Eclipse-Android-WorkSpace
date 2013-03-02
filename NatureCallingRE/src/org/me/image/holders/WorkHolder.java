/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.image.holders;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import org.me.image.icons.ImageMatcher;
import org.me.image.templates.WorkerDrawable;

/**
 *
 * @author Jastrzab
 */
public final class WorkHolder extends WorkerDrawable<ImageMatcher> {

	public WorkHolder(ImageMatcher task) {
		super(task);
	}

	public WorkHolder(ImageMatcher bitmapFetchTask, Resources res, Bitmap bitmap) {
		super(bitmapFetchTask, res, new BitmapDrawable(bitmap));
	}

}