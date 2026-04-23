package org.example;

import java.awt.*;
import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

public class Gmae {

    private long window;

    // fiz
    private float velY = 0;
    private float gravity = 0.3f;
    private boolean onGround = false;
    private float cameraX = 0;

    // CHARACTER
    public static class UserChar {
        private String name;
        private int id;
        private int hitX;
        private int hitY;
        private float x;
        private float y;
        private int jumpS;
        private int speed;
        private Color color;

        public UserChar(String name, int id, int hitX, int hitY, float x, float y, int jumpS, int speed, Color color) {
            this.name = name;
            this.id = id;
            this.hitX = hitX;
            this.hitY = hitY;
            this.x = x;
            this.y = y;
            this.jumpS = jumpS;
            this.speed = speed;
            this.color = color;
        }
    }

    private UserChar ch0 = new UserChar(
            "Nmae",
            0,
            40,
            40,
            400,
            100,
            -10,
            5,
            new Color(0, 255, 0)
    );

    // rectangles
    // platforms

    // TOUCHABLE RECTANGLE
    static class Rect {
        float x, y, w, h;

        Rect(float x, float y, float w, float h) {
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
        }

        boolean intersects(Rect other) {
            return x < other.x + other.w &&
                    x + w > other.x &&
                    y < other.y + other.h &&
                    y + h > other.y;
        }
    }

    // PLATFORM
    static class Plat {
        float x, y, w, h;

        Plat(float x, float y, float w, float h) {
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
        }

        boolean intersects(Plat other) {
            return x < other.x + other.w &&
                    x + w > other.x &&
                    y + h > other.y;
        }
    }

    java.util.List<Rect> rects = new java.util.ArrayList<>();

    public void run() {
        init();
        loop();
        cleanup();
    }

    private void init() {
        if (!glfwInit()) {
            throw new IllegalStateException("GLFW init failed");
        }

        window = glfwCreateWindow(800, 600, "Gmae", 0, 0);

        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        glfwShowWindow(window);

        GL.createCapabilities();

        GL11.glViewport(0, 0, 800, 600);

        GL11.glClearColor(1f, 1f, 1f, 1f);

        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, 800, 600, 0, 1, -1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);

        rects.add(new Rect(200, 500, 2000, 100));
        rects.add(new Rect(700, 350, 100, 150));

    }

    private void loop() {
        while (!glfwWindowShouldClose(window)) {
            update();
            render();

            glfwSwapBuffers(window);
            glfwPollEvents();

            System.out.println("X = " + ch0.x + "   Y = " + ch0.y);
        }
    }

    private void update() {

        // gravity
        float velX = 0;

        // left right
        if (glfwGetKey(window, GLFW_KEY_A) == GLFW_PRESS) {
            velX = -ch0.speed;
        }

        if (glfwGetKey(window, GLFW_KEY_D) == GLFW_PRESS) {
            velX = ch0.speed;
        }

        // jump
        if (glfwGetKey(window, GLFW_KEY_W) == GLFW_PRESS && onGround) {
            velY = ch0.jumpS;
            onGround = false;
        }

        // collision
        Rect player = new Rect(ch0.x, ch0.y, ch0.hitX, ch0.hitY);

        onGround = false;

        // MOVE X
        ch0.x += velX;

        Rect playerX = new Rect(ch0.x, ch0.y, ch0.hitX, ch0.hitY);

        for (Rect r : rects) {
            if (playerX.intersects(r)) {
                if (velX > 0) {
                    ch0.x = r.x - ch0.hitX; // hit from left
                } else if (velX < 0) {
                    ch0.x = r.x + r.w; // hit from right
                }
            }
        }

        // MOVE Y

        velY += gravity;

        ch0.y += velY;

        Rect playerY = new Rect(ch0.x, ch0.y, ch0.hitX, ch0.hitY);

        onGround = false;

        for (Rect r : rects) {
            if (playerY.intersects(r)) {

                if (velY > 0) {
                    // falling → land on top
                    ch0.y = r.y - ch0.hitY;
                    velY = 0;
                    onGround = true;

                } else if (velY < 0) {
                    // going up → hit ceiling
                    ch0.y = r.y + r.h;
                    velY = 0;
                }
            }
        }

        if (ch0.y > 650) {
            ch0.x = 400;
            ch0.y = -50;
        }

        // camera follow
        cameraX += ((ch0.x - 400) - cameraX) * 0.05f;
    }

    private void drawRect(float x, float y, float w, float h) {
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(x, y);
        GL11.glVertex2f(x + w, y);
        GL11.glVertex2f(x + w, y + h);
        GL11.glVertex2f(x, y + h);
        GL11.glEnd();
    }

    private void render() {

        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        GL11.glLoadIdentity();

        // player
        GL11.glColor3f(
                ch0.color.getRed() / 255f,
                ch0.color.getGreen() / 255f,
                ch0.color.getBlue() / 255f
        );

        drawRect(ch0.x - cameraX, ch0.y, ch0.hitX, ch0.hitY);

        for (Rect r : rects) {
            drawRect(r.x - cameraX, r.y, r.w, r.h);
        }
    }

    private void cleanup() {
        glfwDestroyWindow(window);
        glfwTerminate();
    }

    public static void main(String[] args) {
        new Gmae().run();
    }
}