/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatapp;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.io.FileWriter;
import java.io.IOException;

public class Message {

    private String messageID;
    private int messageNumber;
    private String recipient;
    private String messageText;
    private String messageHash;
    private String messageStatus;

    private static int totalMessages = 0;
    private static List<Message> sentMessages = new ArrayList<>();

    // Constructor
    public Message(int messageNumber, String recipient,
                   String messageText) {

        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.messageText = messageText;

        this.messageID = generateMessageID();
        this.messageHash = createMessageHash();
        this.messageStatus = "Pending";
    }

    // Generate a random 10-digit message ID
    private String generateMessageID() {

        Random random = new Random();

        long number = 1_000_000_000L
                + (long) (random.nextDouble()
                * 9_000_000_000L);

        return String.valueOf(number);
    }

    // Check that the message ID has 10 digits
    public boolean checkMessageID() {
        return messageID.matches("\\d{10}");
    }

    // Validate the recipient cellphone number
    public String checkRecipientCell() {

        if (recipient != null
                && recipient.matches("^\\+27\\d{9}$")) {

            return "Cell phone number successfully captured.";
        }

        return "Cell phone number is incorrectly formatted or does "
                + "not contain an international code. Please correct "
                + "the number and try again.";
    }

    // Check the message length
    public String checkMessageLength() {

        if (messageText.length() <= 250) {
            return "Message ready to send.";
        }

        int excess = messageText.length() - 250;

        return "Message exceeds 250 characters by "
                + excess + "; please reduce the size.";
    }

    // Create the message hash 
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
                + ":" + messageNumber
                + ":" + firstWord + lastWord).toUpperCase();
    }

    // Allow the user to choose what to do with the message
    public String SentMessage() {

        java.util.Scanner scanner =
                new java.util.Scanner(System.in);

        System.out.println("1. Send Message");
        System.out.println("2. Disregard Message");
        System.out.println("3. Store Message to send later");
        System.out.print("Choose an option: ");

        String choice = scanner.nextLine();

        return SentMessage(choice);
    }

    // Process the selected message option
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
                sentMessages.add(this);
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

    // Display all messages sent during this application run
    public String printMessages() {

        StringBuilder output = new StringBuilder();

        for (Message message : sentMessages) {

            output.append("Message ID: ")
                    .append(message.messageID).append("\n");

            output.append("Message Hash: ")
                    .append(message.messageHash).append("\n");

            output.append("Recipient: ")
                    .append(message.recipient).append("\n");

            output.append("Message: ")
                    .append(message.messageText).append("\n\n");
        }

        return output.toString();
    }

    // Return the total number of messages sent
    public int returnTotalMessages() {
        return totalMessages;
    }

    // Stores a message in a JSON file
    
public void storeMessage() {

    java.io.File file = new java.io.File("messages.json");
    String existingContent = "";

    try {
        if (file.exists() && file.length() > 0) {
            java.util.Scanner reader =
                    new java.util.Scanner(file, "UTF-8");

            reader.useDelimiter("\\Z");

            if (reader.hasNext()) {
                existingContent = reader.next();
            }

            reader.close();
        }

        existingContent = existingContent.trim();

        String newMessage = "{"
                + "\"messageID\":\"" + escapeJSON(messageID) + "\","
                + "\"messageNumber\":" + messageNumber + ","
                + "\"recipient\":\"" + escapeJSON(recipient) + "\","
                + "\"messageText\":\"" + escapeJSON(messageText) + "\","
                + "\"messageHash\":\"" + escapeJSON(messageHash) + "\","
                + "\"messageStatus\":\"Stored\""
                + "}";

        String updatedContent;

        if (existingContent.isEmpty()) {
            updatedContent = "[" + newMessage + "]";
        } else {
            if (!existingContent.startsWith("[")
                    || !existingContent.endsWith("]")) {
                System.out.println(
                        "The existing messages.json file is not a valid JSON array."
                );
                return;
            }

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
        }

        try (FileWriter writer = new FileWriter(file, false)) {
            writer.write(updatedContent);
        }

        System.out.println("Message successfully stored.");

    } catch (IOException e) {
        System.out.println(
                "Error storing message: " + e.getMessage()
        );
    }
}
    private String escapeJSON(String text) {

        return text.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    // Getters
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