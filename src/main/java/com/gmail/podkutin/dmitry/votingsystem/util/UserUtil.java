package com.gmail.podkutin.dmitry.votingsystem.util;

import com.gmail.podkutin.dmitry.votingsystem.model.user.User;
import com.gmail.podkutin.dmitry.votingsystem.to.UserTo;

public class UserUtil {

    public static UserTo asTo(User user) {
        return new UserTo(user.getId(), user.getName(), user.getEmail(), user.getPassword());
    }
}