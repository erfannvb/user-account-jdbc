package com.example.repository.user;

import com.example.connection.JdbcConnection;
import com.example.entity.Gender;
import com.example.entity.User;
import com.example.entity.UserRole;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.example.repository.user.UserQueries.*;

public class UserRepositoryImpl implements UserRepository {

    private Connection connection = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    @Override
    public void save(User user) {
        try {

            connection = JdbcConnection.getConnection();
            if (connection == null)
                System.out.println("Error getting the connection.");

            preparedStatement = connection.prepareStatement(INSERT_INTO_USER);

            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setObject(3, user.getUserRole(), Types.OTHER);
            preparedStatement.setString(4, user.getUsername());
            preparedStatement.setObject(5, user.getGender(), Types.OTHER);
            preparedStatement.setInt(6, user.getAge());

            preparedStatement.execute();
            System.out.println("New user added to the database successfully.");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveAll(List<User> userList) {
        try {

            connection = JdbcConnection.getConnection();
            if (connection == null)
                System.out.println("Error getting the connection.");

            preparedStatement = connection.prepareStatement(INSERT_INTO_USER);

            for (int i = 0; i < userList.size(); i++) {
                preparedStatement.setString(1, userList.get(i).getFirstName());
                preparedStatement.setString(2, userList.get(i).getLastName());
                preparedStatement.setObject(3, userList.get(i).getUserRole(), Types.OTHER);
                preparedStatement.setString(4, userList.get(i).getUsername());
                preparedStatement.setObject(5, userList.get(i).getGender(), Types.OTHER);
                preparedStatement.setInt(6, userList.get(i).getAge());
                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                JdbcConnection.closeConnection(connection);
                JdbcConnection.closePreparedStatement(preparedStatement);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void update(User user) {
        try {

            connection = JdbcConnection.getConnection();
            if (connection == null)
                System.out.println("Error getting the connection.");

            preparedStatement = connection.prepareStatement(UPDATE_USER);

            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setObject(3, user.getUserRole(), Types.OTHER);
            preparedStatement.setString(4, user.getUsername());
            preparedStatement.setObject(5, user.getGender(), Types.OTHER);
            preparedStatement.setInt(6, user.getAge());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                JdbcConnection.closeConnection(connection);
                JdbcConnection.closePreparedStatement(preparedStatement);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void deleteById(Long userId) {
        try {

            connection = JdbcConnection.getConnection();
            if (connection == null)
                System.out.println("Error getting the connection.");

            preparedStatement = connection.prepareStatement(DELETE_USER_BY_ID);
            preparedStatement.setLong(1, userId);
            preparedStatement.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                JdbcConnection.closeConnection(connection);
                JdbcConnection.closePreparedStatement(preparedStatement);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public User loadById(Long userId) {
        User user = new User();

        try {

            connection = JdbcConnection.getConnection();
            if (connection == null)
                System.out.println("Error getting the connection.");

            preparedStatement = connection.prepareStatement(SELECT_USER_BY_ID);
            preparedStatement.setLong(1, userId);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                user.setUserId(resultSet.getLong("user_id"));
                user.setFirstName(resultSet.getString("first_name"));
                user.setLastName(resultSet.getString("last_name"));
                user.setUserRole((UserRole) resultSet.getObject("user_role"));
                user.setUsername(resultSet.getString("username"));
                user.setGender((Gender) resultSet.getObject("gender"));
                user.setAge(resultSet.getInt("age"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                JdbcConnection.closeConnection(connection);
                JdbcConnection.closePreparedStatement(preparedStatement);
                JdbcConnection.closeResultSet(resultSet);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return user;
    }

    @Override
    public List<User> loadAll() {
        List<User> userList = new ArrayList<>();

        try {

            connection = JdbcConnection.getConnection();
            if (connection == null)
                System.out.println("Error getting the connection.");

            preparedStatement = connection.prepareStatement(SELECT_ALL);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                user.setUserId(resultSet.getLong("user_id"));
                user.setFirstName(resultSet.getString("first_name"));
                user.setLastName(resultSet.getString("last_name"));
                user.setUserRole((UserRole) resultSet.getObject("user_role"));
                user.setUsername(resultSet.getString("username"));
                user.setGender((Gender) resultSet.getObject("gender"));
                user.setAge(resultSet.getInt("age"));
                userList.add(user);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                JdbcConnection.closeConnection(connection);
                JdbcConnection.closePreparedStatement(preparedStatement);
                JdbcConnection.closeResultSet(resultSet);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return userList;
    }
}
