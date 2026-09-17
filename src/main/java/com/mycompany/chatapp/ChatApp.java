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

        System.out.println("Please enter your first name: ");
        String firstName = scanner.nextLine();

        System.out.println("Please enter your last name: ");
        String lastName = scanner.nextLine();

        System.out.println("Please enter your username: ");
        String username = scanner.nextLine();

        System.out.println("Please enter your password: ");
        String password = scanner.nextLine();

        System.out.println("Please enter your cellphone number: ");
        String cellphone = scanner.nextLine();

        // Creates a login object using the user's information
        Login login = new Login(username, password, cellphone, firstName, lastName);

        System.out.println();
        System.out.println("----------- REGISTRATION -----------");

        if (login.checkUserName()) {
            System.out.println("Username successfully captured.");
        } else {
            System.out.println("Username is not correctly formatted; "
                    + "please ensure that your username contains an underscore "
                    + "and is no more than five characters in length.");
        }

        if (login.checkPasswordComplexity()) {
            System.out.println("Password successfully captured.");
        } else {
            System.out.println("Password is not correctly formatted; "
                    + "please ensure that the password contains at least "
                    + "eight characters, a capital letter, a number, "
                    + "and a special character.");
        }
   if (login.checkCellPhoneNumber()) {
            System.out.println("Cell phone number successfully added.");
        } else {
            System.out.println("Cell phone number is incorrectly formatted "
                    + "or does not contain an international code; "
                    + "please correct the number and try again.");
        }

        System.out.println();
        System.out.println(login.registerUser());

        // Login should only happen if registration details are correct
        if (login.checkUserName()
                && login.checkPasswordComplexity()
                && login.checkCellPhoneNumber()) {

            System.out.println();
            System.out.println("--------------- LOGIN ---------------");

            System.out.println("Please enter username: ");
            String enteredUsername = scanner.nextLine();

            System.out.println("Please enter password: ");
            String enteredPassword = scanner.nextLine();

            login.loginUser(enteredUsername, enteredPassword);

            System.out.println(login.returnLoginStatus());

        } else {
            System.out.println();
            System.out.println("Registration unsuccessful. Please correct your details.");
        }

        scanner.close();
    }
}
