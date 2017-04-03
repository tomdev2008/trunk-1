package com.yang.camera.record;


import com.fatalsignal.util.DeviceUtils;

public class CameraUtils {
	/** 是否支持前置摄像头 */
	public static boolean isSupportFrontCamera() {
		if (!DeviceUtils.hasGingerbread()) {
			return false;
		}
		int numberOfCameras = android.hardware.Camera.getNumberOfCameras();
		if (2 == numberOfCameras) {
			return true;
		}
		return false;
	}
}
