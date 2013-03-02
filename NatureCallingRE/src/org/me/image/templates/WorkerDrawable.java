/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.me.image.templates;

import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import java.lang.ref.WeakReference;

/**
 *
 * @author Jastrząb
 */
public class WorkerDrawable<Worker> extends BitmapDrawable {

	private WeakReference<Worker> mTaskReference;

	public WorkerDrawable(Worker task) {
		super();
		mTaskReference = new WeakReference<Worker>(task);
	}

	public WorkerDrawable(Worker task, Resources res, BitmapDrawable bitmap) {
		super(res, bitmap.getBitmap());
		mTaskReference = new WeakReference<Worker>(task);
	}

	public Worker getTask() {
		return mTaskReference.get();
	}
	
}

