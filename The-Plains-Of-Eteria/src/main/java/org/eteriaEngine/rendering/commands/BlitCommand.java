package org.eteriaEngine.rendering.commands;

import org.eteriaEngine.interfaces.ICommand;
import org.eteriaEngine.rendering.RTHandle;
import org.eteriaEngine.rendering.mesh.Mesh;
import org.eteriaEngine.rendering.mesh.Primitives;

import static org.lwjgl.opengl.GL11.*;

class BlitCommand implements ICommand {

    private final CommandBuffer commandBuffer;
    private final RTHandle source;

    BlitCommand(RTHandle source, CommandBuffer commandBuffer){
        this.source = source;
        this.commandBuffer = commandBuffer;
    }

    @Override
    public void execute() {
        commandBuffer.screenMaterial.setTexture("_MainTex", source.getTexture());
        commandBuffer.screenMaterial.apply();

        Mesh mesh = Primitives.getQuad();
        mesh.bind();

        //Draws all elements of the mesh.
        glDrawElements(GL_TRIANGLES, mesh.getElementLength(), GL_UNSIGNED_INT, 0);
        commandBuffer.screenMaterial.unbind();
        mesh.unbind();
    }
}
