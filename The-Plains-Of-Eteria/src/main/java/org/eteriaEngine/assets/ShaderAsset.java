package org.eteriaEngine.assets;

import org.eteriaEngine.rendering.Shader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;

class ShaderAsset extends Asset<Shader> {

    Shader shader = null;
    public ShaderAsset(String path, InputStream inputStream) {
        super(path, inputStream);
    }

    private int compileShader(){
        StringBuilder vertexBuilder = new StringBuilder();
        StringBuilder fragmentBuilder = new StringBuilder();

        vertexBuilder.append("#version 330 core \n");
        fragmentBuilder.append("#version 330 core \n");

        int lineMode = 0;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {

                if(line.contains("#VERTEX")){
                    lineMode = 1;
                    continue;
                }
                if(line.contains("#FRAGMENT")){
                    lineMode = 2;
                    continue;
                }

                switch (lineMode){
                    case 0 -> {
                        vertexBuilder.append(line).append("\n");
                        fragmentBuilder.append(line).append("\n");
                    }
                    case 1 -> vertexBuilder.append(line).append("\n");
                    case 2 -> fragmentBuilder.append(line).append("\n");
                }
            }
        } catch (IOException ex) {
            throw new RuntimeException("Failed to load a shader file!"
                    + System.lineSeparator() + ex.getMessage());
        }

        int vertexID = compileVertex(vertexBuilder.toString());
        int fragmentID = compileFragment(fragmentBuilder.toString());
        int shaderID = attachShader(vertexID, fragmentID);
        return shaderID;
    }
    private int attachShader(int vertexID, int fragmentID){
        int shaderID = glCreateProgram();
        glAttachShader(shaderID, vertexID);
        glAttachShader(shaderID, fragmentID);

        glLinkProgram(shaderID);
        int success = glGetProgrami(shaderID, GL_LINK_STATUS);
        if(success == GL_FALSE){
            int length = glGetProgrami(shaderID, GL_INFO_LOG_LENGTH);
            System.out.println("[SHADER ERROR] Linking shader error.");
            System.out.println(glGetProgramInfoLog(shaderID, length));
            assert false : "";
        }
        return shaderID;
    }
    private int compileVertex(String vertexSource){
        int program = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(program, vertexSource);
        glCompileShader(program);

        int success = glGetShaderi(program, GL_COMPILE_STATUS);
        if(success == GL_FALSE){
            int length = glGetShaderi(program, GL_INFO_LOG_LENGTH);
            System.out.println("[SHADER ERROR] Vertex shader compilation error.");
            System.out.println(glGetShaderInfoLog(program, length));
            assert false : "";
        }
        return program;
    }
    private int compileFragment(String fragmentSource){
        int program = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(program, fragmentSource);
        glCompileShader(program);

        int success = glGetShaderi(program, GL_COMPILE_STATUS);
        if(success == GL_FALSE){
            int length = glGetShaderi(program, GL_INFO_LOG_LENGTH);
            System.out.println("[SHADER ERROR] Fragment shader compilation error.");
            System.out.println(glGetShaderInfoLog(program, length));
            assert false : "";
        }
        return program;
    }

    @Override
    public void loadAsset() {
        if(shader != null){
            return;
        }
        shader = new Shader(compileShader());
    }
    @Override
    public Shader getAsset() {
        return shader;
    }

    @Override
    public void dispose() {
        glDeleteShader(shader.getShaderID());
        shader = null;
        isLoaded = false;
    }
}