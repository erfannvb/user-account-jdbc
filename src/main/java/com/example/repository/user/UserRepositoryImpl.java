package com.example.repository.user;

import com.example.connection.JdbcConnection;
import com.example.entity.enumeration.Gender;
import com.example.entity.User;
import com.example.entity.enumeration.UserRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.example.repository.user.UserQueries.*;

public class UserRepositoryImpl implements UserRepository {

    private static final Logger logger = LoggerFactory.getLogger(UserRepositoryImpl.class);

    private static final String CONNECTION_ERROR = "Error getting the connection.";

    private static final String USER_ID = "user_id";
    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";
    private static final String EMAIL = "email";
    private static final String USER_ROLE = "user_role";
    private static final String USERNAME = "username";
    private static final String GENDER = "gender";
    private static final String AGE = "age";

    private Connection connection = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    @Override
    public void save(User user) {
        try {

            connection = getConnection();
            if (connection == null)
                logger.info(CONNECTION_ERROR);

            preparedStatement = connection.prepareStatement(INSERT_INTO_USER);

            setData(user);

            preparedStatement.execute();
            logger.info("New user added to the database successfully.");

        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        } finally {
            closeConnection();
            closePreparedStatement();
            closeResultSet();
        }
    }

    @Override
    public long saveAndReturnUserId(User user) {
        long userId = 0;
        try {

            connection = getConnection();
            if (connection == null)
                logger.info(CONNECTION_ERROR);

            preparedStatement = connection.prepareStatement(INSERT_INTO_USER,
                    Statement.RETURN_GENERATED_KEYS);

            setData(user);

            resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();
            userId = resultSet.getLong(USER_ID);

        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        } finally {
            closeConnection();
            closePreparedStatement();
            closeResultSet();
        }
        return userId;
    }

    @Override
    public void saveAll(List<User> userList) {
        try {

            connection = getConnection();
            if (connection == null)
                logger.info(CONNECTION_ERROR);

            preparedStatement = connection.prepareStatement(INSERT_INTO_USER);

            for (User user : userList) {
                setData(user);
            }

            preparedStatement.executeBatch();

        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        } finally {
            closeConnection();
            closePreparedStatement();
            closeResultSet();
        }
    }

    @Override
    public void update(User user) {
        try {

            connection = getConnection();
            if (connection == null)
                logger.info(CONNECTION_ERROR);

            preparedStatement = connection.prepareStatement(UPDATE_USER);

            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setObject(4, user.getUserRole(), Types.OTHER);
            preparedStatement.setString(5, user.getUsername());
            preparedStatement.setObject(6, user.getGender(), Types.OTHER);
            preparedStatement.setInt(7, user.getAge());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        } finally {
            closeConnection();
            closePreparedStatement();
            closeResultSet();
        }
    }

    @Override
    public void deleteById(Long userId) {
        try {

            connection = getConnection();
            if (connection == null)
                logger.info(CONNECTION_ERROR);

            preparedStatement = connection.prepareStatement(DELETE_USER_BY_ID);
            preparedStatement.setLong(1, userId);
            preparedStatement.execute();

        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        } finally {
            closeConnection();
            closePreparedStatement();
            closeResultSet();
        }
    }

    @Override
    public User loadById(Long userId) {
        User user = new User();

        try {

            connection = getConnection();
            if (connection == null)
                logger.info(CONNECTION_ERROR);

            preparedStatement = connection.prepareStatement(SELECT_USER_BY_ID);
            preparedStatement.setLong(1, userId);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                setUser(user);
            }

        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        } finally {
            closeConnection();
            closePreparedStatement();
            closeResultSet();
        }
        return user;
    }

    @Override
    public List<User> loadAll() {
        List<User> userList = new ArrayList<>();

        try {

            connection = getConnection();
            if (connection == null)
                logger.info(CONNECTION_ERROR);

            preparedStatement = connection.prepareStatement(SELECT_ALL_USERS);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                setUser(user);
                userList.add(user);
            }

        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        } finally {
            closeConnection();
            closePreparedStatement();
            closeResultSet();
        }
        return userList;
    }

    @Override
    public User[] loadAllUsingArray() {
        User[] users;
        try {

            connection = getConnection();
            if (connection == null)
                logger.info(CONNECTION_ERROR);

            preparedStatement = connection.prepareStatement(SELECT_ALL_USERS,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            resultSet = preparedStatement.executeQuery();

            int size = 0;

            resultSet.last();
            size = resultSet.getRow();
            resultSet.beforeFirst();

            users = new User[size];

            int count = 0;

            while (resultSet.next()) {
                User user = new User();
                setUser(user);
                users[count++] = user;
            }

        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        } finally {
            closeConnection();
            closePreparedStatement();
            closeResultSet();
        }
        return users;
    }

    @Override
    public int getNumberOfUsers() {
        int numberOfUsers = 0;
        try {

            connection = getConnection();
            if (connection == null)
                logger.info(CONNECTION_ERROR);

            preparedStatement = connection.prepareStatement(GET_NUMBER_OF_USERS);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                numberOfUsers = resultSet.getInt("number_of_users");
                System.out.println("Number of Users : " + numberOfUsers);
            }

        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        } finally {
            closeConnection();
            closePreparedStatement();
            closeResultSet();
        }
        return numberOfUsers;
    }

    @Override
    public long loadId() {
        long userId = 0;
        try {

            connection = getConnection();
            if (connection == null)
                logger.info(CONNECTION_ERROR);

            preparedStatement = connection.prepareStatement(SELECT_ALL_USERS);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next())
                userId = resultSet.getLong(USER_ID);

        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        } finally {
            closeConnection();
            closePreparedStatement();
            closeResultSet();
        }
        return userId;
    }

    private void setUser(User user) throws SQLException {
        user.setUserId(resultSet.getLong(USER_ID));
        user.setFirstName(resultSet.getString(FIRST_NAME));
        user.setLastName(resultSet.getString(LAST_NAME));
        user.setEmail(resultSet.getString(EMAIL));
        user.setUserRole((UserRole) resultSet.getObject(USER_ROLE));
        user.setUsername(resultSet.getString(USERNAME));
        user.setGender((Gender) resultSet.getObject(GENDER));
        user.setAge(resultSet.getInt(AGE));
    }

    private void setData(User user) throws SQLException {
        preparedStatement.setString(1, user.getFirstName());
        preparedStatement.setString(2, user.getLastName());
        preparedStatement.setObject(3, user.getUserRole(), Types.OTHER);
        preparedStatement.setString(4, user.getUsername());
        preparedStatement.setObject(5, user.getGender(), Types.OTHER);
        preparedStatement.setInt(6, user.getAge());
    }

    private static Connection getConnection() throws SQLException {
        return JdbcConnection.getConnection();
    }

    private void closeConnection() {
        try {
            JdbcConnection.closeConnection(connection);
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private void closePreparedStatement() {
        try {
            JdbcConnection.closePreparedStatement(preparedStatement);
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private void closeResultSet() {
        try {
            JdbcConnection.closeResultSet(resultSet);
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
