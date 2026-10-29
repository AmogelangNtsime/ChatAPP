/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatapp;

/**
 *
 * @author Student
 */

public class MessageStore {

    public static final String FLAG_SENT = "Sent";
    public static final String FLAG_STORED = "Stored";
    public static final String FLAG_DISREGARDED = "Disregarded";

    private static final int INITIAL_CAPACITY = 10;
    private static final String NO_MESSAGE = "No message found.";

    // Category arrays (message text only)
    private String[] sentMessages = new String[INITIAL_CAPACITY];
    private String[] disregardedMessages = new String[INITIAL_CAPACITY];
    private String[] storedMessages = new String[INITIAL_CAPACITY];
    private int sentCount = 0;
    private int disregardedCount = 0;
    private int storedCount = 0;

    // Parallel arrays (same index = same message)
    private String[] messageIDs = new String[INITIAL_CAPACITY];
    private String[] messageHashes = new String[INITIAL_CAPACITY];
    private String[] recipients = new String[INITIAL_CAPACITY];
    private String[] messageTexts = new String[INITIAL_CAPACITY];
    private String[] messageFlags = new String[INITIAL_CAPACITY];
    private int totalCount = 0;

    // ------------------------------------------------------------------
    // Populating the arrays
    // ------------------------------------------------------------------

    /** Adds a Message object using its own ID, hash, recipient and status. */
    public boolean addMessage(Message message) {
        return addMessage(message.getMessageID(), message.getMessageHash(),
                message.getRecipient(), message.getMessageText(),
                message.getMessageStatus());
    }

    /**
     * Adds one message to the parallel arrays and to the array that
     * matches its flag. Returns false if the flag is not recognised.
     */
    public boolean addMessage(String id, String hash, String recipient,
                              String text, String flag) {

        if (!isValidFlag(flag)) {
            return false;
        }

        messageIDs = append(messageIDs, totalCount, id);
        messageHashes = append(messageHashes, totalCount, hash);
        recipients = append(recipients, totalCount, recipient);
        messageTexts = append(messageTexts, totalCount, text);
        messageFlags = append(messageFlags, totalCount, flag);
        totalCount++;

        switch (flag) {
            case FLAG_SENT -> {
                sentMessages = append(sentMessages, sentCount, text);
                sentCount++;
            }
            case FLAG_STORED -> {
                storedMessages = append(storedMessages, storedCount, text);
                storedCount++;
            }
            default -> {
                disregardedMessages = append(
                        disregardedMessages, disregardedCount, text);
                disregardedCount++;
            }
        }

        return true;
    }

    // ------------------------------------------------------------------
    // 2a. Display the sender and recipient of all stored messages
    // ------------------------------------------------------------------

    public String displaySendersAndRecipients(String sender) {

        if (storedCount == 0) {
            return "There are no stored messages.";
        }

        StringBuilder output = new StringBuilder();

        for (int i = 0; i < totalCount; i++) {
            if (FLAG_STORED.equals(messageFlags[i])) {
                output.append("Sender: ").append(sender)
                        .append(" | Recipient: ").append(recipients[i])
                        .append("\n");
            }
        }

        return output.toString();
    }

    // ------------------------------------------------------------------
    // 2b. Display the longest stored message
    // ------------------------------------------------------------------

    /** Returns the text of the longest stored message. */
    public String findLongestStoredMessage() {

        String longest = null;

        for (int i = 0; i < storedCount; i++) {
            if (longest == null
                    || storedMessages[i].length() > longest.length()) {
                longest = storedMessages[i];
            }
        }

        return longest == null ? "There are no stored messages." : longest;
    }

    // ------------------------------------------------------------------
    // 2c. Search for a message ID
    // ------------------------------------------------------------------

    /** Returns the text of the message with this ID. */
    public String findMessageByID(String id) {

        int index = indexOf(messageIDs, id);

        return index == -1 ? NO_MESSAGE : messageTexts[index];
    }

    /** Returns the recipient and message for this ID, ready to display. */
    public String searchByMessageID(String id) {

        int index = indexOf(messageIDs, id);

        if (index == -1) {
            return NO_MESSAGE;
        }

        return "Recipient: " + recipients[index]
                + "\nMessage: " + messageTexts[index];
    }

    // ------------------------------------------------------------------
    // 2d. Search for all messages sent or stored for a recipient
    // ------------------------------------------------------------------

    /** Returns the text of every sent or stored message for a recipient. */
    public String[] findMessagesByRecipient(String recipient) {

        String[] found = new String[totalCount];
        int foundCount = 0;

        for (int i = 0; i < totalCount; i++) {

            boolean wanted = !FLAG_DISREGARDED.equals(messageFlags[i]);

            if (wanted && recipients[i].equals(recipient)) {
                found[foundCount++] = messageTexts[i];
            }
        }

        return copyOf(found, foundCount);
    }

