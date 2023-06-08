package com.example.repository.account;

public class AccountQueries {

    public static final String INSERT_INTO_ACCOUNT = "insert into account(account_name, user_id)" +
            " values(?,?)";

    public static final String UPDATE_ACCOUNT = "update account set account_name=?, user_id=?" +
            " where account_id=?";

    public static final String DELETE_ACCOUNT_BY_ID = "delete from account where account_id=?";

    public static final String SELECT_ACCOUNT_BY_ID = "select * from account where account_id=?";

    public static final String SELECT_ALL_ACCOUNTS = "select * from account";

    public static final String GET_NUMBER_OF_ACCOUNTS = "select count(accounts.account_name)" +
            " as number_of_accounts from account";

}
