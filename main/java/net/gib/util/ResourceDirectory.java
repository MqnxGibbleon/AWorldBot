package net.gib.util;


import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ResourceDirectory {


    public static void saveJson(JSONObject content, String path, String filename) {
        File file = new File("src/main/resources/" + path + "/" + filename + ".json");
        try {
            var writer = new FileWriter(file);
            writer.write(content.toString(4));
            writer.flush();
            writer.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static JSONObject getJson(String path, String filename) {
        File file = new File("src/main/resources/" + path + "/" + filename + ".json");
        try {
            var text = Files.readString(Path.of(file.getPath()));
            return new JSONObject(text);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static File getFile(String path, String filename) {
        return new File("src/main/resources/" + path + "/" + filename + ".json");
    }

    public static File getPic(String path, String filename) {
        return new File("src/main/resources/" + path + "/" + filename + ".png");
    }



}
