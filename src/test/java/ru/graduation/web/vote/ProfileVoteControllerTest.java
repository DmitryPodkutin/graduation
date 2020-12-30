package ru.graduation.web.vote;

import org.junit.Assume;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.graduation.TestUtil;
import ru.graduation.UserTestData;
import ru.graduation.model.restaurant.Vote;
import ru.graduation.service.RestaurantService;
import ru.graduation.service.VoteService;
import ru.graduation.web.AbstractControllerTest;
import ru.graduation.web.json.JsonUtil;

import java.time.LocalTime;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.graduation.RestaurantTestData.RESTAURANT_2;
import static ru.graduation.RestaurantTestData.RESTAURANT_3;
import static ru.graduation.TestUtil.readFromJson;
import static ru.graduation.TestUtil.userHttpBasic;
import static ru.graduation.VoteTestData.*;
import static ru.graduation.util.ValidationUtil.DEADLINE_TIME;

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
        Vote newVote = getNewUserVote();
        ResultActions action = perform(MockMvcRequestBuilders.post("/profile/restaurants/100003/votes")
                .with(userHttpBasic(UserTestData.user))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newVote)))
                .andDo(print())
                .andExpect(status().isCreated());
        Vote created = readFromJson(action, Vote.class);
        final int id = created.id();
        newVote.setId(id);
        VOTE_MATCHER.assertMatch(created, newVote);

        TestUtil.mockAuthorize(UserTestData.user);
        TestUtil.userAuth(UserTestData.user);
        Vote taken = service.get(id);
        if (taken.getRestaurant().id() == (RESTAURANT_2.id())) {
            taken.setRestaurant(RESTAURANT_2);
        }
        VOTE_MATCHER.assertMatch(taken, newVote);
    }

    @Test
    void update() throws Exception {
        Assume.assumeTrue("Try this test up to 11:00AM",LocalTime.now().isBefore(DEADLINE_TIME));
        Vote updated = getUpdatedVote();
        perform(MockMvcRequestBuilders.put(VOTE3_REST_URL).contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(UserTestData.user))
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isNoContent());
        TestUtil.mockAuthorize(UserTestData.user);
        Vote taken = service.get(VOTE_3.id());
        if (taken.getRestaurant().id() == (RESTAURANT_3.id())) {
            taken.setRestaurant(RESTAURANT_3);
//            }
            VOTE_MATCHER.assertMatch(taken, updated);
        }
    }

    @Test
    public void updateAfterDeadLineTime() throws Exception {
        Assume.assumeTrue("Try this test after 11:00AM",LocalTime.now().isAfter(DEADLINE_TIME));
        Vote updated = getUpdatedVote();
        perform(MockMvcRequestBuilders.put(VOTE3_REST_URL).contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(UserTestData.user))
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isConflict());
    }
}
