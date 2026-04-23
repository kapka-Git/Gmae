package org.example;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

public class Gmae {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final float GRAVITY = 0.3f;
    private static final float SPAWN_X = 400;
    private static final float SPAWN_Y = 100;

    private long window;

    public void run() {
        initWindow();

        Menu menu = new Menu(window);
        CharacterPicker picker = new CharacterPicker(window, Characters.ALL);

        while (!glfwWindowShouldClose(window)) {
            Menu.Choice choice = menu.run();
            if (choice == Menu.Choice.EXIT) break;

            CharacterDef pick = picker.run();
            if (pick == null) continue;

            playLevel(pick);
        }

        cleanup();
    }

    private void initWindow() {
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
    }

    private void playLevel(CharacterDef def) {
        UserChar player = new UserChar(def, SPAWN_X, SPAWN_Y);
        Camera camera = new Camera(WIDTH / 2f, 0.05f);
        List<Rect> platforms = new ArrayList<>();
        platforms.add(new Rect(200, 500, 2000, 100));
        platforms.add(new Rect(700, 350, 100, 150));

        Keys keys = new Keys(window);

        while (!glfwWindowShouldClose(window)) {
            if (keys.pressed(GLFW_KEY_ESCAPE)) return;

            player.update(window, GRAVITY, platforms);
            camera.follow(player.getX());

            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
            GL11.glLoadIdentity();
            player.render(camera.getX());
            GL11.glColor3f(0f, 175f / 255f, 0f);
            for (Rect r : platforms) {
                r.render(camera.getX());
            }

            glfwSwapBuffers(window);
            glfwPollEvents();
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
