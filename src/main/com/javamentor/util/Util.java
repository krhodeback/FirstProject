package com.javamentor.util;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.javamentor.exceptions.DaoException;

public class Util {
    private static final String URL = "jdbc:mysql://localhost:3306/sys";
    private static final String USER_NAME = "root";
    private static final String PASSWORD = "1234";
    private static final String DRIVERS = "com.mysql.cj.jdbc.Driver";

    public Connection getConnetion() throws DaoException {
        try {
            Class.forName(DRIVERS);
            Connection connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
            return connection;
        } catch (SQLException | ClassNotFoundException e) {
            throw new DaoException("Cant get connection", e);
        }
    }

}
