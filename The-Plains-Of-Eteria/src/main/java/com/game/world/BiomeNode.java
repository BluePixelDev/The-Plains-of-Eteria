package com.game.world;

public class BiomeNode {
    private final BiomeType biomeType;
    private final float humidity;
    private final float temperature;

    public BiomeNode(BiomeType biomeType, float humidity, float temperature) {
        this.biomeType = biomeType;
        this.humidity = humidity;
        this.temperature = temperature;
    }

    public BiomeType getBiomeType() {
        return biomeType;
    }
    public float getHumidity() {
        return humidity;
    }
    public float getTemperature() {
        return temperature;
    }
}
