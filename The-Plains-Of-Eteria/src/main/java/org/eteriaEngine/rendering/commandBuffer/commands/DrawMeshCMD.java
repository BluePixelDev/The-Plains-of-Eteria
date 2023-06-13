package org.eteriaEngine.rendering.commandBuffer.commands;

import org.eteriaEngine.core.Screen;
import org.eteriaEngine.core.Time;
import org.eteriaEngine.interfaces.ICommand;
import org.eteriaEngine.rendering.camera.CameraManager;
import org.eteriaEngine.rendering.commandBuffer.CommandBuffer;
import org.eteriaEngine.rendering.enums.BlendingMode;
import org.eteriaEngine.rendering.enums.CullingMode;
import org.eteriaEngine.rendering.enums.DepthFunc;
import org.eteriaEngine.rendering.material.Material;
import org.eteriaEngine.rendering.mesh.Mesh;
import org.joml.Matrix4f;
import org.joml.Vector4f;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;

public class DrawMeshCMD implements ICommand {
        private final Mesh mesh;
        private final Matrix4f matrix;
        private final Material material;
        private final CommandBuffer commandBuffer;

        public DrawMeshCMD(Mesh mesh, Matrix4f matrix, Material material, CommandBuffer commandBuffer){
            this.mesh = mesh;
            this.matrix = matrix;
            this.material = material;
            this.commandBuffer = commandBuffer;
        }

        @Override
        public void execute() {
            material.bind();
            setMaterialProperties(material, matrix, commandBuffer.getRenderContext());
            material.apply();
            mesh.bind();

            //Draws all elements of the mesh.
            glDrawElements(GL_TRIANGLES, mesh.getIndicesCount(), GL_UNSIGNED_INT, 0);
            material.unbind();
            mesh.unbind();
        }

        private void setMaterialProperties(Material material, Matrix4f matrix, CommandBuffer.RenderContext renderContext){
            if(renderContext == null){
                commandBuffer.setRenderContext(
                        new CommandBuffer.RenderContext(
                                material.getCulling(),
                                material.getDepthFunc(),
                                material.getBlendingMode()
                        )
                );
            }
            else {
                if(material.getCulling() != renderContext.cullingMode()){
                    applyCulling(material.getCulling());
                }
                if(material.getDepthFunc() != renderContext.depthFunc()){
                    applyDepth(material.getDepthFunc());
                }
                if(material.getBlendingMode() != renderContext.blendingMode()){
                    applyBlending(material.getBlendingMode());
                }
            }

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

        private void applyCulling(CullingMode culling){
            glEnable(GL_CULL_FACE);
            switch (culling){
                case BACK -> glCullFace(GL_BACK);
                case FRONT -> glCullFace(GL_FRONT);
                case BOTH -> glDisable(GL_CULL_FACE);
            }
        }
        private void applyDepth(DepthFunc depthFunc){
            switch (depthFunc){
                case NEVER -> glDepthFunc(GL_NEVER);
                case LESS -> glDepthFunc(GL_LESS);
                case EQUAL -> glDepthFunc(GL_EQUAL);
                case LESS_EQUAL -> glDepthFunc(GL_LEQUAL);
                case GREATER -> glDepthFunc(GL_GREATER);
                case NOT_EQUAL -> glDepthFunc(GL_NOTEQUAL);
                case GREATER_EQUAL -> glDepthFunc(GL_GEQUAL);
                case ALWAYS -> glDepthFunc(GL_ALWAYS);
            }
        }
        private void applyBlending(BlendingMode blendingMode){
            glEnable(GL_BLEND);

            if(blendingMode == BlendingMode.ADDITIVE){
                glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            }
        }

}
