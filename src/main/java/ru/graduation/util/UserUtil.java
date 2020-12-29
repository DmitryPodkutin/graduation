package ru.graduation.util;

import ru.graduation.model.user.User;
import ru.graduation.to.UserTo;

public class UserUtil {

    public static UserTo asTo(User user) {
        return new UserTo(user.getId(), user.getName(), user.getEmail(), user.getPassword());
    }
}