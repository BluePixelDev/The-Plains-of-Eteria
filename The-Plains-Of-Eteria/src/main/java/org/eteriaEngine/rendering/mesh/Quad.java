package org.eteriaEngine.rendering.mesh;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.joml.Vector4f;

/**
 * Creates a new Mesh in the shape of a quad with a specified size.
 */
public class Quad extends Mesh {
    public Quad(float size){
        super();
        setVertices(getVertices(size, size));
        setElements(getElements());
    }
    public Quad(float width, float height){
        super();
        setVertices(getVertices(width, height));
        setColors(getColors());
        setUVs(getUVs());
        setElements(getElements());
    }

    public Vector3f[] getVertices(float width, float height){
        return new Vector3f[]{
                new Vector3f(width/2.0f, -height/2.0f, 0.0f),
                new Vector3f(-width/2.0f,  height/2.0f, 0.0f),
                new Vector3f(width/2.0f,  height/2.0f, 0.0f),
                new Vector3f(-width/2.0f, -height/2.0f, 0.0f)
        };
    }
    public Vector4f[] getColors(){
        return new Vector4f[]{
                new Vector4f(1.0f, 0.0f, 0.0f, 1.0f),
                new Vector4f(0.0f, 1.0f, 0.0f, 1.0f),
                new Vector4f(0.0f, 0.0f, 1.0f, 1.0f),
                new Vector4f(1.0f, 1.0f, 0.0f, 1.0f),
        };
    }
    public Vector2f[] getUVs(){
        return new Vector2f[]{
                new Vector2f(1.0f, 1.0f),
                new Vector2f(0.0f, 0.0f),
                new Vector2f(1.0f, 0.0f),
                new Vector2f(0.0f, 1.0f),
        };
    }

    public Vector3i[] getElements(){
        return new Vector3i[]{
               new Vector3i(2, 1, 0),
               new Vector3i(0, 1, 3)
        };
    }
}
