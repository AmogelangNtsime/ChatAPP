/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.chatapp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;


public class MessageStoreTest {

    private static final String MESSAGE_1 = "Did you get the cake?";
    private static final String MESSAGE_2 =
            "Where are you? You are late! I have asked you to be on time.";
    private static final String MESSAGE_3 = "Yohoooo, I am at your gate.";
    private static final String MESSAGE_4 = "It is dinner time!";
    private static final String MESSAGE_5 = "Ok, I am leaving without you.";
    private static final String RECIPIENT_2 = "+27838884567";

    private MessageStore store;

    private void addTestMessage(String id, int number, String recipient,
                                String text, String flag) {
        Message message = new Message(id, number, recipient, text);
        message.setMessageStatus(flag);
        store.addMessage(message);
    }

    @BeforeEach
    public void setUp() {
        store = new MessageStore();
        addTestMessage("1000000001", 1, "+27834557896", MESSAGE_1, MessageStore.FLAG_SENT);
        addTestMessage("2000000002", 2, RECIPIENT_2, MESSAGE_2, MessageStore.FLAG_STORED);
        addTestMessage("3000000003", 3, "+27834484567", MESSAGE_3, MessageStore.FLAG_DISREGARDED);
        addTestMessage("0838884567", 4, "Developer", MESSAGE_4, MessageStore.FLAG_SENT);
        addTestMessage("5000000005", 5, RECIPIENT_2, MESSAGE_5, MessageStore.FLAG_STORED);
    }

  

    @Test
    public void testSentMessagesArrayCorrectlyPopulated() {
        assertArrayEquals(new String[]{MESSAGE_1, MESSAGE_4}, store.getSentMessages());
    }

    @Test
    public void testDisregardedMessagesArrayCorrectlyPopulated() {
        assertArrayEquals(new String[]{MESSAGE_3}, store.getDisregardedMessages());
    }

    @Test
    public void testStoredMessagesArrayCorrectlyPopulated() {
        assertArrayEquals(new String[]{MESSAGE_2, MESSAGE_5}, store.getStoredMessages());
    }

    @Test
    public void testMessageHashAndIDArraysCorrectlyPopulated() {
        assertArrayEquals(
                new String[]{"10:1:DIDCAKE?", "20:2:WHERETIME.",
                    "30:3:YOHOOOO,GATE.", "08:4:ITTIME!", "50:5:OK,YOU."},
                store.getMessageHashes());
        assertArrayEquals(
                new String[]{"1000000001", "2000000002", "3000000003",
                    "0838884567", "5000000005"},
                store.getMessageIDs());
    }

    @Test
    public void testArraysGrowBeyondInitialCapacity() {
        MessageStore bigStore = new MessageStore();
        for (int i = 0; i < 25; i++) {
            bigStore.addMessage("ID" + i, "HASH" + i, "+27830000000",
                    "Message " + i, MessageStore.FLAG_SENT);
        }
        assertEquals(25, bigStore.getSentMessages().length);
        assertEquals("Message 24", bigStore.getSentMessages()[24]);
    }

    @Test
    public void testUnknownFlagIsRejected() {
        assertFalse(store.addMessage("1", "H", "+27830000000", "Hi", "Unknown"));
    }

   

    @Test
    public void testDisplayLongestMessage() {
        assertEquals(MESSAGE_2, store.findLongestStoredMessage());
    }

    @Test
    public void testLongestMessageWhenNothingStored() {
        assertEquals("There are no stored messages.",
                new MessageStore().findLongestStoredMessage());
    }

   

    @Test
    public void testSearchForMessageID() {
        assertEquals(MESSAGE_4, store.findMessageByID("0838884567"));
    }

    @Test
    public void testSearchForMessageIDDisplaysRecipientAndMessage() {
        assertEquals("Recipient: Developer\nMessage: " + MESSAGE_4,
                store.searchByMessageID("0838884567"));
    }

    @Test
    public void testSearchForMessageIDNotFound() {
        assertEquals("No message found.", store.findMessageByID("9999999999"));
    }

  
    @Test
    public void testSearchAllMessagesForRecipient() {
        assertArrayEquals(new String[]{MESSAGE_2, MESSAGE_5},
                store.findMessagesByRecipient(RECIPIENT_2));
    }

    @Test
    public void testSearchRecipientIgnoresDisregardedMessages() {
        assertArrayEquals(new String[]{},
                store.findMessagesByRecipient("+27834484567"));
    }

    @Test
    public void testSearchRecipientDisplay() {
        assertEquals("Message: " + MESSAGE_2 + "\nMessage: " + MESSAGE_5 + "\n",
                store.searchByRecipient(RECIPIENT_2));
    }

 

