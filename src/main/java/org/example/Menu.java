package org.example;

import java.util.List;

import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.opengl.GL11;

public class Menu {
    public enum Choice { NEW_GAME, EXIT }

    private static final List<String> OPTIONS = List.of("NEW GAME", "EXIT");
    private static final Choice[] CHOICES = { Choice.NEW_GAME, Choice.EXIT };

    private final long window;

    public Menu(long window) {
        this.window = window;
    }

    public Choice run() {
        Keys keys = new Keys(window);
        int selected = 0;

        while (!glfwWindowShouldClose(window)) {
            if (keys.pressed(GLFW_KEY_DOWN) || keys.pressed(GLFW_KEY_S))
                selected = (selected + 1) % OPTIONS.size();
            if (keys.pressed(GLFW_KEY_UP) || keys.pressed(GLFW_KEY_W))
                selected = (selected - 1 + OPTIONS.size()) % OPTIONS.size();
            if (keys.pressed(GLFW_KEY_Z) || keys.pressed(GLFW_KEY_ENTER))
                return CHOICES[selected];
            if (keys.pressed(GLFW_KEY_X) || keys.pressed(GLFW_KEY_ESCAPE))
                return Choice.EXIT;

            render(selected);
            glfwSwapBuffers(window);
            glfwPollEvents();
        }
        return Choice.EXIT;
    }

    private void render(int selected) {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        GL11.glLoadIdentity();

        TextRenderer.draw("GMAE", 300, 120, 0, 0, 0, 1.5f);

        for (int i = 0; i < OPTIONS.size(); i++) {
            float x = 280;
            float y = 280 + i * 80;
            float w = 240;
            float h = 60;

            if (i == selected) {
                GL11.glColor3f(0, 0, 0);
                new Rect(x, y, w, h).render(0);
                TextRenderer.draw(OPTIONS.get(i), x + 16, y + 43, 1, 1, 1, 0.8f);
            } else {
                GL11.glColor3f(0.85f, 0.85f, 0.85f);
                new Rect(x, y, w, h).render(0);
                TextRenderer.draw(OPTIONS.get(i), x + 16, y + 43, 0, 0, 0, 0.8f);
            }
        }

        TextRenderer.draw("ARROWS select   Z confirm   X back/exit", 260, 580, 0.4f, 0.4f, 0.4f, 0.3f);
    }
}
