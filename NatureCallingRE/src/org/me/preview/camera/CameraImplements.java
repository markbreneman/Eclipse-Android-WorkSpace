/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.preview.camera;

import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.opengl.GLES20;
import android.os.Build;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.util.List;

/**
 *
 * @author Jastrzab
 */
public class CameraImplements {

	private static final int GL_TEXTURE_EXTERNAL_OES = 0x8D65;

	public static FrameBuffer getBuffer(Camera.Parameters parameters, PreviewFormat size) {
		int length = (size.width * size.height * ImageFormat.getBitsPerPixel(parameters.getPreviewFormat()) / 8);
		return new FrameBuffer(length, size.width, size.height);
	}

	public static SurfaceTexture getSurfaceTexture() {
		int[] textures = new int[1];
		GLES20.glGenTextures(1, textures, 0);
		GLES20.glBindTexture(GL_TEXTURE_EXTERNAL_OES, textures[0]);
		return new SurfaceTexture(textures[0]);
	}

	public static SurfaceHolder getSurfaceHolder(Context context) {
		SurfaceHolder surface = new SurfaceView(context).getHolder();
		surface.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		return surface;
	}
	
	public static boolean isSurfaceTexture() {
		return Build.VERSION.SDK_INT >= 11;
	}

	public static boolean isMultiCamera() {
		if (Build.VERSION.SDK_INT >= 9) {
			return Camera.getNumberOfCameras() > 1;
		} else {
			return false;
		}
	}

	public static int validCamera(int number) {
		return Math.min(number, Camera.getNumberOfCameras() - 1);
	}

	public static String getWhite(Parameters parameters) {
		List<String> supportedWhiteBalance = parameters.getSupportedWhiteBalance();
		if (supportedWhiteBalance != null) {
			for (String white : supportedWhiteBalance) {
				if (!white.equals(Parameters.WHITE_BALANCE_AUTO)) {
					return white;
				}
			}
		}
		return Parameters.WHITE_BALANCE_AUTO;
	}

}
