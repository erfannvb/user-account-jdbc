package com.example.repository.user;

public class UserQueries {

    public static final String INSERT_INTO_USER = "insert into users(first_name, last_name" +
            ", user_role, username, gender, age) values (?,?,?,?,?,?)";

    public static final String UPDATE_USER = "update users set first_name=?, last_name=?," +
            " user_role=?, username=?, gender=?, age=? where user_id=?";

    public static final String DELETE_USER_BY_ID = "delete from users where user_id=?";

    public static final String SELECT_USER_BY_ID = "select * from users where user_id=?";

    public static final String SELECT_ALL = "select * from users";

}
