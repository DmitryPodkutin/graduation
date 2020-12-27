package ru.graduation;


import ru.graduation.model.user.User;
import ru.graduation.to.UserTo;
import ru.graduation.util.UserUtil;

public class AuthorizedUser extends org.springframework.security.core.userdetails.User {
    private static final long serialVersionUID = 1L;

    private  UserTo userTo;
    private final User user;

    public AuthorizedUser(User user) {
        super(user.getEmail(), user.getPassword(), user.isEnabled(), true, true, true, user.getRoles());
        this.userTo = UserUtil.asTo(user);
        this.user = user;
    }

    public int getId() {
        return userTo.id();
    }

    public void update(UserTo newTo) {
        userTo = newTo;
    }

    public UserTo getUserTo() {
        return userTo;
    }

    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return userTo.toString();
    }
}