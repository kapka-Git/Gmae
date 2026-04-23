package org.example;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

public class Main {

    public static void main(String[] args) {
        // Setup error callback
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW
        if (!GLFW.glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW :(");
        }

        // Create window
        long window = GLFW.glfwCreateWindow(800, 600, "gmae", 0, 0);
        if (window == 0) {
            throw new RuntimeException("Failed to create window :(");
        }

        // Make context current
        GLFW.glfwMakeContextCurrent(window);

        // Enable v-sync
        GLFW.glfwSwapInterval(1);

        // Show window
        GLFW.glfwShowWindow(window);

        // Create OpenGL capabilities
        GL.createCapabilities();

        // Game loop
        while (!GLFW.glfwWindowShouldClose(window)) {

            // Clear screen
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

            // Swap buffers
            GLFW.glfwSwapBuffers(window);

            // Poll events (keyboard, mouse, etc.)
            GLFW.glfwPollEvents();
        }

        // Cleanup
        GLFW.glfwDestroyWindow(window);
        GLFW.glfwTerminate();
    }
}