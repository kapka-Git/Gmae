package org.example;

import static org.lwjgl.glfw.GLFW.*;

public class Keys {
    private final long window;
    private final boolean[] prev = new boolean[GLFW_KEY_LAST + 1];

    public Keys(long window) {
        this.window = window;
        glfwPollEvents();
        for (int k = 32; k <= GLFW_KEY_LAST; k++) {
            prev[k] = glfwGetKey(window, k) == GLFW_PRESS;
        }
    }

    public boolean pressed(int key) {
        boolean now = glfwGetKey(window, key) == GLFW_PRESS;
        boolean was = prev[key];
        prev[key] = now;
        return now && !was;
    }
}
