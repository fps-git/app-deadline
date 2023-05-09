package ru.netology.data;

import com.github.javafaker.Faker;
import lombok.SneakyThrows;
import lombok.Value;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.DriverManager;

public class DataHelper {

    @Value
    public static class UserData {
        String login;
        String password;
    }

    static String loginDB = "app";
    static String passwordDB = "pass";

    private static Faker faker = new Faker();

        public static UserData getValidUserData() {
            return new UserData("vasya", "qwerty123");
        }

    public static UserData getInvalidPassUserData() {
        return new UserData("vasya", faker.internet().password());
    }

    public static UserData getNotRegisteredUserData() {
        return new UserData(faker.name().username(), faker.internet().password());
    }

    @SneakyThrows
    public static String getValidVerificationCode(UserData user) {
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

    public static String getInvalidVerificationCode() {
        return faker.numerify("######");
    }
}
