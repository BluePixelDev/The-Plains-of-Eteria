package org.eteriaEngine.rendering.command;

import org.eteriaEngine.core.Screen;
import org.eteriaEngine.core.Time;
import org.eteriaEngine.interfaces.ICommand;
import org.eteriaEngine.rendering.camera.CameraManager;
import org.eteriaEngine.rendering.material.Material;
import org.eteriaEngine.rendering.mesh.Mesh;
import org.joml.Matrix4f;
import org.joml.Vector4f;

import static org.lwjgl.opengl.GL11.*;

public class DrawMeshCommand implements ICommand {

    private Mesh mesh;
    private Matrix4f matrix;
    private Material material;

    public DrawMeshCommand(Mesh mesh, Matrix4f matrix, Material material, CommandBuffer commandBuffer){
        this.mesh = mesh;
        this.matrix = matrix;
        this.material = material;
    }

    @Override
    public void execute() {
        material.bind();
        setMaterialProperties(material, matrix);
        material.apply();
        mesh.bind();

        //Draws all elements of the mesh.
        glDrawElements(GL_TRIANGLES, mesh.getElementLength(), GL_UNSIGNED_INT, 0);
        material.unbind();
        mesh.unbind();
    }

    private void setMaterialProperties(Material material, Matrix4f matrix){
        material.setFloat("_deltaTime", Time.deltaTime());
        material.setFloat("_timeSinceStart", Time.timeSinceStart());

        material.setVector4f("_ScreenParams",
                new Vector4f(
                        Screen.width(),
                        Screen.height(),
                        1.0f + 1.0f/Screen.width(),
                        1.0f + 1.0f/Screen.height()
                )
        );

        //Upload matrices for position, rotation and scale.
        material.setMatrix4f("_Projection", CameraManager.getCurrentCamera().getProjectionMatrix());
        material.setMatrix4f("_View", CameraManager.getCurrentCamera().getViewMatrix());
        material.setMatrix4f("_Transform", matrix);
    }
}
