package com.javamentor.dao;

import com.javamentor.exceptions.DaoException;
import com.javamentor.model.User;
import com.javamentor.util.Util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.ibatis.jdbc.ScriptRunner;

public class UserDaoJDBCImpl implements UserDao {
    private static final String CREATE_FILE_NAME = "createTables.sql";
    private static final String DELETE_FILE_NAME = "deleteTables.sql";
    private static final String INSERT_USER = "INSERT INTO sys.users (name,lastName,age) VALUES (?,?,?)";
    private static final String DELETE_USER = "DELETE  FROM sys.users WHERE id = ?";
    private static final String DELETE_ALL_USERS = "DELETE  FROM sys.users ";
    private static final String FIND_ALL_USERS = "SELECT * FROM sys.users";
    private Util util;
    private Logger log;

    public UserDaoJDBCImpl() {
        util = new Util();
        log = Logger.getLogger("UserDaoJDBCImpl");
    }

    public void createUsersTable() {
        try {
            scriptRun(CREATE_FILE_NAME);
            log.log(Level.INFO, "Tables have been created");
        } catch (DaoException e) {
            log.log(Level.SEVERE, "Cant create tebles");
        }
    }

    public void dropUsersTable() {
        try {
            scriptRun(DELETE_FILE_NAME);
            log.log(Level.INFO, "Tables have been deleted");
        } catch (DaoException e) {
            log.log(Level.SEVERE, "Cant delete tables");
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try (Connection connection = util.getConnetion();
                PreparedStatement prSt = connection.prepareStatement(INSERT_USER)) {
            prSt.setString(1, name);
            prSt.setString(2, lastName);
            prSt.setByte(3, age);
            prSt.executeUpdate();
            log.log(Level.INFO, "User " + name + " have been added");
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Cant add user", e);
        } catch (DaoException e) {
            log.log(Level.SEVERE, "Cant add user", e);
        }
    }

    public void removeUserById(long id) {
        try (Connection connection = util.getConnetion();
                PreparedStatement prSt = connection.prepareStatement(DELETE_USER)) {
            prSt.setLong(1, id);
            prSt.executeUpdate();
            log.log(Level.INFO, "User have been deleted");
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Cant remove user", e);
        } catch (DaoException e) {
            log.log(Level.SEVERE, "Cant remove user", e);
        }
    }

    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<User>();
        try (Connection connection = util.getConnetion();
                Statement statement = connection.createStatement();
                ResultSet reSet = statement.executeQuery(FIND_ALL_USERS)) {
            while (reSet.next()) {
                userList.add(new User(reSet.getString(2), reSet.getString(3), reSet.getByte(4)) {
                    {
                        setId(reSet.getLong(1));
                    }

                });
            }
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Cant get all users", e);
        } catch (DaoException e) {
            log.log(Level.SEVERE, "Cant get all users", e);
        }
        return userList;
    }

    public void cleanUsersTable() {
        try (Connection connection = util.getConnetion();
                PreparedStatement prSt = connection.prepareStatement(DELETE_ALL_USERS)) {
            prSt.executeUpdate();
            log.log(Level.INFO, "All users have been deleted");
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Cant remove users", e);
        } catch (DaoException e) {
            log.log(Level.SEVERE, "Cant remove users", e);
        }
    }

    private void scriptRun(String fileName) throws DaoException {
        String path = getClass().getClassLoader().getResource(fileName).getFile();
        try (Connection conn = util.getConnetion(); Reader reader = new BufferedReader(new FileReader(path));) {
            ScriptRunner sr = new ScriptRunner(conn);
            sr.runScript(reader);
            log.log(Level.INFO, "Tables have been changed");
        } catch (FileNotFoundException e) {
            throw new DaoException("Cant find file with tables", e);
        } catch (IOException e) {
            throw new DaoException("Cant generate tables ", e);
        } catch (SQLException e) {

        } catch (DaoException e) {
        }
    }
}
