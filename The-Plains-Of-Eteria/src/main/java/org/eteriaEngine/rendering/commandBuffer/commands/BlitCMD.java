package org.eteriaEngine.rendering.commandBuffer.commands;

import org.eteriaEngine.interfaces.ICommand;
import org.eteriaEngine.rendering.RTHandle;
import org.eteriaEngine.rendering.material.Material;
import org.eteriaEngine.rendering.mesh.Mesh;
import org.eteriaEngine.rendering.mesh.Primitives;

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
        material.bind();
        material.setTexture("_mainTex", source.getTexture());
        material.apply();

        Mesh mesh = Primitives.getScreenQuad();
        mesh.bind();

        //Draws all elements of the mesh.
        glDrawElements(GL_TRIANGLES, mesh.getIndicesCount(), GL_UNSIGNED_INT, 0);
        material.unbind();
        mesh.unbind();
    }
}
