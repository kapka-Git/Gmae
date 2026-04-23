package org.example;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

public class Gmae {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final float GRAVITY = 0.3f;

    private long window;
    private UserChar player;
    private Camera camera;
    private final List<Rect> platforms = new ArrayList<>();

    public void run() {
        init();
        loop();
        cleanup();
    }

    private void init() {
        if (!glfwInit()) {
            throw new IllegalStateException("GLFW init failed");
        }

        window = glfwCreateWindow(WIDTH, HEIGHT, "Gmae", 0, 0);
        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        glfwShowWindow(window);

        GL.createCapabilities();
        GL11.glViewport(0, 0, WIDTH, HEIGHT);
        GL11.glClearColor(1f, 1f, 1f, 1f);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, WIDTH, HEIGHT, 0, 1, -1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);

        player = new UserChar("Nmae", 0, 40, 40, 400, 100, -10, 5, new Color(0, 255, 0));
        camera = new Camera(WIDTH / 2f, 0.05f);

        platforms.add(new Rect(200, 500, 2000, 100));
        platforms.add(new Rect(700, 350, 100, 150));
    }

    private void loop() {
        while (!glfwWindowShouldClose(window)) {
            player.update(window, GRAVITY, platforms);
            camera.follow(player.getX());

            render();

            glfwSwapBuffers(window);
            glfwPollEvents();

            System.out.println("X = " + player.getX() + "   Y = " + player.getY());
        }
    }

    private void render() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        GL11.glLoadIdentity();

        player.render(camera.getX());
        for (Rect r : platforms) {
            r.render(camera.getX());
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
