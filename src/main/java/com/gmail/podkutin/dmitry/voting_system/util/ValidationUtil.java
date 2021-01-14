package com.gmail.podkutin.dmitry.voting_system.util;

import com.gmail.podkutin.dmitry.voting_system.model.AbstractBaseEntity;
import com.gmail.podkutin.dmitry.voting_system.util.exception.NotFoundException;
import com.gmail.podkutin.dmitry.voting_system.util.exception.VotingException;

import java.time.LocalTime;

public class ValidationUtil {

    public static final LocalTime DEADLINE_TIME = LocalTime.of(11, 0);

    private ValidationUtil() {
    }

    public static void checkNotFoundWithId(int found, int id) {
        checkNotFound(found != 0, "id=" + id);
    }

    public static void checkNotFound(boolean found, String msg) {
        if (!found) {
            throw new NotFoundException("Not found entity with " + msg);
        }
    }

    public static void checkNew(AbstractBaseEntity entity) {
        if (!entity.isNew()) {
            throw new IllegalArgumentException(entity + " must be new (id=null)");
        }
    }

    public static void assureIdConsistent(AbstractBaseEntity entity, int id) {
//      conservative when you reply, but accept liberally (http://stackoverflow.com/a/32728226/548473)
        if (entity.isNew()) {
            entity.setId(id);
        } else if (entity.id() != id) {
            throw new IllegalArgumentException(entity + " must be with id=" + id);
        }
    }

    //  http://stackoverflow.com/a/28565320/548473
    public static Throwable getRootCause(Throwable t) {
        Throwable result = t;
        Throwable cause;

        while (null != (cause = result.getCause()) && (result != cause)) {
            result = cause;
        }
        return result;
    }

    public static void checkTimeForDedLine() {
        if (LocalTime.now().isAfter(DEADLINE_TIME)) {
            throw new VotingException("It's too late to change vote");
        }
    }
}