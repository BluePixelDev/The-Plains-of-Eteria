package org.eteriaEngine.core;

public final class Resolution {
    private int width;
    private int height;
    private int refreshRate;

    public Resolution(int width, int height, int refreshRate) {
        this.width = width;
        this.height = height;
        this.refreshRate = refreshRate;
    }

    /**
    * Width of the resolution.
    */
    public int width() {
        return width;
    }
    /**
     * Height of the resolution.
     */
    public int height() {
        return height;
    }
    /**
     * Refresh rate of the resolution.
     */
    public int refreshRate() {
        return refreshRate;
    }
}
