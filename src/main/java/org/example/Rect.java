package org.example;

import org.lwjgl.opengl.GL11;

public class Rect {
    public float x, y, w, h;

    public Rect(float x, float y, float w, float h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public boolean intersects(Rect other) {
        return x < other.x + other.w
                && x + w > other.x
                && y < other.y + other.h
                && y + h > other.y;
    }

    public void render(float cameraX) {
        float rx = x - cameraX;
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(rx, y);
        GL11.glVertex2f(rx + w, y);
        GL11.glVertex2f(rx + w, y + h);
        GL11.glVertex2f(rx, y + h);
        GL11.glEnd();
    }
}
