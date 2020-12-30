package ru.graduation;

import ru.graduation.model.user.Role;
import ru.graduation.model.user.User;

import static ru.graduation.model.AbstractBaseEntity.START_SEQ;


public class UserTestData {

    public static final int USER_ID = START_SEQ;
    public static final int ADMIN_ID = START_SEQ + 1;

    public static final User user = new User(USER_ID, "User", "user@yandex.ru", "password", Role.USER);
    public static final User admin = new User(ADMIN_ID, "Admin", "admin@gmail.com", "admin", Role.ADMIN);
}
