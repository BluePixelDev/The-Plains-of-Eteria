package com.game.world;
import org.eteriaEngine.components.Component;
import org.eteriaEngine.components.GameObject;
import org.joml.Vector3f;

public class WorldManager extends Component {
    private WorldGenerator worldGenerator;
    private WorldMap worldMap;

    public WorldManager(GameObject gameObject) {
        super(gameObject);
    }

    public WorldMap getWorldMap() {
        return worldMap;
    }
    public void generateWorld(int chunkWidth, int chunkHeight){
        worldMap = getComponent(WorldMap.class);
        worldGenerator = new WorldGenerator(worldMap);
        worldGenerator.generateArea(new Vector3f(), chunkWidth,chunkHeight);
    }
    public void generateWorld(int chunkWidth, int chunkHeight, long seed){
        worldMap = new WorldMap(gameObject());
        worldGenerator = new WorldGenerator(worldMap, seed);
        worldGenerator.generateArea(new Vector3f(), chunkWidth, chunkHeight);
    }
}
