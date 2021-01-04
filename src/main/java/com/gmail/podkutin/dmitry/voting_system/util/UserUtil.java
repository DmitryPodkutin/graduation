package com.gmail.podkutin.dmitry.voting_system.util;

import com.gmail.podkutin.dmitry.voting_system.model.user.User;
import com.gmail.podkutin.dmitry.voting_system.to.UserTo;

public class UserUtil {

    public static UserTo asTo(User user) {
        return new UserTo(user.getId(), user.getName(), user.getEmail(), user.getPassword());
    }
}