package org.example;

import java.awt.Color;
import java.util.List;

public final class Characters {
    public static final CharacterDef KIRK = new CharacterDef(
            1, "Kirk Hammet", "Wah?",
            "Balanced character",
            5, -10, 40, 40,
            new Color(0, 210, 0)
    );
    public static final CharacterDef JAM = new CharacterDef(
            2, "Jam Hetfield",
            "He's like James Hetfield but the actual jam.",
            "Big and slow but can double jump",
            3, -8, 48, 48,
            new Color(220, 0, 255)
    );
    public static final CharacterDef LARS = new CharacterDef(
            3, "Lars Ulrich", "Napster bad.",
            "Fast and small. Good for speedrunners.",
            7, -8, 32, 32,
            new Color(0, 0, 210)
    );
    public static final CharacterDef ROBERT = new CharacterDef(
            4, "Robert", "Hardcore mode is on.",
            "One life, no checkpoints. Good luck.",
            6, -9, 44, 44,
            new Color(210, 0, 0)
    );

    public static final List<CharacterDef> ALL = List.of(KIRK, JAM, LARS, ROBERT);

    private Characters() {}
}
