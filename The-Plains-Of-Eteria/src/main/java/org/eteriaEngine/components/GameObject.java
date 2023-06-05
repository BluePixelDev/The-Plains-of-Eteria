package org.eteriaEngine.components;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.UUID;

public final class GameObject {
    private final UUID id;
    private String name;
    private boolean isActive = true;
    private final Transform transform = new Transform(this);
    private final ArrayList<Component> components = new ArrayList<>();

    //ID
    public UUID getId(){
        return id;
    }
    //Name
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    //Is active
    public boolean isActive() {
        return isActive;
    }
    public void setActive(boolean active){
        if(!isActive && active){
            isActive = true;
            onEnable();
        }
        if(isActive && !active){
            isActive = false;
            onDisable();
        }
    }
    //Transform
    public Transform transform() {
        return transform;
    }
    //Components
    public <T extends Component> T addComponent(Class<T> type){
        T component;
        try{
            Constructor<?> constructor = type.getConstructor(GameObject.class);
            component = type.cast(constructor.newInstance(this));
            components.add(component);
            component.setEnabled(true);
            component.start();
            return component;

        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public GameObject removeComponent(Component component){
        component.onDisable();
        component.onRemove();
        components.remove(component);
        return this;
    }
    public <T extends Component> T getComponent(Class<T> type){
        T outComponent = null;
        for (Component component : components) {
            if(component.getClass().getName().equals(type.getName())){
                outComponent = type.cast(component);
            }
        }
        assert outComponent != null;
        outComponent.setEnabled(true);
        outComponent.start();
        return outComponent;
    }

    public GameObject(){
        id = java.util.UUID.randomUUID();
    }
    public GameObject(String name){
        this.name = name;
        id = java.util.UUID.randomUUID();
    }

    public void onEnable(){
        for (Component component: components) {
            component.onEnable();
        }
    }
    public void start(){
        if(!isActive){
            return;
        }
        for (Component component: components) {
            component.start();
        }
    }
    public void update(){
        if(!isActive){
            return;
        }
        for (Component component: components) {
            transform.update();
            component.update();
        }
    }
    public void onDisable(){
        for (Component component: components) {
            component.onDisable();
        }
    }
}
