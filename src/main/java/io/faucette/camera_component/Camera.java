package io.faucette.camera_component;


import io.faucette.math.Vec2;
import io.faucette.math.Vec4;
import io.faucette.math.Mat32;
import io.faucette.math.Mathf;

import io.faucette.scene_graph.Entity;
import io.faucette.scene_graph.Component;
import io.faucette.scene_graph.ComponentManager;

import io.faucette.transform_components.Transform2D;


public class Camera extends Component {
    protected boolean active;

    private float width;
    private float height;
    private float invWidth;
    private float invHeight;

    private float aspect;

    private float orthographicSize;

    private float minOrthographicSize;
    private float maxOrthographicSize;

    private Mat32 projection;
    private Mat32 view;

    private Vec4 background;

    private boolean needsUpdate;


    public Camera() {

        super();

        active = true;

        width = 960f;
        height = 640f;
        invWidth = 1f / width;
        invHeight = 1f / height;

        aspect = width / height;

        orthographicSize = 1f;

        minOrthographicSize = Mathf.EPSILON;
        maxOrthographicSize = 1024f;

        projection = new Mat32();
        view = new Mat32();

        background = new Vec4();

        needsUpdate = true;
    }

    public boolean isActive() {
        return active;
    }

    public Camera setActive() {

        this.active = true;

        if (hasComponentManager()) {
            ((CameraManager) getComponentManager()).setActiveCamera(this);
        }

        return this;
    }

    public Camera setBackground(Vec4 background) {
        this.background.copy(background);
        return this;
    }
    public Vec4 getBackground() {
        return this.background;
    }

    public Camera set(float width, float height) {

        this.width = width;
        this.height = height;

        invWidth = 1 / width;
        invHeight = 1 / height;

        aspect = width / height;
        needsUpdate = true;

        return this;
    }

    public Mat32 getView() {
        Entity entity = this.entity;
        Transform2D transform = entity != null ? entity.getComponent(Transform2D.class) : null;

        if (transform != null) {
            Mat32.inverse(view, transform.getMatrix());
        }

        return view;
    }
    public Mat32 getProjection() {
        updateMatrix2D();
        return projection;
    }

    public void updateMatrix2D() {
        updateMatrix2D(false);
    }
    public void updateMatrix2D(boolean force) {
        if (force || needsUpdate) {
            if (force || active) {
                needsUpdate = false;

                orthographicSize = Mathf.clamp(orthographicSize, minOrthographicSize, maxOrthographicSize);

                float right = orthographicSize * aspect;
                float left = -right;
                float top = orthographicSize;
                float bottom = -top;

                Mat32.orthographic(projection, top, right, bottom, left);
            }
        }
    }

    private static Mat32 toWorldMatrix = new Mat32();
    public Vec2 toWorld(Vec2 out, Vec2 v) {

        out.x = 2f * (v.x * invWidth) - 1f;
        out.y = -2f * (v.y * invHeight) + 1f;

        Mat32.mul(toWorldMatrix, getProjection(), getView());
        out.transform(toWorldMatrix.inverse());

        return out;
    }

    private Mat32 toScreenMatrix = new Mat32();
    private Vec2 toScreenVec = new Vec2();
    public Vec2 toScreen(Vec2 out, Vec2 v) {
        toScreenVec.copy(v);

        Mat32.mul(toScreenMatrix, getProjection(), getView());
        Vec2.transform(toScreenVec, toScreenVec, toScreenMatrix);

        out.x = ((toScreenVec.x + 1f) * 0.5f) * width;
        out.y = ((1f - toScreenVec.y) * 0.5f) * height;

        return out;
    }

    @Override
    public Class<? extends ComponentManager> getComponentManagerClass() {
        return CameraManager.class;
    }
    @Override
    public ComponentManager createComponentManager() {
        return new CameraManager();
    }

    @Override
    public Component clear() {

        active = true;

        width = 960f;
        height = 640f;
        invWidth = 1f / width;
        invHeight = 1f / height;

        aspect = width / height;

        orthographicSize = 1f;

        minOrthographicSize = Mathf.EPSILON;
        maxOrthographicSize = 1024f;

        projection = new Mat32();
        view = new Mat32();

        needsUpdate = true;

        return this;
    }
}
