/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
 
package com.mycompany.chatapp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/*
 * This class contains unit tests for the Login class.
 * The tests check that the registration and login
 * features are working correctly.
 */
public class LoginTest {

    /*
     * Test 1:
     * Username is correctly formatted.
     */
    @Test
    public void testUsernameCorrect() {

        Login login = new Login(
                "kyl_1",
                "Ch&&sec@ke99!",
                "+27838968976",
                "Kyle",
                "Smith"
        );

        assertTrue(login.checkUserName());
    }

    /*
     * Test 2:
     * Username is incorrectly formatted.
     */
    @Test
    public void testUsernameIncorrect() {

        Login login = new Login(
                "kyle!!!!!!!",
                "Ch&&sec@ke99!",
                "+27838968976",
                "Kyle",
                "Smith"
        );

        assertFalse(login.checkUserName());
    }

    /*
     * Test 3:
     * Password meets the complexity requirements.
     */
    @Test
    public void testPasswordCorrect() {

        Login login = new Login(
                "kyl_1",
                "Ch&&sec@ke99!",
                "+27838968976",
                "Kyle",
                "Smith"
        );

        assertTrue(login.checkPasswordComplexity());
    }

    /*
     * Test 4:
     * Password does not meet the complexity requirements.
     */
    @Test
    public void testPasswordIncorrect() {

        Login login = new Login(
                "kyl_1",
                "password",
                "+27838968976",
                "Kyle",
                "Smith"
        );

        assertFalse(login.checkPasswordComplexity());
    }

    /*
     * Test 5:
     * Cellphone number is correctly formatted.
     */
    @Test
    public void testCellPhoneCorrect() {

        Login login = new Login(
                "kyl_1",
                "Ch&&sec@ke99!",
                "+27838968976",
                "Kyle",
                "Smith"
        );

        assertTrue(login.checkCellPhoneNumber());
    }

    /*
     * Test 6:
     * Cellphone number is incorrectly formatted.
     */
    @Test
    public void testCellPhoneIncorrect() {

        Login login = new Login(
                "kyl_1",
                "Ch&&sec@ke99!",
                "08966553",
                "Kyle",
                "Smith"
        );

        assertFalse(login.checkCellPhoneNumber());
    }

    /*
     * Test 7:
     * Login is successful when the correct
     * username and password are entered.
     */
    @Test
    public void testLoginSuccessful() {

        Login login = new Login(
                "kyl_1",
                "Ch&&sec@ke99!",
                "+27838968976",
                "Kyle",
                "Smith"
        );

        assertTrue(
                login.loginUser("kyl_1", "Ch&&sec@ke99!")
        );
    }

    /*
     * Test 8:
     * Login fails when incorrect details are entered.
     */
    @Test
    public void testLoginFailed() {

        Login login = new Login(
                "kyl_1",
                "Ch&&sec@ke99!",
                "+27838968976",
                "Kyle",
                "Smith"
        );

        assertFalse(
                login.loginUser("wrong", "wrong")
        );
    }

    /*
     * Test 9:
     * Check the successful login message.
     */
    @Test
    public void testSuccessfulLoginMessage() {

        Login login = new Login(
                "kyl_1",
                "Ch&&sec@ke99!",
                "+27838968976",
                "Kyle",
                "Smith"
        );

        login.loginUser("kyl_1", "Ch&&sec@ke99!");

        assertEquals(
                "Welcome Kyle, Smith it is great to see you again.",
                login.returnLoginStatus()
        );
    }

    /*
     * Test 10:
     * Check the failed login message.
     */
    @Test
    public void testFailedLoginMessage() {

        Login login = new Login(
                "kyl_1",
                "Ch&&sec@ke99!",
                "+27838968976",
                "Kyle",
                "Smith"
        );

        login.loginUser("wrong", "wrong");

        assertEquals(
                "Username or password incorrect, please try again.",
                login.returnLoginStatus()
        );
    }

    /*
     * Test 11:
     * Check the successful registration message.
     */
    @Test
    public void testRegistrationSuccessful() {

        Login login = new Login(
                "kyl_1",
                "Ch&&sec@ke99!",
                "+27838968976",
                "Kyle",
                "Smith"
        );

        assertEquals(
                "User has been registered successfully.",
                login.registerUser()
        );
    }

    /*
     * Test 12:
     * Check the incorrect username registration message.
     */
    @Test
    public void testRegistrationUsernameIncorrect() {

        Login login = new Login(
                "kyle!!!!!!!",
                "Ch&&sec@ke99!",
                "+27838968976",
                "Kyle",
                "Smith"
        );

        assertEquals(
                "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.",
                login.registerUser()
        );
    }

    /*
     * Test 13:
     * Check the incorrect password registration message.
     */
    @Test
    public void testRegistrationPasswordIncorrect() {

        Login login = new Login(
                "kyl_1",
                "password",
                "+27838968976",
                "Kyle",
                "Smith"
        );

        assertEquals(
                "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.",
                login.registerUser()
        );
    }

    /*
     * Test 14:
     * Check that a correctly formatted username,
     * password and cellphone number all pass validation.
     */
    @Test
    public void testAllRegistrationDetailsCorrect() {

        Login login = new Login(
                "kyl_1",
                "Ch&&sec@ke99!",
                "+27838968976",
                "Kyle",
                "Smith"
        );

        assertTrue(login.checkUserName());
        assertTrue(login.checkPasswordComplexity());
        assertTrue(login.checkCellPhoneNumber());
    }
}