package org.example;

import java.io.FileReader;
import java.util.*;
import java.awt.Color;

public class Jacread {

    public static List<CharacterDef> loadCharacters(String filename) throws Exception {
        List<CharacterDef> list = new ArrayList<>();

        try (FileReader reader = new FileReader(filename)) {

            int ch;
            StringBuilder buffer = new StringBuilder();

            boolean inString = false;
            boolean inNumber = false;
            boolean inColor = false;

            List<String> strings = new ArrayList<>();
            List<Integer> numbers = new ArrayList<>();

            boolean doubleJump = false;
            boolean hardcore = false;

            Color color = null;

            int idCounter = 1;

            while ((ch = reader.read()) != -1) {
                char c = (char) ch;

                if (c == '{') {
                    strings.clear();
                    numbers.clear();
                    buffer.setLength(0);
                    color = null;
                    doubleJump = false;
                    hardcore = false;
                    continue;
                }

                if (c == '}') {

                    String tag = buffer.toString().toLowerCase();
                    buffer.setLength(0);

                    if (tag.contains("doublejump")) doubleJump = true;
                    if (tag.contains("hardcore")) hardcore = true;

                    if (strings.size() < 3 || numbers.size() < 4 || color == null) {
                        continue;
                    }

                    CharacterDef def = new CharacterDef(
                            idCounter++,
                            strings.get(0),
                            strings.get(1),
                            strings.get(2),
                            numbers.get(0),
                            numbers.get(1),
                            numbers.get(2),
                            numbers.get(3),
                            color,
                            doubleJump,
                            hardcore
                    );

                    list.add(def);
                    continue;
                }

                if (c == '"') {
                    if (inString) {
                        strings.add(buffer.toString());
                        buffer.setLength(0);
                        inString = false;
                    } else {
                        inString = true;
                    }
                    continue;
                }

                if (c == '*') {
                    if (inNumber) {
                        try {
                            numbers.add(Integer.parseInt(buffer.toString()));
                        } catch (Exception ignored) {}
                        buffer.setLength(0);
                        inNumber = false;
                    } else {
                        inNumber = true;
                    }
                    continue;
                }

                if (c == '[') {
                    inColor = true;
                    continue;
                }

                if (c == ']') {
                    try {
                        String[] parts = buffer.toString().split("\\.");
                        int r = Integer.parseInt(parts[0]);
                        int g = Integer.parseInt(parts[1]);
                        int b = Integer.parseInt(parts[2]);
                        color = new Color(r, g, b);
                    } catch (Exception ignored) {}

                    buffer.setLength(0);
                    inColor = false;
                    continue;
                }

                if (!inString && !inNumber && !inColor) {
                    if (c != ',' && c != ' ' && c != ';') {
                        buffer.append(c);
                    }
                } else {
                    buffer.append(c);
                }
            }
        }

        return list;
    }
}