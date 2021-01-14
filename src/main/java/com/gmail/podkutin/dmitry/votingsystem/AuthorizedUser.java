package com.gmail.podkutin.dmitry.votingsystem;


import com.gmail.podkutin.dmitry.votingsystem.model.user.User;
import com.gmail.podkutin.dmitry.votingsystem.to.UserTo;
import com.gmail.podkutin.dmitry.votingsystem.util.UserUtil;

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