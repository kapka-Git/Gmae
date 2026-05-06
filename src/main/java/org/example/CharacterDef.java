package org.example;

import java.awt.Color;

public record CharacterDef(
        int id,
        String name,
        String desc,
        String special,
        int speed,
        int jumpS,
        int hitX,
        int hitY,
        java.awt.Color colour,
        boolean doubleJump,
        boolean hardcore
) {}