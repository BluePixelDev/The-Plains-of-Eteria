package org.eteriaEngine.rendering;

import org.eteriaEngine.core.EngineUtility;
import org.eteriaEngine.interfaces.IDisposable;
import org.eteriaEngine.rendering.enums.FilterMode;
import org.eteriaEngine.rendering.enums.GraphicsFormat;
import org.eteriaEngine.rendering.enums.TextureWrapMode;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;

public class Texture2D implements IDisposable {
    private int texID;
    private TextureWrapMode wrapMode = TextureWrapMode.REPEAT;
    private FilterMode filterMode = FilterMode.BILINEAR;
    private boolean markModified = false;

    /**
     * @return ID of this texture.
     */
    public int getTexID(){
        return texID;
    }

    /**
     * Changes texture wrapping mode.
     */
    public void setWrapMode(TextureWrapMode wrapMode) {
        if(this.wrapMode != wrapMode){
            this.wrapMode = wrapMode;
            markModified = true;
        }
    }
    /**
     * Changes texture filter mode.
     */
    public void setFilterMode(FilterMode filterMode) {
        if(this.filterMode != filterMode){
            this.filterMode = filterMode;
            markModified = true;
        }
    }

    private Texture2D(){
    }
    public Texture2D(BufferedImage bufferedImage){
        texID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texID);

        ByteBuffer imageBuffer = EngineUtility.convertToByteBuffer(bufferedImage);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, bufferedImage.getWidth(), bufferedImage.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, imageBuffer);
        glBindTexture(GL_TEXTURE_2D, 0);
        markModified = true;
    }
    public Texture2D(int width, int height){
        texID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texID);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, 0);
        glBindTexture(GL_TEXTURE_2D, 0);
        markModified = true;
    }
    public Texture2D(int width, int height, GraphicsFormat graphicsFormat){
        texID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texID);

        glTexImage2D(
                GL_TEXTURE_2D,
                0,
                RenderUtility.GetGraphicsFormat(graphicsFormat),
                width,
                height,
                0,
                RenderUtility.GetInternalFormat(graphicsFormat),
                GL_UNSIGNED_BYTE,
                0
        );

        glBindTexture(GL_TEXTURE_2D, 0);
        markModified = true;
    }

    /**
     * Binds this texture and prepares it for rendering.
     * All changes done to the texture are applied here.
     */
    public void bind(){
        glBindTexture(GL_TEXTURE_2D, texID);
        updateTextureParams();
    }
    /**
     * Unbinds this texture from rendering.
     */
    public void unbind(){
        glBindTexture(GL_TEXTURE_2D, 0);
    }
    /**
     * Deletes the texture.
     */
    public void dispose(){
        glDeleteTextures(texID);
        texID = 0;
    }

    //Updates texture parameters to be ready for drawing.
    private void updateTextureParams(){
        if(markModified){
            //Updating texture wrap
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, RenderUtility.GetWrapMode(wrapMode));
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, RenderUtility.GetWrapMode(wrapMode));

            //Updating texture filter
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, RenderUtility.GetFilterMode(filterMode));
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, RenderUtility.GetFilterMode(filterMode));

            glBindTexture(GL_TEXTURE_2D, 0);
            markModified = false;
        }
    }
}