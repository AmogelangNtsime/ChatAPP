/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatapp;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 *
 * @author Student
 */
public class MessageFileHandler {
    
        /** Reads every message in the file into the store as a Stored message. */
    public static int loadStoredMessages(String filePath, MessageStore store) {

        Path path = Path.of(filePath);

        if (!Files.exists(path)) {
            return 0;
        }

        int loaded = 0;

        try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {

            JsonElement root = JsonParser.parseReader(reader);

            if (!root.isJsonArray()) {
                return 0;
            }

            for (JsonElement element : root.getAsJsonArray()) {

                JsonObject entry = element.getAsJsonObject();

                boolean added = store.addMessage(
                        entry.get("messageID").getAsString(),
                        entry.get("messageHash").getAsString(),
                        entry.get("recipient").getAsString(),
                        entry.get("messageText").getAsString(),
                        MessageStore.FLAG_STORED);

                if (added) {
                    loaded++;
                }
            }

        } catch (IOException | RuntimeException e) {
            System.out.println("Could not read " + filePath + ": " + e.getMessage());
        }

        return loaded;
    }

    /** Removes the message with this hash from the file. */
    public static void removeStoredMessage(String filePath, String hash) {

        Path path = Path.of(filePath);

        if (!Files.exists(path)) {
            return;
        }

        try {
            JsonArray remaining = new JsonArray();

            try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {

                for (JsonElement element : JsonParser.parseReader(reader).getAsJsonArray()) {

                    String entryHash = element.getAsJsonObject()
                            .get("messageHash").getAsString();

                    if (!entryHash.equals(hash)) {
                        remaining.add(element);
                    }
                }
            }

            try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
                new GsonBuilder().setPrettyPrinting().create().toJson(remaining, writer);
            }

        } catch (IOException | RuntimeException e) {
            System.out.println("Could not update " + filePath + ": " + e.getMessage());
        }
    }

    
}
