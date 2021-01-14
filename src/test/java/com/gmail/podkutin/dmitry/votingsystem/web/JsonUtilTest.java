package com.gmail.podkutin.dmitry.votingsystem.web;

import org.junit.jupiter.api.Test;
import com.gmail.podkutin.dmitry.votingsystem.model.restaurant.Vote;
import com.gmail.podkutin.dmitry.votingsystem.web.json.JsonUtil;

import java.util.List;

import static com.gmail.podkutin.dmitry.votingsystem.VoteTestData.*;

class JsonUtilTest {

    @Test
    void readWriteValue() {
        String json = JsonUtil.writeValue(VOTE_1);
        System.out.println(json);
        Vote vote = JsonUtil.readValue(json, Vote.class);
        System.out.println(vote);
        VOTE_MATCHER.assertMatch(vote, VOTE_1);
    }

    @Test
    void readWriteValues() {
        String json = JsonUtil.writeValue(List.of(VOTE_1, VOTE_2, VOTE_3));
        System.out.println(json);
        List<Vote> meals = JsonUtil.readValues(json, Vote.class);
        VOTE_MATCHER.assertMatch(meals, List.of(VOTE_1, VOTE_2, VOTE_3));
    }
}