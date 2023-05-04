package ru.netology.data;

import com.github.javafaker.Faker;
import lombok.SneakyThrows;
import lombok.Value;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.DriverManager;

public class DataHelper {

    @Value
    public class UserData {
        String login;
        String password;
    }

    String loginDB = "app";
    String passwordDB = "pass";

    Faker faker = new Faker();

    public UserData getValidUserData() {
        return new UserData("vasya", "qwerty123");
    }

    public UserData getInvalidPassUserData() {
        return new UserData("vasya", faker.internet().password());
    }

    public UserData getNotRegisteredUserData() {
        return new UserData(faker.name().username(), faker.internet().password());
    }

    @SneakyThrows
    public String getValidVerificationCode(UserData user) {
        var runner = new QueryRunner();
        String verificationCode;

        var getID = "SELECT id FROM users WHERE login = ?;";
        var getCode = "SELECT code FROM auth_codes WHERE user_id = ? ORDER BY created DESC LIMIT 1;";
        try (
                var conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/app", loginDB, passwordDB
                );
        ) {
            String id = runner.query(conn, getID, new ScalarHandler<>(), user.getLogin());
            verificationCode = runner.query(conn, getCode, new ScalarHandler<>(), id);
        }
        return verificationCode;
    }

    public String getInvalidVerificationCode() {
        return faker.numerify("######");
    }

    @SneakyThrows
    public String getUserStatus(UserData user) {
        var runner = new QueryRunner();
        String status;

        var getStatus = "SELECT status FROM users WHERE login = ?;";
        try (
                var conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/app", loginDB, passwordDB
                );
        ) {
            status = runner.query(conn, getStatus, new ScalarHandler<>(), user.getLogin());
        }
        return status;
    }

    @SneakyThrows
    public void setUserStatus(UserData user, String status) {
        var runner = new QueryRunner();

        var setStatus = "UPDATE users SET status= ? WHERE login = ?;";
        try (
                var conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/app", loginDB, passwordDB
                );
        ) {
            runner.update(conn, setStatus, status, user.getLogin());
        }
    }
}
