package org.example;

import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.stb.STBEasyFont;

public final class TextRenderer {
    private static final ByteBuffer BUFFER = BufferUtils.createByteBuffer(1 << 16);

    public static void draw(String text, float x, float y, float r, float g, float b, float scale) {
        BUFFER.clear();
        int quads = STBEasyFont.stb_easy_font_print(0, 0, text, null, BUFFER);

        GL11.glColor3f(r, g, b);
        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, 0);
        GL11.glScalef(scale, scale, 1);

        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
        GL11.glVertexPointer(2, GL11.GL_FLOAT, 16, BUFFER);
        GL11.glDrawArrays(GL11.GL_QUADS, 0, quads * 4);
        GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);

        GL11.glPopMatrix();
    }

    private TextRenderer() {}
}
