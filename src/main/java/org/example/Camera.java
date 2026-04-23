package org.example;

public class Camera {
    private float x = 0;
    private final float viewportHalfWidth;
    private final float smoothing;

    public Camera(float viewportHalfWidth, float smoothing) {
        this.viewportHalfWidth = viewportHalfWidth;
        this.smoothing = smoothing;
    }

    public void follow(float targetX) {
        x += ((targetX - viewportHalfWidth) - x) * smoothing;
    }

    public float getX() {
        return x;
    }
}
