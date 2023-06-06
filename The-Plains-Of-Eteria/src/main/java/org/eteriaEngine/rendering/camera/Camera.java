package org.eteriaEngine.rendering.camera;

import org.eteriaEngine.components.Component;
import org.eteriaEngine.components.GameObject;
import org.eteriaEngine.core.Screen;
import org.eteriaEngine.rendering.enums.ProjectionMode;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera extends Component {
    private float fov = 70.0f;
    private float near = 0.1f;
    private float far = 250.0f;
    private ProjectionMode projectionMode = ProjectionMode.PERSPECTIVE;

    private final Matrix4f projectionMatrix;
    private final Matrix4f viewMatrix;

    public Camera(GameObject gameObject){
        super(gameObject);

        projectionMatrix = new Matrix4f();
        viewMatrix = new Matrix4f();

        adjustProjection();
        adjustView();
    }

    public float getFov() {
        return fov;
    }
    public void setFov(float fov) {
        this.fov = fov;
    }
    public float getNear() {
        return near;
    }
    public void setNear(float near) {
        if(near < 0.01f){
            near = 0.01f;
        }
        this.near = near;
    }
    public float getFar() {
        return far;
    }
    public void setFar(float far) {
        this.far = far;
    }
    public ProjectionMode getProjectionMode() {
        return projectionMode;
    }
    public void setProjectionMode(ProjectionMode projectionMode) {
        this.projectionMode = projectionMode;
    }

    public Matrix4f getViewMatrix(){
        return viewMatrix;
    }
    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    @Override
    public void update() {
        adjustProjection();
        adjustView();
    }

    //Updates projection matrix of this camera.
    private void adjustProjection(){
        projectionMatrix.identity();
        switch (projectionMode){
            case PERSPECTIVE -> projectionMatrix.perspective((float)Math.toRadians(fov), (float)Screen.width() / Screen.height(), near, far);
            case ORTHOGRAPHIC -> projectionMatrix.ortho(0.0f, Screen.width(), 0.0f, Screen.height(), near, far);
        }
    }
    //Updates view matrix of this camera.
    private void adjustView(){
        Vector3f position = new Vector3f(transform().getPosition());

        viewMatrix.identity();
        viewMatrix.lookAt(
                position,
                transform().back().add(position.x, position.y, position.z),
                transform().up()
        );
    }
}
