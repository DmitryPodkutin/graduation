package com.gmail.podkutin.dmitry.votingsystem.util;

import com.gmail.podkutin.dmitry.votingsystem.model.AbstractBaseEntity;
import com.gmail.podkutin.dmitry.votingsystem.util.exception.NotFoundException;

import java.util.Optional;

public class ValidationUtil {

    private ValidationUtil() {
    }

    public static <T> T checkNotFoundWithEntity(Optional<T> entity, String message) {
        return entity.orElseThrow(() -> new NotFoundException(message));
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
}