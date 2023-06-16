package org.eteriaEngine.rendering.commandBuffer.commands;

import org.eteriaEngine.interfaces.ICommand;
import org.eteriaEngine.rendering.RTHandle;
import org.eteriaEngine.rendering.material.Material;
import org.eteriaEngine.rendering.mesh.Mesh;
import org.eteriaEngine.rendering.mesh.Primitive;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import static org.lwjgl.opengl.GL11.*;

public class BlitCMD implements ICommand {
    private final RTHandle source;
    private final Material material;

    public BlitCMD(RTHandle source, Material material){
        this.source = source;
        this.material = material;
    }

    @Override
    public void execute() {
        Matrix4f mat4f =  new Matrix4f().identity().scale(new Vector3f(2,2,2));

        material.bind();
        material.setTexture("_mainTex", source.getTexture());
        material.setMatrix4f("_Transform", mat4f);
        material.apply();

        Mesh mesh = Primitive.getQuad();
        mesh.bind();

        //Draws all elements of the mesh.
        glDrawElements(GL_TRIANGLES, mesh.getIndicesCount(), GL_UNSIGNED_INT, 0);
        material.unbind();
        mesh.unbind();
    }
}
