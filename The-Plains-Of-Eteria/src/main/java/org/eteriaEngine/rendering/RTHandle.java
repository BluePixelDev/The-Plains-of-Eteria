package org.eteriaEngine.rendering;

import org.eteriaEngine.core.Screen;
import org.eteriaEngine.interfaces.IDisposable;
import org.eteriaEngine.rendering.enums.FilterMode;
import org.eteriaEngine.rendering.enums.GraphicsFormat;
import org.eteriaEngine.rendering.enums.TextureWrapMode;

/**
 * Render texture that resizes along with the screen.
 */
public class RTHandle implements IDisposable {
    private FrameBuffer frameBuffer;
    private int width, height;
    private final FilterMode filterMode;
    private final TextureWrapMode wrapMode;
    private final GraphicsFormat graphicsFormat;

    /**
     * Allocates new render texture with specified size.
     * Please note that the size cannot be 0 and will change along the screen size.
     */
    public static RTHandle Alloc(int width, int height, FilterMode filterMode, TextureWrapMode wrapMode, GraphicsFormat graphicsFormat){
        return new RTHandle(width, height, filterMode, wrapMode, graphicsFormat);
    }
    private RTHandle(int width, int height, FilterMode filterMode, TextureWrapMode wrapMode, GraphicsFormat graphicsFormat){
        this.width = width;
        this.height = height;

        this.filterMode = filterMode;
        this.wrapMode = wrapMode;
        this.graphicsFormat = graphicsFormat;
        frameBuffer = new FrameBuffer(width, height, wrapMode, filterMode, graphicsFormat);
    }

    public void bind(){
        if(frameBuffer == null){
            return;
        }

        int currentWidth = Screen.width();
        int currentHeight = Screen.height();

        if(currentWidth > width || currentHeight > height){
            frameBuffer.dispose();
            width = currentWidth;
            height = currentHeight;
            frameBuffer = new FrameBuffer(width, height, wrapMode, filterMode, graphicsFormat);
        }
        frameBuffer.bind();
    }
    public void unbind(){
        frameBuffer.unbind();
    }

    public Texture2D getTexture(){
        return frameBuffer.getTexture();
    }
    public int getRenderTargetID(){
        return frameBuffer.getFrameBufferID();
    }

    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }

    @Override
    public void dispose() {
        frameBuffer.dispose();
        frameBuffer = null;
    }
}
