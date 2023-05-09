package ru.netology.data;

import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.DriverManager;

public class TestHelper {

    static String loginDB = "app";
    static String passwordDB = "pass";

    @SneakyThrows
    public static String getUserStatus(DataHelper.UserData user) {
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
    public static void setUserStatus(DataHelper.UserData user, String status) {
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
