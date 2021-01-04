package com.gmail.podkutin.dmitry.voting_system.web.restaurant;

import com.gmail.podkutin.dmitry.voting_system.web.AbstractControllerTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.gmail.podkutin.dmitry.voting_system.RestaurantTestData;
import com.gmail.podkutin.dmitry.voting_system.UserTestData;
import com.gmail.podkutin.dmitry.voting_system.model.restaurant.Restaurant;
import com.gmail.podkutin.dmitry.voting_system.service.RestaurantService;
import com.gmail.podkutin.dmitry.voting_system.util.exception.NotFoundException;
import com.gmail.podkutin.dmitry.voting_system.web.json.JsonUtil;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static com.gmail.podkutin.dmitry.voting_system.RestaurantTestData.*;
import static com.gmail.podkutin.dmitry.voting_system.TestUtil.readFromJson;
import static com.gmail.podkutin.dmitry.voting_system.TestUtil.userHttpBasic;
import static com.gmail.podkutin.dmitry.voting_system.UserTestData.user;

public class AdminRestaurantControllerTest extends AbstractControllerTest {

    private static final String ADMIN_REST_URL = AdminRestaurantController.REST_URL + '/';
    private static final String PROFILE_REST_URL = ProfileRestaurantController.REST_URL + '/';

    @Autowired
    RestaurantService service;

    @Test
    public void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(PROFILE_REST_URL)
                .with(userHttpBasic(UserTestData.admin)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(RESTAURANT_MATCHER.contentJson(RESTAURANT_2, RESTAURANT_1, RESTAURANT_3));
    }

    @Test
    public void get() throws Exception {
        perform(MockMvcRequestBuilders.get(PROFILE_REST_URL + RESTAURANT_ID)
                .with(userHttpBasic(UserTestData.admin)))
                .andExpect(status().isOk())
                .andDo(print())
                // https://jira.spring.io/browse/SPR-14472
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(RESTAURANT_2_ORDERED));
    }

    @Test
    public void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(PROFILE_REST_URL + RESTAURANT_ID_NOT_FOUND)
                .with(userHttpBasic(UserTestData.admin)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void create() throws Exception {
        Restaurant newRestaurant = RestaurantTestData.getNewRestaurant();
        ResultActions action = perform(MockMvcRequestBuilders.post(ADMIN_REST_URL)
                .with(userHttpBasic(UserTestData.admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newRestaurant)))
                .andDo(print())
                .andExpect(status().isCreated());

        Restaurant created = readFromJson(action, Restaurant.class);
        int newId = created.id();
        newRestaurant.setId(newId);
        RESTAURANT_MATCHER.assertMatch(created, newRestaurant);
        RESTAURANT_MATCHER.assertMatch(service.get(newId), newRestaurant);
    }

    @Test
    public void createNotAdmin() throws Exception {
        Restaurant newRestaurant = RestaurantTestData.getNewRestaurant();
        perform(MockMvcRequestBuilders.post(ADMIN_REST_URL)
                .with(userHttpBasic(user))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newRestaurant)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    public void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(ADMIN_REST_URL + RESTAURANT_ID)
                .with(userHttpBasic(UserTestData.admin)))
                .andDo(print())
                .andExpect(status().isNoContent());
        Assertions.assertThrows(NotFoundException.class, () -> service.get(RESTAURANT_ID));
    }

    @Test
    public void deleteNotAdmin() throws Exception {
        perform(MockMvcRequestBuilders.delete(ADMIN_REST_URL + RESTAURANT_ID)
                .with(userHttpBasic(user)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    public void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(ADMIN_REST_URL + RESTAURANT_ID_NOT_FOUND)
                .with(userHttpBasic(UserTestData.admin)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void update() throws Exception {
        Restaurant updated = RestaurantTestData.getUpdatedRestaurant();
        perform(MockMvcRequestBuilders.put(ADMIN_REST_URL + RESTAURANT_ID)
                .with(userHttpBasic(UserTestData.admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());
        RESTAURANT_MATCHER.assertMatch(service.get(updated.getId()), updated);
    }

    @Test
    public void updateNotAdmin() throws Exception {
        Restaurant updated = RestaurantTestData.getUpdatedRestaurant();
        perform(MockMvcRequestBuilders.put(ADMIN_REST_URL + RESTAURANT_ID)
                .with(userHttpBasic(user))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isForbidden());
    }

}
