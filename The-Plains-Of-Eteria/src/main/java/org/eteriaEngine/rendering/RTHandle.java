package org.eteriaEngine.rendering;

import org.eteriaEngine.core.Screen;
import org.eteriaEngine.interfaces.IDisposable;
import org.eteriaEngine.rendering.enums.FilterMode;
import org.eteriaEngine.rendering.enums.GraphicsFormat;
import org.eteriaEngine.rendering.enums.TextureWrapMode;

import java.util.Objects;

/**
 * Render texture that resizes along with the screen.
 */
public class RTHandle implements IDisposable {
    private RenderTexture frameBuffer;
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
        frameBuffer = new RenderTexture(width, height, wrapMode, filterMode, graphicsFormat);
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
            frameBuffer = new RenderTexture(width, height, wrapMode, filterMode, graphicsFormat);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RTHandle rtHandle = (RTHandle) o;
        return width == rtHandle.width && height == rtHandle.height && Objects.equals(frameBuffer, rtHandle.frameBuffer) && filterMode == rtHandle.filterMode && wrapMode == rtHandle.wrapMode && graphicsFormat == rtHandle.graphicsFormat;
    }

    @Override
    public int hashCode() {
        return Objects.hash(frameBuffer, width, height, filterMode, wrapMode, graphicsFormat);
    }
}
