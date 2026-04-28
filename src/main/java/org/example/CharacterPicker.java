package org.example;

import java.util.List;

import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.opengl.GL11;

public class CharacterPicker {
    private static final int SCREEN_W = 800;

    private final long window;
    private final List<CharacterDef> chars;

    public CharacterPicker(long window, List<CharacterDef> chars) {
        this.window = window;
        this.chars = chars;
    }

    public CharacterDef run() {
        Keys keys = new Keys(window);
        int selected = 0;

        while (!glfwWindowShouldClose(window)) {
            if (keys.pressed(GLFW_KEY_RIGHT) || keys.pressed(GLFW_KEY_D))
                selected = (selected + 1) % chars.size();
            if (keys.pressed(GLFW_KEY_LEFT) || keys.pressed(GLFW_KEY_A))
                selected = (selected - 1 + chars.size()) % chars.size();
            if (keys.pressed(GLFW_KEY_Z) || keys.pressed(GLFW_KEY_ENTER))
                return chars.get(selected);
            if (keys.pressed(GLFW_KEY_X) || keys.pressed(GLFW_KEY_ESCAPE))
                return null;

            render(selected);
            glfwSwapBuffers(window);
            glfwPollEvents();
        }
        return null;
    }

    private void render(int selected) {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        GL11.glLoadIdentity();

        TextRenderer.draw("PICK YOUR CHARACTER", 190, 50, 0, 0, 0, 3f);

        float slotW = 160;
        float totalW = chars.size() * slotW;
        float startX = (SCREEN_W - totalW) / 2f;
        float slotY = 180;
        float slotH = 160;

        for (int i = 0; i < chars.size(); i++) {
            CharacterDef c = chars.get(i);
            float slotX = startX + i * slotW;
            float slotCenterX = slotX + slotW / 2f;

            if (i == selected) {
                GL11.glColor3f(0.95f, 0.85f, 0.1f);
                new Rect(slotX + 5, slotY, slotW - 10, slotH).render(0);
            }

            GL11.glColor3f(
                    c.colour().getRed() / 255f,
                    c.colour().getGreen() / 255f,
                    c.colour().getBlue() / 255f
            );
            float charX = slotCenterX - c.hitX() / 2f;
            float charY = slotY + (slotH - c.hitY()) / 2f;
            new Rect(charX, charY, c.hitX(), c.hitY()).render(0);
        }

        CharacterDef sel = chars.get(selected);
        TextRenderer.draw(sel.name(), 100, 380, 0, 0, 0, 2.5f);
        TextRenderer.draw("SPEED " + sel.speed() + "   JUMP " + (-sel.jumpS()), 100, 420, 0, 0, 0, 2f);
        TextRenderer.draw(sel.special(), 100, 460, 0.2f, 0.2f, 0.2f, 1.6f);

        TextRenderer.draw("LEFT/RIGHT select   Z confirm   X back", 170, 560, 0.4f, 0.4f, 0.4f, 1.5f);
    }
}
