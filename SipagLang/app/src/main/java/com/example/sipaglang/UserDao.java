package com.example.sipaglang;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface UserDao {


    @Insert
    void insertUser(User user);

    @Query("SELECT * FROM users WHERE username = :username AND password = :password")
    User login(String username, String password);


    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    User getUserByUsername(String username);


    @Query("UPDATE users SET password = :newPassword WHERE username = :username")
    void updatePassword(String username, String newPassword);


    @Query("SELECT COUNT(*) FROM users WHERE username = :username")
    int checkUsernameExists(String username);


    @Query("SELECT * FROM users WHERE username = :username")
    User getUserDetails(String username);
}
