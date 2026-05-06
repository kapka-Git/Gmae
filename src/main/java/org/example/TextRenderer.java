package org.example;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.stb.STBTTBakedChar;
import org.lwjgl.stb.STBTruetype;
import org.lwjgl.stb.STBTTAlignedQuad;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

public final class TextRenderer {

    private static final int BITMAP_W = 1024;
    private static final int BITMAP_H = 1024;
    private static final int FIRST_CHAR = 32;
    private static final int NUM_CHARS = 96;
    private static final float FONT_HEIGHT = 64f;

    private static STBTTBakedChar.Buffer charData;
    private static int textureId = -1;

    // Викликай один раз на старті, наприклад: TextRenderer.init("assets/font.ttf");
    public static void init(String ttfPath) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(ttfPath));
        ByteBuffer ttfBuffer = BufferUtils.createByteBuffer(bytes.length);
        ttfBuffer.put(bytes).flip();

        ByteBuffer bitmap = BufferUtils.createByteBuffer(BITMAP_W * BITMAP_H);
        charData = STBTTBakedChar.malloc(NUM_CHARS);

        STBTruetype.stbtt_BakeFontBitmap(ttfBuffer, FONT_HEIGHT, bitmap, BITMAP_W, BITMAP_H, FIRST_CHAR, charData);

        textureId = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_ALPHA, BITMAP_W, BITMAP_H,
                0, GL11.GL_ALPHA, GL11.GL_UNSIGNED_BYTE, bitmap);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
    }

    // Той самий виклик що і раніше: TextRenderer.draw("text", x, y, r, g, b, scale);
    public static void draw(String text, float x, float y, float r, float g, float b, float scale) {
        if (textureId == -1) throw new IllegalStateException("TextRenderer не ініціалізовано! Викличи init() спочатку.");

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
        GL11.glColor3f(r, g, b);

        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, 0);
        GL11.glScalef(scale, scale, 1);

        FloatBuffer xpos = BufferUtils.createFloatBuffer(1);
        FloatBuffer ypos = BufferUtils.createFloatBuffer(1);
        xpos.put(0, 0f);
        ypos.put(0, 0f);

        try (STBTTAlignedQuad q = STBTTAlignedQuad.malloc()) {
            GL11.glBegin(GL11.GL_QUADS);
            for (char c : text.toCharArray()) {
                if (c < FIRST_CHAR || c >= FIRST_CHAR + NUM_CHARS) continue;

                STBTruetype.stbtt_GetBakedQuad(charData, BITMAP_W, BITMAP_H, c - FIRST_CHAR, xpos, ypos, q, true);

                GL11.glTexCoord2f(q.s0(), q.t0()); GL11.glVertex2f(q.x0(), q.y0());
                GL11.glTexCoord2f(q.s1(), q.t0()); GL11.glVertex2f(q.x1(), q.y0());
                GL11.glTexCoord2f(q.s1(), q.t1()); GL11.glVertex2f(q.x1(), q.y1());
                GL11.glTexCoord2f(q.s0(), q.t1()); GL11.glVertex2f(q.x0(), q.y1());
            }
            GL11.glEnd();
        }

        GL11.glPopMatrix();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
    }

    private TextRenderer() {}
}