package com.gmail.podkutin.dmitry.votingsystem.web.vote;

import com.gmail.podkutin.dmitry.votingsystem.AbstractControllerTest;
import com.gmail.podkutin.dmitry.votingsystem.TestUtil;
import com.gmail.podkutin.dmitry.votingsystem.UserTestData;
import com.gmail.podkutin.dmitry.votingsystem.model.restaurant.Vote;
import com.gmail.podkutin.dmitry.votingsystem.service.VoteService;
import com.gmail.podkutin.dmitry.votingsystem.util.TimeUtil;
import com.gmail.podkutin.dmitry.votingsystem.web.json.JsonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import static com.gmail.podkutin.dmitry.votingsystem.RestaurantTestData.RESTAURANT_2;
import static com.gmail.podkutin.dmitry.votingsystem.RestaurantTestData.RESTAURANT_3;
import static com.gmail.podkutin.dmitry.votingsystem.TestUtil.readFromJson;
import static com.gmail.podkutin.dmitry.votingsystem.TestUtil.userHttpBasic;
import static com.gmail.podkutin.dmitry.votingsystem.VoteTestData.*;
import static com.gmail.podkutin.dmitry.votingsystem.util.TimeUtil.setClock;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProfileVoteControllerTest extends AbstractControllerTest {

    @Autowired
    private VoteService service;

    @Test
    public void get() throws Exception {
        perform(MockMvcRequestBuilders.get("/profile/votes/")
                .with(userHttpBasic(UserTestData.user)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(VOTE_4)).andReturn();
    }

    @Test
    public void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get("/profile/votes/").param("date", "2020-01-01")
                .with(userHttpBasic(UserTestData.user)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print());
    }

    @Test
    public void create() throws Exception {
        ResultActions action = perform(MockMvcRequestBuilders.post("/profile/restaurants/100003/votes")
                .with(userHttpBasic(UserTestData.user)))
                .andDo(print())
                .andExpect(status().isCreated());
        Vote created = readFromJson(action, Vote.class);
        final int id = created.id();
        Vote newVote = getNewUserVote();
        newVote.setId(id);
        VOTE_MATCHER.assertMatch(created, newVote);

        TestUtil.mockAuthorize(UserTestData.user);
        Vote taken = service.get(id);
        if (taken.getRestaurant().id() == (RESTAURANT_2.id())) {
            taken.setRestaurant(RESTAURANT_2);
        }
        VOTE_MATCHER.assertMatch(taken, newVote);
    }

    @Test
    void update() throws Exception {
        setClockBeforeDeadLine();
        perform(MockMvcRequestBuilders.put(VOTE3_REST_URL).contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(UserTestData.user)))
                .andDo(print())
                .andExpect(status().isNoContent());
        TestUtil.mockAuthorize(UserTestData.user);
        Vote taken = service.get(VOTE_3.id());
        if (taken.getRestaurant().id() == (RESTAURANT_3.id())) {
            taken.setRestaurant(RESTAURANT_3);
            VOTE_MATCHER.assertMatch(taken, getUpdatedVote());
        }
    }

    @Test
    void updateNotFound() throws Exception {
        setClockBeforeDeadLine();
        perform(MockMvcRequestBuilders.put(VOTE3_REST_URL_NOT_FOUND).contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(UserTestData.user)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void updateAfterDeadLineTime() throws Exception {
        setClock(Clock.fixed(
                Instant.parse("2020-08-22T12:00:00Z"),
                ZoneOffset.UTC));
        Vote updated = getUpdatedVote();
        perform(MockMvcRequestBuilders.put(VOTE3_REST_URL).contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(UserTestData.user))
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isConflict());
    }

    private void setClockBeforeDeadLine() {
        setClock(TimeUtil.clock = Clock.fixed(
                Instant.parse("2020-08-22T10:00:00Z"),
                ZoneOffset.UTC));
    }

}