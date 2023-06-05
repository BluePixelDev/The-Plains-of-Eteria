package org.eteriaEngine.assets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

class TextAsset extends Asset<String> {

    String data;
    public TextAsset(String path, InputStream inputStream) {
        super(path, inputStream);
    }

    @Override
    public void loadAsset() {
        if(data != "" && data != null){
            return;
        }

        StringBuilder builder = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }
        } catch (IOException ex) {
            throw new RuntimeException("Failed to load a shader file!"
                    + System.lineSeparator() + ex.getMessage());
        }
        data = builder.toString();
    }
    @Override
    public String getAsset() {

        return data;
    }

    public void dispose() {
        data = "";
        isLoaded = false;
    }
}
