package org.eteriaEngine.core;

import java.awt.image.BufferedImage;

public final class Application {
    private static int targetFramerate = 0;

    public static int targetFramerate() {
        return targetFramerate;
    }
    public static void setTargetFramerate(int targetFramerate) {
        Application.targetFramerate = targetFramerate;
    }
    public static void setWindowTitle(String title){
        Window window = EteriaEngine.getWindow();
        window.setWindowTitle(title);
    }
    public static void setWindowIcon(BufferedImage image){
        EteriaEngine.getWindow().setWindowIcon(image);
    }
}
