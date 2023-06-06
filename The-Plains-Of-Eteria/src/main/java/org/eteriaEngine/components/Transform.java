package org.eteriaEngine.components;

import org.eteriaEngine.Mathf;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;

public class Transform extends Component{
    private Transform parent = null;
    private final ArrayList<Transform> children = new ArrayList<>();

    private Vector3f localPosition = new Vector3f();
    private Quaternionf localRotation = new Quaternionf();
    private Vector3f scale = new Vector3f(1f, 1f, 1f);
    private final Matrix4f transformationMatrix = new Matrix4f();

    Transform(GameObject gameObject){
        super(gameObject);
    }

    public Matrix4f getTransformationMatrix() {
        return new Matrix4f(transformationMatrix);
    }

    @Override
    public void update() {
        updateTransformMatrix(localPosition, localRotation, scale, true);
    }

    //<editor-fold desc="Parent/Children">
    //Internal methods
    void addChildren(Transform transform){
        children.add(transform);
    }
    void removeChildren(Transform transform){
        children.remove(transform);
    }

    /**
     * Sets a parent to this Transform.
     */
    public void setParent(Transform transform){
        if(transform != parent){
            //Binding parent
            if(transform != null){
                transform.addChildren(this);
                parent = transform;
            }
            else{
                //Unbinding parent.
                transform.removeChildren(this);
                Vector3f pos = new Vector3f();
                Quaternionf rot = new Quaternionf();
                Vector3f sca = new Vector3f();

                transformationMatrix.transformPosition(pos);
                transformationMatrix.rotation(rot);
                transformationMatrix.scale(sca);

                localPosition = pos;
                localRotation = rot;
                scale = sca;
                parent = null;
            }
        }
    }
    /**
     * Returns parent of this Transform
     */
    public Transform getParent() {
        return parent;
    }
    /**
     * Returns an array of all children Transforms.
     */
    public Transform[] getChildren(){
        Transform[] result = new Transform[children.size()];
        return children.toArray(result);
    }
    //</editor-fold>

    //<editor-fold desc="Directions">
    /**
     * The up direction from the Transform.
     */
    public Vector3f up(){
        Vector3f up = new Vector3f(0.0f, 1.0f, 0.0f);
        up.rotate(getRotation());
        return up;
    }
    /**
     * The forward direction from the Transform.
     */
    public Vector3f forward(){
        Vector3f forward = new Vector3f(0.0f, 0.0f, 1.0f);
        forward.rotate(getRotation());
        return forward;
    }
    /**
     * The forward direction from the Transform.
     */
    public Vector3f back(){
        Vector3f back = new Vector3f(0.0f, 0.0f, -1.0f);
        back.rotate(getRotation());
        return back;
    }
    /**
     * The right direction from the Transform.
     */
    public Vector3f right(){
        Vector3f right = new Vector3f(1.0f, 0.0f, 0.0f);
        right.rotate(getRotation());
        return right;
    }
    //</editor-fold>

    //---- LOCAL TRANSFORMATIONS ----

    //<editor-fold desc="Position - LOCAL">

    /**
     * @return Position of this Transform in local space.
     */
    public Vector3f getLocalPosition() {
        return new Vector3f(localPosition);
    }
    /**
     * Sets position of this transform in local space.
     */
    public void setLocalPosition(Vector3f localPosition) {
        this.localPosition = new Vector3f(localPosition);
    }
    //</editor-fold>

    //<editor-fold desc="Rotation - LOCAL">
    /**
     * @return Rotation of this Transform in local space.
     */
    public Quaternionf getLocalRotation() {
        return new Quaternionf(localRotation);
    }
    /**
     * Sets rotation of thi Transform in local space.
     */
    public void setLocalRotation(Quaternionf localRotation) {
        this.localRotation = new Quaternionf(localRotation);
    }
    //</editor-fold>

    //<editor-fold desc="Scale - LOCAL">
    /**
     * @return Scale of this Transform in local space.
     */
    public Vector3f getScale() {
        return new Vector3f(scale);
    }
    /**
     * Sets scale of this Transform in local space.
     */
    public void setScale(Vector3f scale) {
        this.scale = new Vector3f(scale);
    }

    //</editor-fold>

    //---- WORLD TRANSFORMATIONS ----

    //<editor-fold desc="Position - WORLD">
    public Vector3f getPosition(){
        if(parent != null){
            Vector3f position = new Vector3f(localPosition);
            position.rotate(parent.getRotation());
            position.mul(parent.getScale());
            position.add(parent.getPosition());
            return position;
        }

        return new Vector3f(localPosition);
    }
    public void setPosition(Vector3f position){
        if(parent != null){
            setLocalPosition(position);
            updateTransformMatrix(position, localRotation, getLossyScale(), false);
        }
        else{
            setLocalPosition(position);
        }
    }
    //</editor-fold>

    //<editor-fold desc="Rotation - WORLD">
    /**
     * @return Rotation of this Transform in world space.
     */
    public Quaternionf getRotation(){
        if(parent != null){
            Quaternionf rotation = new Quaternionf(parent.getRotation());
            rotation.mul(localRotation);
            return rotation;
        }
        return new Quaternionf(localRotation);
    }
    /**
     * Sets rotation of this Transform in world space.
     */
    public void setRotation(Quaternionf rotation){
        if(parent != null){
            Quaternionf rot = new Quaternionf(localRotation);
            Quaternionf invRot = new Quaternionf(rotation);
            invRot.invert();
            rot.mul(invRot);
            setLocalRotation(rot);
        }else{
            setLocalRotation(rotation);
        }
    }
    //</editor-fold>

    //<editor-fold desc="Scale - WORLD">
    /**
     * Returns scale of this Transform in world space.
     */
    public Vector3f getLossyScale(){
        if(parent != null){
            return parent.getLossyScale().mul(scale);
        }
        return new Vector3f(scale);
    }
    //</editor-fold>

    //---- ROTATION ----
    /**
     * Rotates transform in world space around axis with given angle.
     */
    public void rotate(float angle, Vector3f axis){
        angle = angle * Mathf.degToRad;
        setRotation(getRotation().rotateAxis(angle, axis));
    }
    /**
     * Rotates transform in local space around axis with given angle.
     */
    public void rotateLocal(float angle, Vector3f axis){
        setLocalRotation(getLocalRotation().rotateAxis(angle, axis));
    }

    //---- TRANSLATION ----
    /**
     * Moves transform in world space in specified direction.
     */
    public void translate(Vector3f direction){
        setPosition(getPosition().add(direction));
    }
    /**
     * Moves transform in local space in specified direction.
     */
    public void translateLocal(Vector3f direction){
        setLocalPosition(getLocalPosition().add(direction));
    }

    //---- SCALING ----
    /**
     * Scales transform in local space by specified scale.
     */
    public void scale(Vector3f vector){
        setScale(getScale().mul(vector));
    }

    //Updates transformation Matrix with current values.
    private void updateTransformMatrix(Vector3f position , Quaternionf rotation, Vector3f scale, boolean includeParent){
        transformationMatrix.identity();

        if(parent != null && includeParent){
            transformationMatrix.mul(parent.getTransformationMatrix());
        }

        Vector3f pos = new Vector3f(position);
        pos.x = -pos.x;
        pos.y = -pos.y;

        transformationMatrix.translate(pos);
        transformationMatrix.rotate(rotation);
        transformationMatrix.scale(scale);
    }
}