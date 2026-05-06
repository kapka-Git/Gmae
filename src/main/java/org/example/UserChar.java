package org.example;

import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.opengl.GL11;

public class UserChar {

    private static final float DOUBLE_JUMP_VEL = -8f;
    private static final float FALL_KILL_Y = 800f;

    private final CharacterDef def;
    private final float spawnX;
    private final float spawnY;

    private float x;
    private float y;
    private float velY = 0;

    private boolean onGround = false;
    private boolean doubleJumpArmed = false;

    private float respawnX;
    private float respawnY;

    public UserChar(CharacterDef def, float spawnX, float spawnY) {
        this.def = def;
        this.spawnX = spawnX;
        this.spawnY = spawnY;

        this.x = spawnX;
        this.y = spawnY;

        this.respawnX = spawnX;
        this.respawnY = spawnY;
    }

    public void update(long window, float gravity, Level level) {

        float velX = readHorizontalInput(window);

        handleJumpInput(window);
        handleDoubleJumpInput(window);

        velY += gravity;
        x += velX;

        for (Rect o : level.obstacles) {
            if (bounds().intersects(o)) {
                if (velX > 0) x = o.x - def.hitX();
                else if (velX < 0) x = o.x + o.w;
            }
        }

        y += velY;
        onGround = false;

        tryLandOn(level.ground);
        for (Rect o : level.obstacles) tryLandOn(o);
        for (Rect p : level.platforms) tryLandOn(p);

        Rect kill = level.touchedKillzone(bounds());
        if (kill != null) {
            teleportToRespawn();
        }

        Rect cp = level.touchedCheckpoint(bounds());
        if (cp != null && !def.hardcore()) {
            respawnX = cp.x;
            respawnY = cp.y;
        }

        if (y > FALL_KILL_Y) {
            teleportToRespawn();
        }

        System.out.println(this.x + " " + this.y);
    }

    private float readHorizontalInput(long window) {
        boolean left = glfwGetKey(window, GLFW_KEY_A) == GLFW_PRESS
                || glfwGetKey(window, GLFW_KEY_LEFT) == GLFW_PRESS;

        boolean right = glfwGetKey(window, GLFW_KEY_D) == GLFW_PRESS
                || glfwGetKey(window, GLFW_KEY_RIGHT) == GLFW_PRESS;

        if (left) return -def.speed();
        if (right) return def.speed();
        return 0;
    }

    private void handleJumpInput(long window) {
        boolean jumpPressed =
                glfwGetKey(window, GLFW_KEY_Z) == GLFW_PRESS ||
                        glfwGetKey(window, GLFW_KEY_W) == GLFW_PRESS ||
                        glfwGetKey(window, GLFW_KEY_UP) == GLFW_PRESS;

        if (jumpPressed && onGround) {
            velY = def.jumpS();
            onGround = false;

            if (def.doubleJump()) {
                doubleJumpArmed = true;
            }
        }
    }

    private void handleDoubleJumpInput(long window) {
        if (!def.doubleJump()) return;

        boolean pressed =
                glfwGetKey(window, GLFW_KEY_X) == GLFW_PRESS ||
                        glfwGetKey(window, GLFW_KEY_LEFT_SHIFT) == GLFW_PRESS ||
                        glfwGetKey(window, GLFW_KEY_RIGHT_SHIFT) == GLFW_PRESS;

        if (pressed && doubleJumpArmed) {
            velY = DOUBLE_JUMP_VEL;
            doubleJumpArmed = false;
        }
    }

    private void tryLandOn(Rect surface) {
        if (velY < 0) return;

        float top = y;
        float bottom = y + def.hitY();
        float left = x;
        float right = x + def.hitX();

        if (bottom > surface.y && top < surface.y
                && right > surface.x && left < surface.x + surface.w) {

            y = surface.y - def.hitY();
            velY = 0;
            onGround = true;

            if (def.doubleJump()) {
                doubleJumpArmed = false;
            }
        }
    }

    private void teleportToRespawn() {
        velY = 0;
        onGround = false;
        doubleJumpArmed = false;

        if (!def.hardcore() && respawnX != spawnX) {
            x = respawnX - (def.hitX() - 20) / 2f;
            y = respawnY;
        } else {
            x = spawnX;
            y = spawnY;
        }
    }

    public Rect bounds() {
        return new Rect(x, y, def.hitX(), def.hitY());
    }

    public void render(float cameraX) {
        GL11.glColor3f(
                def.colour().getRed() / 255f,
                def.colour().getGreen() / 255f,
                def.colour().getBlue() / 255f
        );

        bounds().render(cameraX);
    }

    public float getX() { return x; }
    public float getY() { return y; }
}