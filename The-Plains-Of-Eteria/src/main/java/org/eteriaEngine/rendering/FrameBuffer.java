package org.eteriaEngine.rendering;

import org.eteriaEngine.interfaces.IDisposable;
import org.eteriaEngine.rendering.enums.FilterMode;
import org.eteriaEngine.rendering.enums.GraphicsFormat;
import org.eteriaEngine.rendering.enums.TextureWrapMode;

import static org.lwjgl.opengl.GL30.*;


/**
 * Using FrameBuffer we can render to non-default frame buffer and thus
 * render without disturbing the main screen.
 */
public class FrameBuffer implements IDisposable {
    private int width;
    private int height;

    private int bufferId;
    private Texture2D texture;

    public int getFrameBufferID(){
        return bufferId;
    }
    public Texture2D getTexture(){
        return texture;
    }

    public FrameBuffer(int width, int height, TextureWrapMode wrapMode, FilterMode filterMode, GraphicsFormat graphicsFormat){
        this.width = width;
        this.height = height;

        bufferId = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, bufferId);

        texture = new Texture2D(width, height, graphicsFormat);
        texture.setWrapMode(wrapMode);
        texture.setFilterMode(filterMode);

        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, texture.getTexID(), 0);

        int rboID = glGenRenderbuffers();
        glBindRenderbuffer(GL_RENDERBUFFER, rboID);
        glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH24_STENCIL8, width, height);
        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_STENCIL_ATTACHMENT, GL_RENDERBUFFER, rboID);

        if(glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE){
            System.out.println(glGetError());
        }

        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    /**
     * Binds this framebuffer to render into it.
     */
    public void bind(){
        glBindFramebuffer(GL_FRAMEBUFFER, bufferId);
        glViewport(0, 0, width, height);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }
    /**
     * Unbinds this framebuffer from rendering.
     */
    public void unbind(){
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    @Override
    public void dispose() {
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        glDeleteBuffers(bufferId);
    }
}
