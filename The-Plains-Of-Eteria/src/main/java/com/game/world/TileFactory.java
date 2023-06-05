package com.game.world;
import org.joml.Vector2f;

public class TileFactory {

    public static Tile GetTile(TileType tileType, BiomeType biomeType){
        switch (tileType){
            case WATER_DEEP, WATER: {
                Tile tile = new Tile(tileType, biomeType);
                return tile;
            }
            case SAND:{
                Tile tile = new Tile(tileType, biomeType);
                tile.setTextureOffset(new Vector2f(1.0f, 0.0f));
                return tile;
            }
            case DIRT: {
                Tile tile = new Tile(tileType, biomeType);
                tile.setTextureOffset(new Vector2f(2.0f, 0.0f));
                return tile;
            }
            case GRASS: {
                Tile tile = new Tile(tileType, biomeType);
                tile.setTextureOffset(new Vector2f(4.0f, 0.0f));
                return tile;
            }
        }
        return null;
    }
}
