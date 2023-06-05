package com.game.world;

import org.eteriaEngine.components.Component;
import org.eteriaEngine.components.GameObject;

import java.util.ArrayList;

public class WorldMap extends Component {

    private int tileSize;
    private float tileScale;
    private int chunkTileSize;

    private final ArrayList<Chunk> chunks = new ArrayList<>();
    public WorldMap(GameObject gameObject) {
        super(gameObject);
    }

    //---- TILE SIZE ----
    public int getTileSize() {
        return tileSize;
    }
    public void setTileSize(int tileSize) {
        this.tileSize = tileSize;
    }
    //---- TILE SCALE ----
    public float getTileScale() {
        return tileScale;
    }
    public void setTileScale(int tileScale) {
        this.tileScale = tileScale;
    }
    //---- CHUNK SIZE ----
    public int getChunkTileSize() {
        return chunkTileSize;
    }
    public void setChunkTileSize(int chunkTileSize) {
        this.chunkTileSize = chunkTileSize;
    }

    public void addChunk(Chunk chunk){
        chunks.add(chunk);
    }
    public void removeChunk(Chunk chunk){
        chunks.remove(chunk);
    }
    public ArrayList<Chunk> getChunks() {
        return chunks;
    }

    public float chunkUnitSize(){
        return chunkTileSize * tileScale;
    }
    public float chunkPixelSize(){
        return chunkTileSize * tileScale * tileSize;
    }
}
