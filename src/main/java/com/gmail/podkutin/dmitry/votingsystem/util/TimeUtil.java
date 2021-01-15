package com.gmail.podkutin.dmitry.votingsystem.util;

import com.gmail.podkutin.dmitry.votingsystem.util.exception.VotingException;

import java.time.Clock;
import java.time.LocalTime;

public class TimeUtil {

    private static final LocalTime DEADLINE_TIME = LocalTime.of(11, 0);
    public static Clock clock = Clock.systemDefaultZone();

    public static void checkTimeForDedLine() {
        if (LocalTime.now(clock).isAfter(DEADLINE_TIME)) {
            throw new VotingException("It's too late to change vote");
        }
    }

    public static void setClock(Clock clock) {
        TimeUtil.clock = clock;
    }
}
