package com.example.kyscanner.security;
import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;

public class FlashLightManager {
    private static FlashLightManager instance;
    private boolean isFlashOn = false;  // Track flashlight state
    private CameraManager cameraManager;
    private String cameraId;

    private FlashLightManager(Context context) {
        cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        try {
            cameraId = cameraManager.getCameraIdList()[0]; // Get the back camera ID
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    public static FlashLightManager getInstance(Context context) {
        if (instance == null) {
            instance = new FlashLightManager(context);
        }
        return instance;
    }

    public void toggleFlashlight() {
        try {
            isFlashOn = !isFlashOn; // Toggle state
            cameraManager.setTorchMode(cameraId, isFlashOn);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    public boolean isFlashOn() {
        return isFlashOn;
    }
}
