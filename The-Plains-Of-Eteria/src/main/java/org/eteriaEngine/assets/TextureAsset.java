package org.eteriaEngine.assets;

import org.eteriaEngine.rendering.Texture2D;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.InputStream;

class TextureAsset extends Asset<Texture2D> {

    private Texture2D texture;
    public TextureAsset(String path, InputStream inputStream) {
        super(path, inputStream);
    }

    @Override
    public void loadAsset(){
        try {
            texture = new Texture2D(ImageIO.read(inputStream));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public Texture2D getAsset() {
       return texture;
    }
    public void dispose() {
        texture.unbind();
        texture = null;
        isLoaded = false;
    }
}
