package ru.netology.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;

class LoginTest {

    @Test
    @DisplayName("Should login with valid user data")
    void shouldLogin() {
        open("http://localhost:9999");
        var data = new DataHelper();
        new LoginPage().validLogin(data.getValidUserData())
                .validVerify(data.getValidVerificationCode(data.getValidUserData()));
    }

    @Test
    @DisplayName("Should not login with invalid password")
    void shouldNotLoginWithInvalidPassword() {
        open("http://localhost:9999");
        var data = new DataHelper();
        new LoginPage().invalidLogin(data.getInvalidPassUserData());
    }

    @Test
    @DisplayName("Should not login if user is not registered")
    void shouldNotLoginIfUserNotRegistered() {
        open("http://localhost:9999");
        var data = new DataHelper();
        new LoginPage().invalidLogin(data.getNotRegisteredUserData());
    }

    @Test
    @DisplayName("Should show error message if verification code is invalid")
    void shouldShowErrorMessageIfBadVerificationCode() {
        open("http://localhost:9999");
        var data = new DataHelper();
        new LoginPage().validLogin(data.getValidUserData()).invalidVerify(data.getInvalidVerificationCode());
    }

    @Test
    @DisplayName("Should show error message when user is blocked")
    void shouldShowErrorMessageIfBlocked() {
        open("http://localhost:9999");
        var data = new DataHelper();
        var user = data.getValidUserData();
        data.setUserStatus(user, "blocked");

        new LoginPage().blockedUserLogin(user);

        data.setUserStatus(user, "active");
    }

    @Test
    @DisplayName("Should block user if invalid password is entered 3 times")
    void shouldBlockUser() {
        open("http://localhost:9999");
        var data = new DataHelper();
        var invalidPassUser = data.getInvalidPassUserData();

        for (int i = 0; i < 3; i++) {
            new LoginPage().invalidLogin(invalidPassUser);
        }

        String expected = "blocked";
        String actual = data.getUserStatus(invalidPassUser);

        Assertions.assertEquals(expected, actual);

        data.setUserStatus(invalidPassUser, "active");
    }
}

