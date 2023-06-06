package com.game;
import org.eteriaEngine.core.Time;
import org.eteriaEngine.components.Component;
import org.eteriaEngine.components.GameObject;
import org.joml.Vector3f;

import java.util.Random;

public class Shaker extends Component {

    Random random = new Random();
    private float shakeStrength = 5f;
    private float shakeTime = 2f;
    private Vector3f position;

    float timer = shakeTime;

    public Shaker(GameObject gameObject) {
        super(gameObject);
    }

    public void update(){
        timer -= Time.deltaTime();
        if(timer < 0){
            position = new Vector3f(0, 0, 0);
            position.add(random.nextFloat(), random.nextFloat(), random.nextFloat());
            position.mul(shakeStrength);

            transform().setPosition(position);
            timer = shakeTime;
        }
    }
}
