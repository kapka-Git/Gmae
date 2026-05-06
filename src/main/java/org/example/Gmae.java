package org.example;

import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class Gmae {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final float GRAVITY = 0.3f;
    private static final float SPAWN_X = 380;
    private static final float SPAWN_Y = -20;
    private static final int ROBERT_ID = 4;

    private static List<CharacterDef> ALL = List.of();

    public void run() {


        try {
            ALL = Jacread.loadCharacters("src/main/resources/ja/chars.jac");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        long window = initWindow();
        try {
            TextRenderer.init("src/main/resources/fonts/ComicSansMS3.ttf");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Menu menu = new Menu(window);
        CharacterPicker picker = new CharacterPicker(window, ALL);


        while (!glfwWindowShouldClose(window)) {
            Menu.Choice choice = menu.run();
            if (choice == Menu.Choice.EXIT) break;

            CharacterDef pick = picker.run();
            if (pick == null) continue;

            playLevel(pick, window);
        }

        cleanup(window);
    }

    private long initWindow() {
        if (!glfwInit()) {
            throw new IllegalStateException("GLFW init failed");
        }

        long window = glfwCreateWindow(WIDTH, HEIGHT, "Gmae", 0, 0);
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
        return window;
    }

    private void playLevel(CharacterDef def, long window) {
        UserChar player = new UserChar(def, SPAWN_X, SPAWN_Y);
        Camera camera = new Camera(WIDTH / 2f, 0.05f);
        Level level = new Level();

        Keys keys = new Keys(window);
        boolean isRobert = def.id() == ROBERT_ID;

        while (!glfwWindowShouldClose(window)) {
            if (keys.pressed(GLFW_KEY_C)
                    || keys.pressed(GLFW_KEY_LEFT_CONTROL)
                    || keys.pressed(GLFW_KEY_RIGHT_CONTROL)
                    || keys.pressed(GLFW_KEY_ESCAPE)) return;

            level.update();
            player.update(window, GRAVITY, level);
            camera.follow(player.getX());

            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
            GL11.glLoadIdentity();
            level.render(camera.getX(), isRobert);
            player.render(camera.getX());

            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }

    private void cleanup(long window) {
        glfwDestroyWindow(window);
        glfwTerminate();
    }

    public static void main(String[] args) {
        new Gmae().run();
    }
}
