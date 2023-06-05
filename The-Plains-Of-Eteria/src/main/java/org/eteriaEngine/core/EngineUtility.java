package org.eteriaEngine.core;

import org.eteriaEngine.rendering.Renderer;
import org.eteriaEngine.rendering.camera.Camera;
import org.joml.Vector3f;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

public final class EngineUtility {
    //<editor-fold desc="Clamp Methods">

    // TODO: 06.05.2023 Implement method for checking if tile is in view.
    public static boolean isInView(Renderer renderer, Camera camera){
        return false;
    }
    public static Vector3f lerp(Vector3f a, Vector3f b, float t){
        t = clamp(t, 0, 1);
        Vector3f vecA = new Vector3f(a);
        vecA.mul(1 - t);

        Vector3f vecB = new Vector3f(b);
        vecB.mul(t);

        return vecA.add(vecB);
    }
    public static float lerp(float a, float b, float t){
        t = clamp(t, 0, 1);
        return (1 - t) * a + t * b;
    }
    public static double lerp(double a, double b, double t){
        t = clamp(t, 0, 1);
        return (1 - t) * a + t * b;
    }
    /**
     * Clamps value between min and max value.
     */
    public static float clamp(float value, float min, float max){
        return value < min? min : value > max? max : value;
    }
    /**
     * Clamps value between min and max value..
     */
    public static int clamp(int value, int min, int max){
        return value < min? min : value > max? max : value;
    }
    /**
     * Clamps value between min and max value.
     */
    public static double clamp(double value, double min, double max){
        return value < min? min : value > max? max : value;
    }
    //</editor-fold>

    /**
     * Converts BufferedImage into ByteBuffer.
     */
    public static ByteBuffer convertToByteBuffer(BufferedImage bufferedImage){
        int[] pixels = new int[bufferedImage.getWidth() * bufferedImage.getHeight()];
        bufferedImage.getRGB(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), pixels, 0, bufferedImage.getWidth());
        ByteBuffer dataBuffer = ByteBuffer.allocateDirect(bufferedImage.getWidth() * bufferedImage.getHeight() * 4);

        for(int h = 0; h < bufferedImage.getHeight(); h++) {
            for(int w = 0; w < bufferedImage.getWidth(); w++) {
                int pixel = pixels[h * bufferedImage.getWidth() + w];

                dataBuffer.put((byte) ((pixel >> 16) & 0xFF));
                dataBuffer.put((byte) ((pixel >> 8) & 0xFF));
                dataBuffer.put((byte) (pixel & 0xFF));
                dataBuffer.put((byte) ((pixel >> 24) & 0xFF));
            }
        }
        return dataBuffer.flip();
    }
}
