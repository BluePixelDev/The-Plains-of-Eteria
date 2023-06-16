package org.eteriaEngine.rendering.mesh;

public class Primitive {
    private Primitive(){

    }

    private static final Mesh quad = new Quad(1, 1);
    public static Mesh getQuad() {
        return quad;
    }
}
