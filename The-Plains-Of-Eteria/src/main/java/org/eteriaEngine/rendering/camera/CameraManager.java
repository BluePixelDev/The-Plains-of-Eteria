package org.eteriaEngine.rendering.camera;
import java.lang.ref.WeakReference;

public class CameraManager {
    private static WeakReference<Camera> currentCamera = null;

    public static Camera getCurrentCamera() {
        return currentCamera.get();
    }
    public static void setCurrentCamera(Camera currentCamera) {
        CameraManager.currentCamera = new WeakReference<>(currentCamera);
    }
}
