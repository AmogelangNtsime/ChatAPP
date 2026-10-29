/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.chatapp;

import java.util.Scanner;

/**
 * Main class of the QuickChat application. Handles registration, login
 * and the main menu. Every message is recorded in a MessageStore so the
 * Stored Messages menu (option 4) can search, delete and report on them.
 *
 * @author Student
 */
public class ChatApp {

    private static final String MESSAGES_FILE = "messages.json";

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("====================================");
        System.out.println("          CHAT APPLICATION");
        System.out.println("====================================");

        // PART 1: REGISTRATION
        System.out.print("Please enter your first name: ");
        String firstName = scanner.nextLine();

        System.out.print("Please enter your last name: ");
        String lastName = scanner.nextLine();

        System.out.print("Please enter your username: ");
        String username = scanner.nextLine();

        System.out.print("Please enter your password: ");
        String password = scanner.nextLine();

        System.out.print("Please enter your cellphone number: ");
        String cellphone = scanner.nextLine();

        Login login = new Login(
                username, password, cellphone, firstName, lastName);

        System.out.println("\n----------- REGISTRATION -----------");

        System.out.println(login.registerUser());

        if (!login.checkUserName()
                || !login.checkPasswordComplexity()
                || !login.checkCellPhoneNumber()) {

            System.out.println(
                    "Registration unsuccessful. Please correct your details.");
            scanner.close();
            return;
        }

        // PART 1: LOGIN
        System.out.println("\n--------------- LOGIN ---------------");

        System.out.print("Please enter username: ");
        String enteredUsername = scanner.nextLine();

        System.out.print("Please enter password: ");
        String enteredPassword = scanner.nextLine();

        login.loginUser(enteredUsername, enteredPassword);

        System.out.println(login.returnLoginStatus());

        // PART 2: allows access only after successful login
        if (!login.loginUser(enteredUsername, enteredPassword)) {
            scanner.close();
            return;
        }

        System.out.println("\nWelcome to QuickChat.");

        // Ask how many messages the user wants to send
        int numberOfMessages = 0;

        while (numberOfMessages <= 0) {

            System.out.print(
                    "How many messages would you like to send? ");

            String input = scanner.nextLine();

            try {
                numberOfMessages = Integer.parseInt(input);

                if (numberOfMessages <= 0) {
                    System.out.println(
                            "Please enter a number greater than zero.");
                }

            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid whole number.");
            }
        }

        // PART 3: arrays that hold every message (populated at runtime)
        MessageStore store = new MessageStore();
        String sender = firstName + " " + lastName;

        int loaded = MessageFileHandler.loadStoredMessages(MESSAGES_FILE, store);
        System.out.println(loaded + " stored message(s) loaded from "
                + MESSAGES_FILE + ".");

        int choice = 0;
        int messagesCreated = 0;
        int messageNumber = 0;

        // Main menu loop
        while (choice != 3) {

            System.out.println("\n========== QUICKCHAT MENU ==========");
            System.out.println("1. Send Messages");
            System.out.println("2. Show recently sent messages");
            System.out.println("3. Quit");
            System.out.println("4. Stored Messages");
            System.out.print("Choose an option: ");

            String input = scanner.nextLine();

            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                choice = 0;
            }

            switch (choice) {

                case 1:

                    // Send messages until the chosen limit is reached
                    while (messagesCreated < numberOfMessages) {

                        System.out.println("\nMessage "
                                + (messagesCreated + 1)
                                + " of " + numberOfMessages);

                        System.out.print("Enter recipient cellphone number: ");
                        String recipient = scanner.nextLine();

                        System.out.print("Enter your message: ");
                        String messageText = scanner.nextLine();

                        Message message = new Message(
                                ++messageNumber, recipient, messageText);

                        System.out.println(
                                "Message ID generated: "
                                + message.getMessageID());

                        System.out.println(message.checkRecipientCell());
                        System.out.println(message.checkMessageLength());

                        if (!message.checkMessageID()
                                || !message.checkRecipientCell().equals(
                                "Cell phone number successfully captured.")
                                || !message.checkMessageLength().equals(
                                "Message ready to send.")) {

                            System.out.println(
                                    "Please correct the message details.");
                            continue;
                        }

                        System.out.println(
                                "Message Hash: " + message.createMessageHash());

                        System.out.println(message.SentMessage(scanner));

                        // Add the message to the Sent, Stored or
                        // Disregarded array (ignored while still Pending)
                        store.addMessage(message);

                        if ("Sent".equals(message.getMessageStatus())) {

                            System.out.println("Message ID: "
                                    + message.getMessageID());
                            System.out.println("Message Hash: "
                                    + message.getMessageHash());
                            System.out.println("Recipient: "
                                    + message.getRecipient());
                            System.out.println("Message: "
                                    + message.getMessageText());

                            messagesCreated++;
                        }
                    }

                    System.out.println(
                            "You have completed your selected messages.");
                    System.out.println(
                            "Total messages sent: "
                            + new Message(0, "+27812345678", "Count")
                                    .returnTotalMessages());

                    break;

                case 2:

                    System.out.println("\n--- Recently sent messages ---");
                    System.out.println(
                            store.getReport(MessageStore.FLAG_SENT));
                    break;

                case 3:

                    System.out.println(
                            "Thank you for using QuickChat.");
                    break;

                case 4:

                    storedMessagesMenu(scanner, store, sender);
                    break;

                default:

                    System.out.println(
                            "Invalid option. Please choose 1, 2, 3 or 4.");
                    break;
            }
        }

        scanner.close();
    }

    /**
     * Fourth main menu option: Stored Messages. Lets the user search,
     * delete and report on the messages held in the arrays.
     */
    private static void storedMessagesMenu(Scanner scanner,
                                           MessageStore store,
                                           String sender) {

        String option = "";

        while (!option.equals("7")) {

            System.out.println("\n======== STORED MESSAGES ========");
            System.out.println("1. Display sender and recipient of all stored messages");
            System.out.println("2. Display the longest stored message");
            System.out.println("3. Search for a message ID");
            System.out.println("4. Search for all messages stored for a recipient");
            System.out.println("5. Delete a message using the message hash");
            System.out.println("6. Display report of all stored messages");
            System.out.println("7. Back to main menu");
            System.out.print("Choose an option: ");

            option = scanner.nextLine().trim();

            switch (option) {

                case "1":
                    System.out.println(store.displaySendersAndRecipients(sender));
                    break;

                case "2":
                    System.out.println("Longest stored message: "
                            + store.findLongestStoredMessage());
                    break;

                case "3":
                    System.out.print("Enter the message ID: ");
                    System.out.println(
                            store.searchByMessageID(scanner.nextLine().trim()));
                    break;

                case "4":
                    System.out.print("Enter the recipient cellphone number: ");
                    System.out.println(
                            store.searchByRecipient(scanner.nextLine().trim()));
                    break;

                case "5":
                    deleteMessage(scanner, store);
                    break;

                case "6":
                    System.out.println(
                            store.getReport(MessageStore.FLAG_STORED));
                    break;

                case "7":
                    break;

                default:
                    System.out.println(
                            "Invalid option. Please choose 1 to 7.");
                    break;
            }
        }
    }

    /** Deletes a message by hash and removes it from the JSON file too. */
    private static void deleteMessage(Scanner scanner, MessageStore store) {

        System.out.print("Enter the message hash: ");
        String hash = scanner.nextLine().trim();

        boolean exists = store.hasHash(hash);

        System.out.println(store.deleteByHash(hash));

        if (exists) {
            MessageFileHandler.removeStoredMessage(MESSAGES_FILE, hash);
        }
    }
}