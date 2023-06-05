package org.eteriaEngine.components;

public abstract class Component {
    private boolean isEnabled;
    private GameObject gameObject;

    public Component(GameObject gameObject) {
        this.gameObject = gameObject;
    }

    public boolean isEnabled() {
        return isEnabled;
    }
    public void setEnabled(boolean enabled) {
        if(isEnabled != enabled){
            if(enabled){
                onEnable();
            }
            else {
                onDisable();
            }
        }
        isEnabled = enabled;
    }

    public final GameObject gameObject(){
        return gameObject;
    }
    public final Transform transform(){
        return gameObject.transform();
    }

    /**
     * Returns component on this GameObject.
     */
    protected <T extends Component> T getComponent(Class<T> tClass){
        return gameObject.getComponent(tClass);
    }

    public void onEnable(){}
    public void start() {}
    public void update() {}
    public void onDisable(){}
    public void onRemove(){}
}