package com.example.repository.account;

import com.example.connection.JdbcConnection;
import com.example.entity.Account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.example.repository.account.AccountQueries.*;

public class AccountRepositoryImpl implements AccountRepository {

    private Connection connection = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    @Override
    public void save(Account account) {
        try {

            connection = JdbcConnection.getConnection();
            if (connection == null)
                System.out.println("Error getting the connection.");

            preparedStatement = connection.prepareStatement(INSERT_INTO_ACCOUNT);

            preparedStatement.setString(1, account.getAccountName());
            preparedStatement.setLong(2, account.getUserId());

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
    public void saveAll(List<Account> accountList) {
        try {

            connection = JdbcConnection.getConnection();
            if (connection == null)
                System.out.println("Error getting the connection.");

            preparedStatement = connection.prepareStatement(INSERT_INTO_ACCOUNT);

            for (int i = 0; i < accountList.size(); i++) {
                preparedStatement.setString(1, accountList.get(i).getAccountName());
                preparedStatement.setLong(2, accountList.get(i).getUserId());
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
    public void update(Account account) {
        try {

            connection = JdbcConnection.getConnection();
            if (connection == null)
                System.out.println("Error getting the connection.");

            preparedStatement = connection.prepareStatement(UPDATE_ACCOUNT);

            preparedStatement.setString(1, account.getAccountName());
            preparedStatement.setLong(2, account.getUserId());
            preparedStatement.setLong(3, account.getAccountId());

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
    public void deleteById(Long accountId) {
        try {

            connection = JdbcConnection.getConnection();
            if (connection == null)
                System.out.println("Error getting the connection.");

            preparedStatement = connection.prepareStatement(DELETE_ACCOUNT_BY_ID);
            preparedStatement.setLong(1, accountId);
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
    public Account loadById(Long accountId) {
        Account account = new Account();

        try {

            connection = JdbcConnection.getConnection();
            if (connection == null)
                System.out.println("Error getting the connection.");

            preparedStatement = connection.prepareStatement(SELECT_ACCOUNT_BY_ID);
            preparedStatement.setLong(1, accountId);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                account.setAccountId(resultSet.getLong("account_id"));
                account.setAccountName(resultSet.getString("account_name"));
                account.setUserId(resultSet.getLong("user_id"));
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

        return account;
    }

    @Override
    public List<Account> loadAll() {
        List<Account> accountList = new ArrayList<>();

        try {

            connection = JdbcConnection.getConnection();
            if (connection == null)
                System.out.println("Error getting the connection.");

            preparedStatement = connection.prepareStatement(SELECT_ALL_ACCOUNTS);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Account account = new Account();
                account.setAccountId(resultSet.getLong("account_id"));
                account.setAccountName(resultSet.getString("account_name"));
                account.setUserId(resultSet.getLong("user_id"));
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

        return accountList;
    }
}
