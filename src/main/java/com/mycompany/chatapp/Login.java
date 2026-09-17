/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatapp;

/**
 *
 * @author Student
 */
public class Login {
    
   private String username;
    private String password;
    private String cellphone;
    private String firstName;
    private String lastName;

    private boolean loginSuccessful;

    // Constructor receives the user's registration information
    public Login(String username, String password, String cellphone,
                 String firstName, String lastName) {

        this.username = username;
        this.password = password;
        this.cellphone = cellphone;
        this.firstName = firstName;
        this.lastName = lastName;
        this.loginSuccessful = false;
    }

    // Checks if the username contains an underscore and is 5 characters or less
    public boolean checkUserName() {
        return username.contains("_") && username.length() <= 5;
    }

    // Checks if the password meets all the required conditions
    public boolean checkPasswordComplexity() {

        if (password.length() < 8) {
            return false;
        }

        boolean hasCapital = false;
        boolean hasNumber = false;
        boolean hasSpecial = false;

        for (int i = 0; i < password.length(); i++) {

            char character = password.charAt(i);

            if (Character.isUpperCase(character)) {
                hasCapital = true;
            }

            if (Character.isDigit(character)) {
                hasNumber = true;
            }

            if (!Character.isLetterOrDigit(character)) {
                hasSpecial = true;
            }
        }

        return hasCapital && hasNumber && hasSpecial;
    }

    // Checks if the cellphone number starts with +27 followed by 1 to 10 digits
    public boolean checkCellPhoneNumber() {
        return cellphone.matches("^\\+27\\d{1,10}$");
    }

    // Checks all registration requirements and returns the exact messages
    public String registerUser() {

        if (!checkUserName()) {
            return "Username is not correctly formatted; please ensure that your username "
                    + "contains an underscore and is no more than five characters in length.";
        }

        if (!checkPasswordComplexity()) {
            return "Password is not correctly formatted; please ensure that the password "
                    + "contains at least eight characters, a capital letter, a number, "
                    + "and a special character.";
        }

        if (!checkCellPhoneNumber()) {
            return "Cell phone number is incorrectly formatted or does not contain an "
                    + "international code; please correct the number and try again.";
        }

        return "User has been registered successfully.";
    }

    // Checks if the entered login information matches the registered information
    public boolean loginUser(String enteredUsername, String enteredPassword) {
        loginSuccessful = enteredUsername.equals(username)
                && enteredPassword.equals(password);
        return loginSuccessful;
    }

    // Gives the user a message depending on whether login was successful
    public String returnLoginStatus() {

        if (loginSuccessful) {
            return "Welcome " + firstName + ", " + lastName + " it is great to see you again.";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }
}