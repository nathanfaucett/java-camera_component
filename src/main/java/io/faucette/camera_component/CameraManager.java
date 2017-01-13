package io.faucette.camera_component;


import io.faucette.scene_graph.Component;
import io.faucette.scene_graph.ComponentManager;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;
import java.util.Iterator;


public class CameraManager extends ComponentManager {
    private Camera activeCamera;


    public CameraManager() {
        super();
        activeCamera = null;
    }

    public boolean hasActiveCamera() {
        return activeCamera != null;
    }
    public Camera getActiveCamera() {
        return activeCamera;
    }
    public CameraManager setActiveCamera(Camera camera) {

        if (activeCamera != null) {
            activeCamera.active = false;
        }

        activeCamera = camera;
        camera.active = true;

        return this;
    }

    @Override
    public <T extends Component> CameraManager addComponent(T component) {
        Camera camera = (Camera) component;

        if (camera.isActive() || activeCamera == null) {
            setActiveCamera(camera);
        }

        super.addComponent(component);

        return this;
    }
    @Override
    public <T extends Component> CameraManager removeComponent(T component) {
        super.removeComponent(component);

        Camera camera = (Camera) component;

        if (camera.isActive()) {
            if (components.size() != 0) {
                ((Camera) components.get(0)).setActive();
            } else {
                activeCamera = null;
            }
        }

        return this;
    }
}
