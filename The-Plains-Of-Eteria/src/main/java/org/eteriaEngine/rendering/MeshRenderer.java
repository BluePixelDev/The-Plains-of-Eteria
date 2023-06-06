package org.eteriaEngine.rendering;
import org.eteriaEngine.components.GameObject;
import org.eteriaEngine.rendering.camera.Camera;
import org.eteriaEngine.rendering.commands.CommandBuffer;
import org.eteriaEngine.rendering.material.Material;
import org.eteriaEngine.rendering.mesh.Mesh;

public class MeshRenderer extends Renderer {
    private Material material = null;
    private Mesh mesh = null;

    public Material getMaterial() {
        return material;
    }
    public void setMaterial(Material material){
        this.material = material;
    }

    public void setMesh(Mesh mesh) {
        this.mesh = mesh;
    }
    public Mesh getMesh() {
        return mesh;
    }
    public MeshRenderer(GameObject gameObject) {
        super(gameObject);
    }

    @Override
    public void render(Camera camera, CommandBuffer commandBuffer) {
        if(material == null){
            return;
        }
        if(mesh == null){
            return;
        }

       commandBuffer.drawMesh(mesh, transform().getTransformationMatrix(), material);
    }
}
