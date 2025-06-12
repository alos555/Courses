package com.example.courses.data;

import com.example.courses.data.model.Result;
import com.example.courses.data.model.LoggedInUser;

import java.io.IOException;

public class LoginDataSource {

    public Result<LoggedInUser> login(String username, String password) {
        try {
            // Фейковый пользователь
            LoggedInUser fakeUser = new LoggedInUser(
                    java.util.UUID.randomUUID().toString(),
                    "Jane Doe"
            );
            return Result.success(fakeUser); // ✅
        } catch (Exception e) {
            return Result.error(new IOException("Error logging in", e)); // ✅
        }
    }

    public void logout() {
        // TODO: реализуй выход
    }
}