    @Test
    public void testDeleteMessageUsingMessageHash() {
        String hash = new Message("2000000002", 2, RECIPIENT_2, MESSAGE_2)
                .getMessageHash();

        assertEquals("Message: \"Where are you? You are late! I have asked you "
                + "to be on time\" successfully deleted.",
                store.deleteByHash(hash));
    }

    @Test
    public void testDeleteKeepsAllArraysAligned() {
        store.deleteByHash("20:2:WHERETIME.");

        assertArrayEquals(new String[]{MESSAGE_5}, store.getStoredMessages());
        assertEquals(4, store.getMessageHashes().length);
        assertEquals("No message found.", store.findMessageByID("2000000002"));
        assertEquals(MESSAGE_5, store.findMessageByID("5000000005"));
        assertEquals(MESSAGE_4, store.findMessageByID("0838884567"));
    }

    @Test
    public void testDeleteHashNotFound() {
        assertEquals("No message with that hash was found.",
                store.deleteByHash("00:0:NOPE"));
    }

 

    @Test
    public void testDisplaySentReport() {
        String expected
                = "Message ID: 1000000001\n"
                + "Message Hash: 10:1:DIDCAKE?\n"
                + "Recipient: +27834557896\n"
                + "Message: Did you get the cake?\n\n"
                + "Message ID: 0838884567\n"
                + "Message Hash: 08:4:ITTIME!\n"
                + "Recipient: Developer\n"
                + "Message: It is dinner time!\n\n";

        assertEquals(expected, store.getReport(MessageStore.FLAG_SENT));
    }

    @Test
    public void testDisplayStoredReport() {
        String expected
                = "Message ID: 2000000002\n"
                + "Message Hash: 20:2:WHERETIME.\n"
                + "Recipient: +27838884567\n"
                + "Message: " + MESSAGE_2 + "\n\n"
                + "Message ID: 5000000005\n"
                + "Message Hash: 50:5:OK,YOU.\n"
                + "Recipient: +27838884567\n"
                + "Message: " + MESSAGE_5 + "\n\n";

        assertEquals(expected, store.getReport(MessageStore.FLAG_STORED));
    }

    @Test
    public void testReportWhenNoMessages() {
        assertEquals("There are no stored messages.",
                new MessageStore().getReport(MessageStore.FLAG_STORED));
    }


    @Test
    public void testDisplaySenderAndRecipientOfStoredMessages() {
        assertEquals("Sender: Amogelang | Recipient: +27838884567\n"
                + "Sender: Amogelang | Recipient: +27838884567\n",
                store.displaySendersAndRecipients("Amogelang"));
    }

  

    @Test
    public void testReadJsonFileIntoStoredArray(@TempDir Path folder)
            throws IOException {

        Path file = folder.resolve("messages.json");

        Files.writeString(file,
                "[{\"messageID\":\"1234567890\",\"messageNumber\":1,"
                + "\"recipient\":\"+27821234567\","
                + "\"messageText\":\"Hello, how are you?\","
                + "\"messageHash\":\"12:1:HELLOYOU?\","
                + "\"messageStatus\":\"Stored\"}]");

        MessageStore fileStore = new MessageStore();
        int loaded = MessageFileHandler.loadStoredMessages(file.toString(), fileStore);

        assertEquals(1, loaded);
        assertArrayEquals(new String[]{"Hello, how are you?"},
                fileStore.getStoredMessages());
        assertEquals("12:1:HELLOYOU?", fileStore.getMessageHashes()[0]);
    }

    @Test
    public void testReadMissingJsonFile(@TempDir Path folder) {
        assertEquals(0, MessageFileHandler.loadStoredMessages(
                folder.resolve("missing.json").toString(), new MessageStore()));
    }

    @Test
    public void testDeleteRemovesMessageFromJsonFile(@TempDir Path folder)
            throws IOException {

        Path file = folder.resolve("messages.json");

        Files.writeString(file,
                "[{\"messageID\":\"1\",\"messageNumber\":1,"
                + "\"recipient\":\"+27821234567\",\"messageText\":\"A\","
                + "\"messageHash\":\"H1\",\"messageStatus\":\"Stored\"},"
                + "{\"messageID\":\"2\",\"messageNumber\":2,"
                + "\"recipient\":\"+27821234567\",\"messageText\":\"B\","
                + "\"messageHash\":\"H2\",\"messageStatus\":\"Stored\"}]");

        MessageFileHandler.removeStoredMessage(file.toString(), "H1");

        MessageStore fileStore = new MessageStore();
        MessageFileHandler.loadStoredMessages(file.toString(), fileStore);

        assertArrayEquals(new String[]{"B"}, fileStore.getStoredMessages());
    }
}
