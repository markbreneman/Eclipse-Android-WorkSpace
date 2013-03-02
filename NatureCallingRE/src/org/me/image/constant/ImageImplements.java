/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.image.constant;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import java.io.File;
import com.naturecalling.R;

/**
 *
 * @author Jastrzab
 */
public final class ImageImplements implements ImageConstant {

	public static void sendFile(File file, Context context) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType(FILE_MIME);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + file.getPath()));
		context.startActivity(Intent.createChooser(intent, context.getResources().getString(R.string.send_using)));
	}

	public static void openFile(File file, Context context) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(Uri.fromFile(file), FILE_MIME);
		context.startActivity(Intent.createChooser(intent, context.getResources().getString(R.string.open_using)));
	}

	public static void sendWith(Context context, String data) {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_SEND);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setType("text/*");
		intent.putExtra(Intent.EXTRA_TEXT, data);
		context.startActivity(Intent.createChooser(intent, context.getResources().getString(R.string.send_using)));
	}

}
