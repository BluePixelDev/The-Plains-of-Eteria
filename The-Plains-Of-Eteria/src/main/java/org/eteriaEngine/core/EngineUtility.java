package org.eteriaEngine.core;

import org.eteriaEngine.rendering.Renderer;
import org.eteriaEngine.rendering.camera.Camera;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

public final class EngineUtility {
    //<editor-fold desc="Clamp Methods">

    // TODO: 06.05.2023 Implement method for checking if tile is in view.
    public static boolean isInView(Renderer renderer, Camera camera){
        return false;
    }


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
