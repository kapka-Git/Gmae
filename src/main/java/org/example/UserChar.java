package org.example;

import java.util.List;

import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.opengl.GL11;

public class UserChar {
    private static final int DOUBLE_JUMP_CHAR_ID = 2;
    private static final float DOUBLE_JUMP_VEL = -8f;

    private final CharacterDef def;
    private final float spawnX;
    private final float spawnY;

    private float x;
    private float y;
    private float velY = 0;
    private boolean onGround = false;
    private boolean doubleJumpArmed = false;

    public UserChar(CharacterDef def, float spawnX, float spawnY) {
        this.def = def;
        this.spawnX = spawnX;
        this.spawnY = spawnY;
        this.x = spawnX;
        this.y = spawnY;
    }

    public void update(long window, float gravity, List<Rect> platforms) {
        float velX = 0;
        if (glfwGetKey(window, GLFW_KEY_A) == GLFW_PRESS) velX = -def.speed();
        if (glfwGetKey(window, GLFW_KEY_D) == GLFW_PRESS) velX = def.speed();

        if (glfwGetKey(window, GLFW_KEY_W) == GLFW_PRESS && onGround) {
            velY = def.jumpS();
            onGround = false;
            if (def.id() == DOUBLE_JUMP_CHAR_ID) doubleJumpArmed = true;
        }

        if (def.id() == DOUBLE_JUMP_CHAR_ID
                && doubleJumpArmed
                && glfwGetKey(window, GLFW_KEY_SPACE) == GLFW_PRESS) {
            velY = DOUBLE_JUMP_VEL;
            doubleJumpArmed = false;
        }

        x += velX;
        Rect bx = bounds();
        for (Rect r : platforms) {
            if (bx.intersects(r)) {
                if (velX > 0) x = r.x - def.hitX();
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
                    y = r.y - def.hitY();
                    velY = 0;
                    onGround = true;
                    doubleJumpArmed = false;
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
        doubleJumpArmed = false;
    }

    public Rect bounds() {
        return new Rect(x, y, def.hitX(), def.hitY());
    }

    public void render(float cameraX) {
        GL11.glColor3f(
                def.color().getRed() / 255f,
                def.color().getGreen() / 255f,
                def.color().getBlue() / 255f
        );
        bounds().render(cameraX);
    }

    public float getX() { return x; }
    public float getY() { return y; }
}
