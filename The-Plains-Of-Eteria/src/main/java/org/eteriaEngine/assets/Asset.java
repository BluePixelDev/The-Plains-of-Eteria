package org.eteriaEngine.assets;

import org.eteriaEngine.interfaces.IDisposable;
import java.io.InputStream;

abstract class Asset<T> implements IDisposable {
    protected final String path;
    protected InputStream inputStream;
    protected boolean isLoaded;

    public Asset(String path, InputStream inputStream){
        this.path = path;
        this.inputStream = inputStream;
    }

    public String getPath() {
        return path;
    }
    public boolean isLoaded() {
        return isLoaded;
    }

    public abstract void loadAsset();
    public abstract T getAsset();
}
