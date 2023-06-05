package org.eteriaEngine.assets;

import java.io.InputStream;
import java.util.HashMap;

public class AssetDatabase {
    private static final HashMap<String, Asset<?>> assetCache = new HashMap<>();

    private AssetDatabase(){
    }

    /**
     * Loads asset or returns cached asset at specified path.
     */
    public static Object loadAssetAtPath(String filePath){
        String path = filePath.toLowerCase();
        Asset<?> asset = null;

        //Checks if the asset isn't cached
        if(assetCache.containsKey(path)){
            asset = assetCache.get(path);
        }
        else{
            try{
                InputStream inputStream = AssetDatabase.class.getResourceAsStream("/" + filePath);
                asset = generateAssetHolder(filePath, inputStream);

            }catch (Exception e){
                e.printStackTrace();
            }
            //Checks whether the asset loaded successfully
            if(asset != null){
                asset.loadAsset();
            }
            //Caches the newly loaded asset
            assetCache.put(path, asset);
        }

        assert asset != null;
        return asset.getAsset();
    }
    /**
     * Clears all cached Assets from Database.
     */
    public static void clearCache(){
        assetCache.clear();
    }
    /**
     * Unloads asset with specific path name.
     */
    public static void unloadAsset(String filePath){
        String localPath = filePath.toLowerCase();
        if(assetCache.containsKey(localPath)){
            Asset<?> asset = assetCache.get(localPath);
            asset.dispose();
        }
    }


    private static Asset<?> generateAssetHolder(String localPath, InputStream inputStream){
        String extension = getFileExtension(localPath);
        switch (extension){
            case "png","jpg" -> {
                return new TextureAsset(localPath, inputStream);
            }
            case "glsl" -> {
                return new ShaderAsset(localPath, inputStream);
            }
            case "txt" -> {
                return new TextAsset(localPath, inputStream);
            }
        }

        return null;
    }
    private static String getFileExtension(String name){
        int lastIndex = name.toLowerCase().lastIndexOf(".");
        return name.substring(lastIndex+1);
    }

    /**
     * Releases all currently loaded assets.
     */
    public static void release() {
        for (Asset<?> asset : assetCache.values()) {
            asset.dispose();
        }
    }
}