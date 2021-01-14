package com.gmail.podkutin.dmitry.votingsystem.web.restaurant;

import com.gmail.podkutin.dmitry.votingsystem.AbstractControllerTest;
import com.gmail.podkutin.dmitry.votingsystem.RestaurantTestData;
import com.gmail.podkutin.dmitry.votingsystem.UserTestData;
import com.gmail.podkutin.dmitry.votingsystem.model.restaurant.Restaurant;
import com.gmail.podkutin.dmitry.votingsystem.service.RestaurantService;
import com.gmail.podkutin.dmitry.votingsystem.util.exception.NotFoundException;
import com.gmail.podkutin.dmitry.votingsystem.web.json.JsonUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.gmail.podkutin.dmitry.votingsystem.RestaurantTestData.*;
import static com.gmail.podkutin.dmitry.votingsystem.TestUtil.readFromJson;
import static com.gmail.podkutin.dmitry.votingsystem.TestUtil.userHttpBasic;
import static com.gmail.podkutin.dmitry.votingsystem.UserTestData.user;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AdminRestaurantControllerTest extends AbstractControllerTest {

    private static final String ADMIN_REST_URL = AdminRestaurantController.REST_URL + '/';

    @Autowired
    RestaurantService service;

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
        RESTAURANT_MATCHER.assertMatch(service.get(newId, false), newRestaurant);
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
        Assertions.assertThrows(NotFoundException.class, () -> service.get(RESTAURANT_ID, true));
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
        RESTAURANT_MATCHER.assertMatch(service.get(updated.getId(), true), updated);
    }

    @Test
    public void updateNotFound() throws Exception {
        Restaurant updated = RestaurantTestData.getNewRestaurant();
        perform(MockMvcRequestBuilders.put(ADMIN_REST_URL + RESTAURANT_ID_NOT_FOUND)
                .with(userHttpBasic(UserTestData.admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isUnprocessableEntity());
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
