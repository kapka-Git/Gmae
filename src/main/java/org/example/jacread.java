package org.example;

import java.io.FileReader;

class Jacread {
    static void jacreader(String filename) throws Exception {
        try (FileReader reader = new FileReader(filename)) {
            int ch;
            while ((ch = reader.read()) != -1) {
                System.out.print((char) ch);
            }
        }
    }
}