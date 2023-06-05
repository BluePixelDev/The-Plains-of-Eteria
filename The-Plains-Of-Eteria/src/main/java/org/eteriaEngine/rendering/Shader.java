package org.eteriaEngine.rendering;
import java.util.Objects;

public class Shader {
    private int shaderID;

    public int getShaderID(){
        return shaderID;
    }
    public Shader(int shader) {
        this.shaderID = shader;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shader shader = (Shader) o;
        return shaderID == shader.shaderID;
    }
    @Override
    public int hashCode() {
        return Objects.hash(shaderID);
    }
}
