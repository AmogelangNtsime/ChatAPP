/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.chatapp;

import java.util.Scanner;

/**
 *
 * @author Student
 */
public class ChatApp {

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

        // PART 2: Allow access only after successful login
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

        int choice = 0;
        int messagesCreated = 0;
        int messageNumber = 0;

        // while loop
        while (choice != 3) {

            System.out.println("\n========== QUICKCHAT MENU ==========");
            System.out.println("1. Send Messages");
            System.out.println("2. Show recently sent messages");
            System.out.println("3. Quit");
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

                        System.out.println(message.SentMessage());

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

                    System.out.println("Coming Soon.");
                    break;

                case 3:

                    System.out.println(
                            "Thank you for using QuickChat.");
                    break;

                default:

                    System.out.println(
                            "Invalid option. Please choose 1, 2 or 3.");
                    break;
            }
        }

        scanner.close();
    }
}