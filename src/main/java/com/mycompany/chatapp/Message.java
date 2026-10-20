/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatapp;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class Message {

    private String messageID;
    private int messageNumber;
    private String recipient;
    private String messageText;
    private String messageHash;
    private String messageStatus;

    private Message next;

    private static Message firstSentMessage = null;
    private static int totalMessages = 0;

    public Message(int messageNumber, String recipient,
                   String messageText) {

        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.messageText = messageText;

        this.messageID = generateMessageID();
        this.messageHash = createMessageHash();
        this.messageStatus = "Pending";
        this.next = null;
    }

    private String generateMessageID() {
        Random random = new Random();

        long number = 1_000_000_000L
                + (long) (random.nextDouble()
                * 9_000_000_000L);

        return String.valueOf(number);
    }

    public boolean checkMessageID() {
        return messageID.matches("\\d{10}");
    }

    public String checkRecipientCell() {

        if (recipient != null
                && recipient.matches("^\\+27\\d{9}$")) {

            return "Cell phone number successfully captured.";
        }

        return "Cell phone number is incorrectly formatted or "
                + "does not contain an international code. "
                + "Please correct the number and try again.";
    }

    public String checkMessageLength() {

        if (messageText.length() <= 250) {
            return "Message ready to send.";
        }

        int excess = messageText.length() - 250;

        return "Message exceeds 250 characters by "
                + excess + ", please reduce the size.";
    }

    public String createMessageHash() {

        String text = messageText.trim();
        String firstWord = "";
        String lastWord = "";

        if (!text.isEmpty()) {

            int firstSpace = text.indexOf(' ');

            if (firstSpace == -1) {
                firstWord = text;
                lastWord = text;
            } else {
                firstWord = text.substring(0, firstSpace);

                int lastSpace = text.lastIndexOf(' ');
                lastWord = text.substring(lastSpace + 1);
            }
        }

        return (messageID.substring(0, 2)
                + ":" + messageNumber + ":"
                + firstWord + lastWord).toUpperCase();
    }

    public String SentMessage() {

        Scanner scanner = new Scanner(System.in);

        System.out.println("1. Send Message");
        System.out.println("2. Disregard Message");
        System.out.println("3. Store Message");

        System.out.print("Choose an option: ");

        String choice = scanner.nextLine();

        return SentMessage(choice);
    }

    public String SentMessage(String choice) {

        switch (choice) {

            case "1":

                if (!checkMessageID()
                        || !checkRecipientCell().equals(
                        "Cell phone number successfully captured.")
                        || !checkMessageLength().equals(
                        "Message ready to send.")) {

                    return "Message could not be sent.";
                }

                messageStatus = "Sent";

                this.next = null;

                if (firstSentMessage == null) {
                    firstSentMessage = this;
                } else {
                    Message current = firstSentMessage;

                    while (current.next != null) {
                        current = current.next;
                    }

                    current.next = this;
                }

                totalMessages++;

                return "Message successfully sent.";

            case "2":

                messageStatus = "Disregarded";

                return "Press 0 to delete the message.";

            case "3":

                storeMessage();
                messageStatus = "Stored";

                return "Message successfully stored.";

            default:

                return "Invalid option.";
        }
    }

    public String printMessages() {

        StringBuilder output = new StringBuilder();
        Message current = firstSentMessage;

        while (current != null) {

            output.append("Message ID: ")
                    .append(current.messageID).append("\n");

            output.append("Message Hash: ")
                    .append(current.messageHash).append("\n");

            output.append("Recipient: ")
                    .append(current.recipient).append("\n");

            output.append("Message: ")
                    .append(current.messageText).append("\n\n");

            current = current.next;
        }

        return output.toString();
    }

    public int returnTotalMessages() {
        return totalMessages;
    }

    public void storeMessage() {

        File file = new File("messages.json");
        String existingContent = "";

        try {

            if (file.exists() && file.length() > 0) {

                try (Scanner reader =
                        new Scanner(file, "UTF-8")) {

                    reader.useDelimiter("\\Z");

                    if (reader.hasNext()) {
                        existingContent = reader.next();
                    }
                }
            }

            String newMessage = "{"
                    + "\"messageID\":\""
                    + escapeJSON(messageID) + "\","
                    + "\"messageNumber\":" + messageNumber + ","
                    + "\"recipient\":\""
                    + escapeJSON(recipient) + "\","
                    + "\"messageText\":\""
                    + escapeJSON(messageText) + "\","
                    + "\"messageHash\":\""
                    + escapeJSON(messageHash) + "\","
                    + "\"messageStatus\":\"Stored\""
                    + "}";

            existingContent = existingContent.trim();

            String updatedContent;

            if (existingContent.isEmpty()) {

                updatedContent = "[" + newMessage + "]";

            } else if (existingContent.startsWith("[")
                    && existingContent.endsWith("]")) {

                String inside = existingContent.substring(
                        1, existingContent.length() - 1
                ).trim();

                if (inside.isEmpty()) {
                    updatedContent = "[" + newMessage + "]";
                } else {
                    updatedContent = "["
                            + inside + ","
                            + newMessage + "]";
                }

            } else {

                System.out.println(
                        "The existing JSON file is not a JSON array."
                );

                return;
            }

            try (FileWriter writer =
                    new FileWriter(file, false)) {

                writer.write(updatedContent);
            }

        } catch (IOException e) {

            System.out.println(
                    "Error storing message: " + e.getMessage()
            );
        }
    }

    private String escapeJSON(String text) {

        if (text == null) {
            return "";
        }

        return text.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    public String getMessageID() {
        return messageID;
    }

    public int getMessageNumber() {
        return messageNumber;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getMessageText() {
        return messageText;
    }

    public String getMessageHash() {
        return messageHash;
    }

    public String getMessageStatus() {
        return messageStatus;
    }
}