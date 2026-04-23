package org.example;

import java.awt.Color;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.opengl.GL11;

public class UserChar {
    private final String name;
    private final int id;
    private final int hitX;
    private final int hitY;
    private final int jumpS;
    private final int speed;
    private final Color color;

    private final float spawnX;
    private final float spawnY;

    private float x;
    private float y;
    private float velY = 0;
    private boolean onGround = false;

    public UserChar(String name, int id, int hitX, int hitY,
                    float x, float y, int jumpS, int speed, Color color) {
        this.name = name;
        this.id = id;
        this.hitX = hitX;
        this.hitY = hitY;
        this.x = x;
        this.y = y;
        this.jumpS = jumpS;
        this.speed = speed;
        this.color = color;
        this.spawnX = x;
        this.spawnY = y;
    }

    public void update(long window, float gravity, List<Rect> platforms) {
        float velX = 0;
        if (glfwGetKey(window, GLFW_KEY_A) == GLFW_PRESS) velX = -speed;
        if (glfwGetKey(window, GLFW_KEY_D) == GLFW_PRESS) velX = speed;

        if (glfwGetKey(window, GLFW_KEY_W) == GLFW_PRESS && onGround) {
            velY = jumpS;
            onGround = false;
        }

        x += velX;
        Rect bx = bounds();
        for (Rect r : platforms) {
            if (bx.intersects(r)) {
                if (velX > 0) x = r.x - hitX;
                else if (velX < 0) x = r.x + r.w;
            }
        }

        velY += gravity;
        y += velY;
        Rect by = bounds();
        onGround = false;
        for (Rect r : platforms) {
            if (by.intersects(r)) {
                if (velY > 0) {
                    y = r.y - hitY;
                    velY = 0;
                    onGround = true;
                } else if (velY < 0) {
                    y = r.y + r.h;
                    velY = 0;
                }
            }
        }

        if (y > 650) respawn();
    }

    private void respawn() {
        x = spawnX;
        y = -50;
        velY = 0;
    }

    public Rect bounds() {
        return new Rect(x, y, hitX, hitY);
    }

    public void render(float cameraX) {
        GL11.glColor3f(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f);
        bounds().render(cameraX);
    }

    public float getX() { return x; }
    public float getY() { return y; }
}
