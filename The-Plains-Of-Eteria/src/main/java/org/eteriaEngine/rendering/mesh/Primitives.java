package org.eteriaEngine.rendering.mesh;

public class Primitives {
    private static final Mesh quad = new Quad(1, 1);
    private static final Mesh screenQuad = new Quad(2, 2);

    public static Mesh getQuad() {
        return quad;
    }
    public static Mesh getScreenQuad() {
        return screenQuad;
    }
}