    /** Same search as above, formatted for the console. */
    public String searchByRecipient(String recipient) {

        String[] found = findMessagesByRecipient(recipient);

        if (found.length == 0) {
            return "No messages found for " + recipient + ".";
        }

        StringBuilder output = new StringBuilder();

        for (String text : found) {
            output.append("Message: ").append(text).append("\n");
        }

        return output.toString();
    }

    // ------------------------------------------------------------------
    // 2e. Delete a message using the message hash
    // ------------------------------------------------------------------

    /**
     * Removes the message with this hash from every array it appears in
     * (the parallel arrays and its category array). Later elements shift
     * one place left, so the arrays stay aligned.
     */
    public String deleteByHash(String hash) {

        int index = indexOf(messageHashes, hash);

        if (index == -1) {
            return "No message with that hash was found.";
        }

        String text = messageTexts[index];
        String flag = messageFlags[index];

        // Position of this message inside its own category array
        int categoryIndex = 0;
        for (int i = 0; i < index; i++) {
            if (messageFlags[i].equals(flag)) {
                categoryIndex++;
            }
        }

        removeAt(messageIDs, totalCount, index);
        removeAt(messageHashes, totalCount, index);
        removeAt(recipients, totalCount, index);
        removeAt(messageTexts, totalCount, index);
        removeAt(messageFlags, totalCount, index);
        totalCount--;

        switch (flag) {
            case FLAG_SENT -> {
                removeAt(sentMessages, sentCount, categoryIndex);
                sentCount--;
            }
            case FLAG_STORED -> {
                removeAt(storedMessages, storedCount, categoryIndex);
                storedCount--;
            }
            default -> {
                removeAt(disregardedMessages, disregardedCount, categoryIndex);
                disregardedCount--;
            }
        }

        return "Message: \"" + withoutFinalFullStop(text)
                + "\" successfully deleted.";
    }

    // ------------------------------------------------------------------
    // 2f. Display a report of the full details of the messages
    // ------------------------------------------------------------------

    /** Report of every message that has the given flag. */
    public String getReport(String flag) {

        StringBuilder output = new StringBuilder();

        for (int i = 0; i < totalCount; i++) {
            if (messageFlags[i].equals(flag)) {
                output.append("Message ID: ").append(messageIDs[i]).append("\n")
                        .append("Message Hash: ").append(messageHashes[i]).append("\n")
                        .append("Recipient: ").append(recipients[i]).append("\n")
                        .append("Message: ").append(messageTexts[i]).append("\n\n");
            }
        }

        if (output.length() == 0) {
            return "There are no " + flag.toLowerCase() + " messages.";
        }

        return output.toString();
    }

    // ------------------------------------------------------------------
    // Getters used by the menu and the unit tests
    // ------------------------------------------------------------------

    public String[] getSentMessages() {
        return copyOf(sentMessages, sentCount);
    }

    public String[] getDisregardedMessages() {
        return copyOf(disregardedMessages, disregardedCount);
    }

    public String[] getStoredMessages() {
        return copyOf(storedMessages, storedCount);
    }

    public String[] getMessageHashes() {
        return copyOf(messageHashes, totalCount);
    }

    public String[] getMessageIDs() {
        return copyOf(messageIDs, totalCount);
    }

    public int getStoredCount() {
        return storedCount;
    }

    /** True if a message with this hash is in the hash array. */
    public boolean hasHash(String hash) {
        return indexOf(messageHashes, hash) != -1;
    }

    // ------------------------------------------------------------------
    // Private helpers
    // ------------------------------------------------------------------

    private boolean isValidFlag(String flag) {
        return FLAG_SENT.equals(flag)
                || FLAG_STORED.equals(flag)
                || FLAG_DISREGARDED.equals(flag);
    }

    /** Copies an array into a new array of the given length (plain loop). */
    private String[] copyOf(String[] source, int newLength) {

        String[] copy = new String[newLength];

        for (int i = 0; i < newLength && i < source.length; i++) {
            copy[i] = source[i];
        }

        return copy;
    }

    /** Puts a value in the next free slot, doubling the array if full. */
    private String[] append(String[] array, int count, String value) {

        String[] target = array;

        if (count == array.length) {
            target = copyOf(array, array.length * 2);
        }

        target[count] = value;
        return target;
    }

    /** Shifts everything after index one place left and clears the end. */
    private void removeAt(String[] array, int count, int index) {

        for (int i = index; i < count - 1; i++) {
            array[i] = array[i + 1];
        }

        array[count - 1] = null;
    }

    private int indexOf(String[] array, String value) {

        for (int i = 0; i < totalCount; i++) {
            if (array[i].equals(value)) {
                return i;
            }
        }

        return -1;
    }

    private String withoutFinalFullStop(String text) {
        return text.endsWith(".") ? text.substring(0, text.length() - 1) : text;
    }
}