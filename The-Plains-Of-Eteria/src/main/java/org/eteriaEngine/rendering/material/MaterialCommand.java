package org.eteriaEngine.rendering.material;

import org.eteriaEngine.rendering.Texture2D;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL20.*;

/**
 * Command of material that handles uploading of values into the shader.
 */
class MaterialCommand<T> {

    private final Material material;
    private final int propertyLocation;
    private final PropertyType propertyType;
    private final T propertyValue;
    private final int textureUnit;

    MaterialCommand(Material material, int propertyLocation, PropertyType shaderPropertyType, T value) {
        this.material = material;
        this.propertyLocation = propertyLocation;
        this.propertyType = shaderPropertyType;
        this.propertyValue = value;
        this.textureUnit = -1;
    }
    MaterialCommand(Material material, int propertyLocation, PropertyType shaderPropertyType, T value, int textureUnit) {
        this.material = material;
        this.propertyLocation = propertyLocation;
        this.propertyType = shaderPropertyType;
        this.propertyValue = value;
        this.textureUnit = textureUnit;
    }

    /**
     * Applies current command to the shader.
     */
    public void applyCommand(){
        switch (propertyType){
            case INT -> glUniform1i(propertyLocation, (int) propertyValue);
            case FLOAT -> glUniform1f(propertyLocation, (float) propertyValue);
            case VECTOR2F -> {
                Vector2f vec2 = (Vector2f)propertyValue;
                glUniform2f(propertyLocation, vec2.x, vec2.y);
            }
            case VECTOR3F -> {
                Vector3f vec3 = (Vector3f)propertyValue;
                glUniform3f(propertyLocation, vec3.x, vec3.y, vec3.z);
            }
            case VECTOR4 -> {
                Vector4f vec4 = new Vector4f((Vector4f) propertyValue);
                glUniform4f(propertyLocation, vec4.x, vec4.y, vec4.z, vec4.w);
            }
            case MATRIX4F -> {
                FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
                Matrix4f mat4f = (Matrix4f) propertyValue;
                mat4f.get(matBuffer);
                glUniformMatrix4fv(propertyLocation, false, matBuffer);
            }
            case TEXTURE_2D -> {
                glUniform1f(propertyLocation, textureUnit);
                ((Texture2D) propertyValue).bind();
                activateTexture(textureUnit);
            }
        }
    }

    //Handles texture activation for textures in slots from 0 to 15.
    private void activateTexture(int unit){
        glActiveTexture(GL_TEXTURE0 + unit);
    }
    private void deactivateTexture(int unit){
        glDisable(GL_TEXTURE0 + unit);
    }
}
