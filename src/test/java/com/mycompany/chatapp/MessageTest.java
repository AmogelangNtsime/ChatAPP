/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.chatapp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Student
 */
public class MessageTest {
    
    @Test
    public void testMessageLengthSuccess() {
        Message message = new Message(
                1, "+27718693002",
                "Hi Mike, can you join us for dinner tonight?"
        );

        assertEquals(
                "Message ready to send.",
                message.checkMessageLength()
        );
    }

    @Test
    public void testMessageLengthFailure() {
        StringBuilder longText = new StringBuilder();

        for (int i = 0; i < 251; i++) {
            longText.append("A");
        }

        Message message = new Message(
                1, "+27718693002", longText.toString()
        );

        assertEquals(
                "Message exceeds 250 characters by 1; please reduce the size.",
                message.checkMessageLength()
        );
    }

    @Test
    public void testRecipientCellSuccess() {
        Message message = new Message(
                1, "+27718693002", "Hello"
        );

        assertEquals(
                "Cell phone number successfully captured.",
                message.checkRecipientCell()
        );
    }

    @Test
    public void testRecipientCellFailure() {
        Message message = new Message(
                1, "08575975889", "Hello"
        );

        assertEquals(
                "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.",
                message.checkRecipientCell()
        );
    }

    @Test
    public void testMessageHashCaseOne() {
        Message message = new Message(
                1, "+27718693002", "Hi tonight"
        );

        String messageID = message.getMessageID();

        assertEquals(
                messageID.substring(0, 2) + ":1:HITONIGHT",
                message.createMessageHash()
        );
    }

    @Test
    public void testMessageHashUppercase() {
        Message message = new Message(
                2, "+27718693002", "Hi Mike"
        );

        assertEquals(
                message.getMessageHash().toUpperCase(),
                message.getMessageHash()
        );
    }

    @Test
    public void testMessageIDCreated() {
        Message message = new Message(
                1, "+27718693002", "Hello"
        );

        assertTrue(message.checkMessageID());
        assertEquals(10, message.getMessageID().length());
    }

    @Test
    public void testMessageSent() {
        Message message = new Message(
                1, "+27718693002", "Hello"
        );

        assertEquals(
                "Message successfully sent.",
                message.SentMessage("1")
        );

        assertEquals("Sent", message.getMessageStatus());
    }

    @Test
    public void testMessageDiscarded() {
        Message message = new Message(
                2, "+27718693002", "Hello"
        );

        assertEquals(
                "Press 0 to delete the message.",
                message.SentMessage("2")
        );

        assertEquals("Disregarded", message.getMessageStatus());
    }

    @Test
    public void testMessageStored() {
        Message message = new Message(
                3, "+27718693002", "Hello"
        );

        assertEquals(
                "Message successfully stored.",
                message.SentMessage("3")
        );

        assertEquals("Stored", message.getMessageStatus());
    }

    @Test
    public void testInvalidSendOption() {
        Message message = new Message(
                4, "+27718693002", "Hello"
        );

        assertEquals(
                "Invalid option.",
                message.SentMessage("9")
        );
    }

    @Test
    public void testReturnTotalMessages() {
        Message message = new Message(
                5, "+27718693002", "Hello"
        );

        int total = message.returnTotalMessages();

        assertTrue(total >= 0);
    }
}