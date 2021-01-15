package com.gmail.podkutin.dmitry.votingsystem.web.restaurant;

import com.gmail.podkutin.dmitry.votingsystem.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.gmail.podkutin.dmitry.votingsystem.UserTestData;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static com.gmail.podkutin.dmitry.votingsystem.RestaurantTestData.*;
import static com.gmail.podkutin.dmitry.votingsystem.TestUtil.userHttpBasic;

public class ProfileRestaurantControllerTest extends AbstractControllerTest {

    private static final String REST_URL = ProfileRestaurantController.REST_URL + '/';

    @Autowired
    private ProfileRestaurantController restController;

    @Test
    public void getWithMenuDay() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RESTAURANT_ID)
                .with(userHttpBasic(UserTestData.user)))
                .andExpect(status().isOk())
                .andDo(print())
                // https://jira.spring.io/browse/SPR-14472
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(RESTAURANT_2_ORDERED));
    }

    @Test
    public void getWithoutMenuDay() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RESTAURANT_ID).param("withMenu", "false")
                .with(userHttpBasic(UserTestData.user)))
                .andExpect(status().isOk())
                .andDo(print())
                // https://jira.spring.io/browse/SPR-14472
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(RESTAURANT_2_WITHOUT_MENU));
    }

    @Test
    public void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RESTAURANT_ID_NOT_FOUND)
                .with(userHttpBasic(UserTestData.user)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void getAllWithMenuDay() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(UserTestData.user)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(getRestaurants()));
    }

    @Test
    void getAllWithoutMenuDay() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL).param("withMenu", "false")
                .with(userHttpBasic(UserTestData.user)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(getRestaurantsWithoutMenu()));
    }
}
