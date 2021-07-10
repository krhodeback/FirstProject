package com.javamentor.main;

import java.util.List;

import com.javamentor.dao.UserDao;
import com.javamentor.dao.UserDaoJDBCImpl;
import com.javamentor.exceptions.DaoException;
import com.javamentor.model.User;
import com.javamentor.service.UserService;
import com.javamentor.service.UserServiceImpl;

public class Main {
    public static void main(String[] args) throws DaoException {
        UserService user = new UserServiceImpl();
        user.createUsersTable();
        user.saveUser("user1", "lastName1", (byte) 21);
        user.saveUser("user2", "lastName2", (byte) 21);
        user.saveUser("user3", "lastName3", (byte) 21);
        user.saveUser("user4", "lastName4", (byte) 21);
        List<User> list = user.getAllUsers();
        list.stream().forEach(System.out::println);
        user.cleanUsersTable();
        user.dropUsersTable();

    }
}
