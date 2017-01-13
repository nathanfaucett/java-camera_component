package io.faucette.camera_component;


import java.util.Comparator;
import java.util.Iterator;
import java.util.Arrays;

import io.faucette.transform_components.Transform2D;

import io.faucette.math.Vec2;
import io.faucette.scene_graph.Scene;
import io.faucette.scene_graph.Entity;

import static org.junit.Assert.*;
import org.junit.*;


public class CameraTest {
    @Test
    public void test() {
        Scene scene = new Scene();

        Camera camera = new Camera();
        Transform2D transform = new Transform2D();
        transform.setPosition(new Vec2(1f, 1f));

        scene.addEntity(new Entity()
            .addComponent(transform)
            .addComponent(camera));

        scene.init();

        float[] projection = {
            0.33333334f, 0f,
            0f, 0.5f,
            0f, 0f
        };
        assertArrayEquals(projection, camera.getProjection().getValues(), 0.0001f);

        float[] view = {
            1f, 0f,
            0f, 1f,
            -1f, -1f
        };
        assertArrayEquals(view, camera.getView().getValues(), 0.0001f);
    }
}
