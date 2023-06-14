package com.example.repository.account;

import com.example.connection.JdbcConnection;
import com.example.entity.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.example.repository.account.AccountQueries.*;

public class AccountRepositoryImpl implements AccountRepository {

    private static final Logger logger = LoggerFactory.getLogger(AccountRepositoryImpl.class);

    private static final String CONNECTION_ERROR = "Error getting the connection.";

    private static final String ACCOUNT_ID = "account_id";
    private static final String USER_ID = "user_id";
    private static final String ACCOUNT_NAME = "account_name";

    private Connection connection = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    @Override
    public void save(Account account) {
        try {

            connection = getConnection();
            if (connection == null)
                logger.info(CONNECTION_ERROR);

            preparedStatement = connection.prepareStatement(INSERT_INTO_ACCOUNT);

            preparedStatement.setString(1, account.getAccountName());
            preparedStatement.setLong(2, account.getUserId());

            preparedStatement.execute();

        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        } finally {
            closeConnection();
            closePreparedStatement();
        }
    }

    @Override
    public long saveAndReturnAccountId(Account account) {
        long accountId = 0;
        try {

            connection = getConnection();
            if (connection == null)
                logger.info(CONNECTION_ERROR);

            preparedStatement = connection.prepareStatement(INSERT_INTO_ACCOUNT,
                    Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, account.getAccountName());
            preparedStatement.setLong(2, account.getUserId());

            resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();
            accountId = resultSet.getLong(ACCOUNT_ID);

        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        } finally {
            closeConnection();
            closePreparedStatement();
            closeResultSet();
        }
        return accountId;
    }

    @Override
    public void saveAll(List<Account> accountList) {
        try {

            connection = getConnection();
            if (connection == null)
                logger.info(CONNECTION_ERROR);

            preparedStatement = connection.prepareStatement(INSERT_INTO_ACCOUNT);

            for (int i = 0; i < accountList.size(); i++) {
                preparedStatement.setString(1, accountList.get(i).getAccountName());
                preparedStatement.setLong(2, accountList.get(i).getUserId());
                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();

        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        } finally {
            closeConnection();
            closePreparedStatement();
        }
    }

    @Override
    public void update(Account account) {
        try {

            connection = getConnection();
            if (connection == null)
                logger.info(CONNECTION_ERROR);

            preparedStatement = connection.prepareStatement(UPDATE_ACCOUNT);

            preparedStatement.setString(1, account.getAccountName());
            preparedStatement.setLong(2, account.getUserId());
            preparedStatement.setLong(3, account.getAccountId());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        } finally {
            closeConnection();
            closePreparedStatement();
        }
    }

    @Override
    public void deleteById(Long accountId) {
        try {

            connection = getConnection();
            if (connection == null)
                logger.info(CONNECTION_ERROR);

            preparedStatement = connection.prepareStatement(DELETE_ACCOUNT_BY_ID);
            preparedStatement.setLong(1, accountId);
            preparedStatement.execute();

        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        } finally {
            closeConnection();
            closePreparedStatement();
        }
    }

    @Override
    public Account loadById(Long accountId) {
        Account account = new Account();

        try {

            connection = getConnection();
            if (connection == null)
                logger.info(CONNECTION_ERROR);

            preparedStatement = connection.prepareStatement(SELECT_ACCOUNT_BY_ID);
            preparedStatement.setLong(1, accountId);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                setAccount(account);
            }

        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        } finally {
            closeConnection();
            closePreparedStatement();
            closeResultSet();
        }

        return account;
    }

    @Override
    public List<Account> loadAll() {
        List<Account> accountList = new ArrayList<>();

        try {

            connection = getConnection();
            if (connection == null)
                logger.info(CONNECTION_ERROR);

            preparedStatement = connection.prepareStatement(SELECT_ALL_ACCOUNTS);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Account account = new Account();
                setAccount(account);
                accountList.add(account);
            }

        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        } finally {
            closeConnection();
            closePreparedStatement();
            closeResultSet();
        }

        return accountList;
    }

    @Override
    public Account[] loadAllUsingArray() {
        Account[] accounts;
        try {

            connection = getConnection();
            if (connection == null)
                logger.info(CONNECTION_ERROR);

            preparedStatement = connection.prepareStatement(SELECT_ALL_ACCOUNTS,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            resultSet = preparedStatement.executeQuery();

            int size = 0;
            resultSet.last();
            size = resultSet.getRow();
            resultSet.beforeFirst();

            accounts = new Account[size];

            int count = 0;

            while (resultSet.next()) {
                Account account = new Account();
                setAccount(account);
                accounts[count++] = account;
            }

        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        } finally {
            closeConnection();
            closePreparedStatement();
            closeResultSet();
        }
        return accounts;
    }

    @Override
    public int getNumberOfAccounts() {
        int numberOfAccounts = 0;

        try {

            connection = getConnection();
            if (connection == null)
                logger.info(CONNECTION_ERROR);

            preparedStatement = connection.prepareStatement(GET_NUMBER_OF_ACCOUNTS);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                numberOfAccounts = resultSet.getInt("number_of_accounts");
                System.out.println("Number of Accounts : " + numberOfAccounts);
            }

        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        } finally {
            closeConnection();
            closePreparedStatement();
            closeResultSet();
        }

        return numberOfAccounts;
    }

    @Override
    public long loadId() {
        long accountId = 0;
        try {

            connection = getConnection();
            if (connection == null)
                logger.info(CONNECTION_ERROR);

            preparedStatement = connection.prepareStatement(SELECT_ALL_ACCOUNTS);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next())
                accountId = resultSet.getLong(ACCOUNT_ID);

        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        } finally {
            closeConnection();
            closePreparedStatement();
            closeResultSet();
        }
        return accountId;
    }

    private void setAccount(Account account) throws SQLException {
        account.setAccountId(resultSet.getLong(ACCOUNT_ID));
        account.setUserId(resultSet.getLong(USER_ID));
        account.setAccountName(resultSet.getString(ACCOUNT_NAME));
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
