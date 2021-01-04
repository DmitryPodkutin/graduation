package com.gmail.podkutin.dmitry.voting_system;

import com.gmail.podkutin.dmitry.voting_system.model.AbstractBaseEntity;
import com.gmail.podkutin.dmitry.voting_system.model.user.Role;
import com.gmail.podkutin.dmitry.voting_system.model.user.User;

public class UserTestData {

    public static final int USER_ID = AbstractBaseEntity.START_SEQ;
    public static final int ADMIN_ID = AbstractBaseEntity.START_SEQ + 1;

    public static final User user = new User(USER_ID, "User", "user@yandex.ru", "password", Role.USER);
    public static final User admin = new User(ADMIN_ID, "Admin", "admin@gmail.com", "admin", Role.ADMIN);
}
