package org.eteriaEngine.rendering.mesh;

import org.eteriaEngine.interfaces.IDisposable;
import org.eteriaEngine.rendering.InternalRenderUtility;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;

public class Mesh implements IDisposable {
    private final int meshID;
    private int vboID;
    private int eboID;

    private boolean markDynamic;
    private boolean markModified;
    private boolean markReadOnly;

    private Vector3f[] vertices = new Vector3f[0];
    private Vector4f[] colors = new Vector4f[0];
    private Vector2f[] UVs = new Vector2f[0];

    private Vector3i[] elements = new Vector3i[0];

    private int allocatedVertexBufferSize;
    private int allocatedElementBufferSize;

    public Mesh(){
        meshID = glGenVertexArrays();
    }
    Mesh(int meshID){
        this.meshID = meshID;
        markReadOnly = true;
    }


    public int getMeshID() {
        return meshID;
    }
    public int getIndicesCount(){
        return elements.length * 3;
    }

    /**
     * Marks this mesh as dynamic.
     * If mesh is marked as dynamic it will use Dynamic buffers.
     */
    public void markDynamic(boolean markDynamic){
        if(this.markDynamic != markDynamic){
            this.markDynamic = markDynamic;
            allocateVertexBuffer(markDynamic, generateVertexBuffer());
        }
    }

    /**
     * Sets individual vertex positions of a mesh.
     */
    public void setVertices(Vector3f[] vertices){
        this.vertices = vertices;
        markModified = true;
    }
    /**
     * Sets individual vertex colors of a mesh.
     */
    public void setColors(Vector4f[] colors){
        this.colors = colors;
        markModified = true;
    }
    /**
     * Sets individual UV positions of a mesh.
     */
    public void setUVs(Vector2f[] UVs){
        this.UVs = UVs;
        markModified = true;
    }
    public void setElements(Vector3i[] elements){
        this.elements = elements;
        markModified = true;
    }

    //Handles updating of mesh vertices and elements.
    private void updateMesh(){
        if(markReadOnly){
            return;
        }

        FloatBuffer vertexBuffer = generateVertexBuffer();
        IntBuffer elementBuffer = generateElementBuffer();

        int vertexBufferSize = vertexBuffer.capacity();
        int elementBufferSize = elementBuffer.capacity();

        if(vertexBufferSize > allocatedVertexBufferSize){
            allocateVertexBuffer(markDynamic, vertexBuffer);
        }
        else{
            updateVertexBuffer(vertexBuffer);
        }

        if(elementBufferSize > allocatedElementBufferSize){
            allocateElementBuffer(markDynamic, elementBuffer);
        }
        else{
            updateElementBuffer(elementBuffer);
        }
    }

    //---- VERTICES ----
    //Generates new vertex buffer based on Vertices, Colors and UVs.
    private FloatBuffer generateVertexBuffer(){
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertices.length * InternalRenderUtility.VERTEX_SIZE);
        for(int i = 0; i < vertices.length ; i++){
            //Position
            vertexBuffer.put(vertices[i].x);
            vertexBuffer.put(vertices[i].y);
            vertexBuffer.put(vertices[i].z);

            //Color
            if(colors.length > i){
                vertexBuffer.put(colors[i].x);
                vertexBuffer.put(colors[i].y);
                vertexBuffer.put(colors[i].z);
                vertexBuffer.put(colors[i].w);
            }
            else{
                vertexBuffer.put(0);
                vertexBuffer.put(0);
                vertexBuffer.put(0);
                vertexBuffer.put(0);
            }

            //uv
            if(UVs.length > i) {
                vertexBuffer.put(UVs[i].x);
                vertexBuffer.put(UVs[i].y);
            }
            else{
                vertexBuffer.put(0);
                vertexBuffer.put(0);
            }
        }
        vertexBuffer.flip();
        return vertexBuffer;
    }
    //Allocates new space on the GPU to store the mesh vertices.
    private void allocateVertexBuffer(boolean isDynamic, FloatBuffer vertexBuffer){
        glDeleteBuffers(vboID);
        glBindVertexArray(meshID);

        int drawMode = isDynamic ? GL_DYNAMIC_DRAW : GL_STATIC_DRAW;

        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, drawMode);
        allocatedVertexBufferSize = vertexBuffer.capacity();

        GL20.glVertexAttribPointer(0, InternalRenderUtility.POSITION_SIZE, GL_FLOAT, false, InternalRenderUtility.VERTEX_SIZE_BYTES, InternalRenderUtility.POSITION_POINTER);
        glVertexAttribPointer(1, InternalRenderUtility.COLOR_SIZE, GL_FLOAT, false, InternalRenderUtility.VERTEX_SIZE_BYTES, InternalRenderUtility.COLOR_POINTER);
        glVertexAttribPointer(2, InternalRenderUtility.UV_SIZE, GL_FLOAT, false, InternalRenderUtility.VERTEX_SIZE_BYTES, InternalRenderUtility.UV_POINTER);

        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
    }
    //Updates mesh's Vertex Buffer.
    private void updateVertexBuffer(FloatBuffer vertexBuffer){
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertexBuffer);
    }

    //---- ELEMENTS ----
    private void allocateElementBuffer(boolean isDynamic, IntBuffer elementBuffer){
        glDeleteBuffers(eboID);
        glBindVertexArray(meshID);

        int drawMode = isDynamic ? GL_DYNAMIC_DRAW : GL_STATIC_DRAW;

        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, drawMode);
        allocatedElementBufferSize = elementBuffer.capacity();
    }
    private IntBuffer generateElementBuffer(){
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elements.length * 3);
        for (Vector3i element : elements) {
            elementBuffer.put(element.x);
            elementBuffer.put(element.y);
            elementBuffer.put(element.z);
        }
        elementBuffer.flip();
        return elementBuffer;
    }
    //Updates mesh's Element Buffer.
    private void updateElementBuffer(IntBuffer elementBuffer){
        glBindBuffer(GL_ARRAY_BUFFER, eboID);
        glBufferSubData(GL_ARRAY_BUFFER, 0, elementBuffer);
    }

    /**
     * Binds this mesh to rendering.
     */
    public void bind(){
        if(markModified){
            updateMesh();
            markModified = false;
        }
        glBindVertexArray(meshID);
    }
    /**
     * Unbinds this mesh from rendering.
     */
    public void unbind(){
        glBindVertexArray(0);
    }
    /**
     * Deletes this mesh EBO, VAO and VBO.
     */
    public void dispose(){
        if(!markReadOnly){
            glDeleteVertexArrays(meshID);
        }
    }
}