package org.eteriaEngine.rendering;

import org.eteriaEngine.components.Component;
import org.eteriaEngine.components.GameObject;
import org.eteriaEngine.rendering.commandBuffer.CommandBuffer;
import org.eteriaEngine.rendering.camera.Camera;
import org.eteriaEngine.rendering.material.Material;
import org.joml.Vector3f;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;

public abstract class Renderer extends Component {
    private Material material = null;
    protected Vector3f bounds = new Vector3f();
    public Renderer(GameObject gameObject) {
        super(gameObject);
    }
    public Material getMaterial() {
        return material;
    }
    public void setMaterial(Material material) {
        if(material != this.material){
            this.material = material;
            loadMaterial();
        }
    }

    public Vector3f getBounds() {
        return bounds;
    }
    public void setBounds(Vector3f bounds) {
        this.bounds = bounds;
    }

    //Loads the material itself.
    private void loadMaterial(){
        if(material == null){
            return;
        }

        Shader shader = material.getShader();
        int shaderProgram = shader.getShaderID();
        glLinkProgram(shader.getShaderID());

        int success = glGetProgrami(shaderProgram, GL_LINK_STATUS);
        if(success == GL_FALSE){
            int length = glGetProgrami(shaderProgram, GL_INFO_LOG_LENGTH);
            System.out.println("[SHADER ERROR] Linking shader error.");
            System.out.println(glGetProgramInfoLog(shaderProgram, length));
            assert false : "";
        }
    }

    public abstract void render(Camera camera, CommandBuffer commandBuffer);

    @Override
    public void onEnable() {
        super.onEnable();
        RenderManager.get().addRenderer(this);
    }
    @Override
    public void onDisable() {
        super.onDisable();
        RenderManager.get().addRenderer(this);
    }
}
