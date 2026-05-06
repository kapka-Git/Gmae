package org.example;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

public class Level {
    private static final int MKZ_MIN_X = 1400;
    private static final int MKZ_MAX_X = 1860;

    private static final Color GROUND_GREEN = new Color(0, 175, 0);
    private static final Color GREEN = new Color(0, 255, 0);
    private static final Color YELLOW = new Color(255, 255, 0);
    private static final Color RED = new Color(255, 0, 0);
    private static final Color BLUE = new Color(0, 0, 255);

    public final Rect ground;
    public final List<Rect> obstacles;
    public final List<Rect> platforms;
    public final List<Rect> killzones;
    public final List<Rect> checkpoints;
    public final List<WorldText> signs;

    private final Rect movingKz;
    private int movingKzDir = 0;

    public Level() {
        ground = new Rect(0, 500, 4000, 100);

        obstacles = List.of(
                new Rect(600, 360, 100, 140),
                new Rect(1100, 260, 100, 240),
                new Rect(2100, 260, 100, 240),
                new Rect(2600, 380, 500, 120),
                new Rect(3200, 240, 50, 260)
        );

        platforms = List.of(
                new Rect(560, 440, 40, 20),
                new Rect(700, 440, 40, 20),
                new Rect(1200, 440, 40, 20),
                new Rect(1200, 370, 40, 20),
                new Rect(1200, 300, 40, 20),
                new Rect(800, 300, 160, 20),
                new Rect(1400, 360, 500, 20),
                new Rect(2090, 300, 10, 20),
                new Rect(2200, 360, 40, 20),
                new Rect(2200, 420, 40, 20),
                new Rect(2560, 440, 40, 20),
                new Rect(3160, 300, 40, 20)
        );

        killzones = new ArrayList<>(List.of(
                new Rect(200, 460, 20, 40),
                new Rect(-800, 510, 800, 20),
                new Rect(1400, 490, 700, 20),
                new Rect(2300, 480, 200, 20),
                new Rect(2800, 360, 100, 20),
                new Rect(3120, 480, 60, 20),
                new Rect(3700, 480, 60, 20),
                new Rect(1400, 340, 40, 20)
        ));
        movingKz = killzones.get(killzones.size() - 1);

        checkpoints = List.of(
                new Rect(2140, 180, 20, 60),
                new Rect(3500, 420, 20, 60)
        );

        signs = List.of(
                new WorldText("Wellcome to the \"CB x BF\"", 0, 30, 0f, 0f, 0f, 0.6f, true),
                new WorldText("v pre alpha 1.1", 5, 45, 0f, 0.6f, 0f, 0.3f, true),
                new WorldText("This is a\n check point", 2080, 100, 0f, 0f, 0f, 0.3f, false)
        );
    }

    public void update() {
        if (movingKzDir == 0) {
            if (movingKz.x < MKZ_MAX_X) movingKz.x += 1;
            else movingKzDir = 1;
        } else {
            if (movingKz.x > MKZ_MIN_X) movingKz.x -= 1;
            else movingKzDir = 0;
        }
    }

    public void render(float cameraX, boolean isRobert) {
        setColor(GROUND_GREEN);
        ground.render(cameraX);

        setColor(GREEN);
        for (Rect r : obstacles) r.render(cameraX);

        setColor(YELLOW);
        for (Rect r : platforms) r.render(cameraX);

        setColor(RED);
        for (Rect r : killzones) r.render(cameraX);

        if (!isRobert) {
            setColor(BLUE);
            for (Rect r : checkpoints) r.render(cameraX);
        }

        for (WorldText t : signs) {
            if (isRobert && !t.showForRobert()) continue;
            TextRenderer.draw(t.text(), t.x() - cameraX, t.y(), t.r(), t.g(), t.b(), t.scale());
        }
    }

    public Rect touchedKillzone(Rect r) {
        for (Rect k : killzones) {
            if (r.intersects(k)) return k;
        }
        return null;
    }

    public Rect touchedCheckpoint(Rect r) {
        for (Rect c : checkpoints) {
            if (r.intersects(c)) return c;
        }
        return null;
    }

    private static void setColor(Color c) {
        GL11.glColor3f(c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f);
    }
}
