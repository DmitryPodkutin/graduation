package com.gmail.podkutin.dmitry.voting_system.web.vote;

import com.gmail.podkutin.dmitry.voting_system.AbstractControllerTest;
import org.junit.Assume;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.gmail.podkutin.dmitry.voting_system.TestUtil;
import com.gmail.podkutin.dmitry.voting_system.UserTestData;
import com.gmail.podkutin.dmitry.voting_system.model.restaurant.Vote;
import com.gmail.podkutin.dmitry.voting_system.service.RestaurantService;
import com.gmail.podkutin.dmitry.voting_system.service.VoteService;
import com.gmail.podkutin.dmitry.voting_system.web.json.JsonUtil;

import java.time.LocalTime;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static com.gmail.podkutin.dmitry.voting_system.RestaurantTestData.RESTAURANT_2;
import static com.gmail.podkutin.dmitry.voting_system.RestaurantTestData.RESTAURANT_3;
import static com.gmail.podkutin.dmitry.voting_system.TestUtil.readFromJson;
import static com.gmail.podkutin.dmitry.voting_system.TestUtil.userHttpBasic;
import static com.gmail.podkutin.dmitry.voting_system.VoteTestData.*;
import static com.gmail.podkutin.dmitry.voting_system.util.ValidationUtil.DEADLINE_TIME;

public class ProfileVoteControllerTest extends AbstractControllerTest {

    @Autowired
    VoteService service;

    @Autowired
    RestaurantService restaurantService;

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
        Assume.assumeTrue("Try this test up to 11:00AM", LocalTime.now().isBefore(DEADLINE_TIME));
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
        Assume.assumeTrue("Try this test up to 11:00AM", LocalTime.now().isBefore(DEADLINE_TIME));
        perform(MockMvcRequestBuilders.put(VOTE3_REST_URL_NOT_FOUND).contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(UserTestData.user)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void updateAfterDeadLineTime() throws Exception {
        Assume.assumeTrue("Try this test after 11:00AM", LocalTime.now().isAfter(DEADLINE_TIME));
        Vote updated = getUpdatedVote();
        perform(MockMvcRequestBuilders.put(VOTE3_REST_URL).contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(UserTestData.user))
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isConflict());
    }
}