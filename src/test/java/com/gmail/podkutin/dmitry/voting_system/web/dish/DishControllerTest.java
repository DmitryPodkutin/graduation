package com.gmail.podkutin.dmitry.voting_system.web.dish;

import com.gmail.podkutin.dmitry.voting_system.AbstractControllerTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.gmail.podkutin.dmitry.voting_system.DishTestData;
import com.gmail.podkutin.dmitry.voting_system.UserTestData;
import com.gmail.podkutin.dmitry.voting_system.model.restaurant.Dish;
import com.gmail.podkutin.dmitry.voting_system.service.DishService;
import com.gmail.podkutin.dmitry.voting_system.util.exception.NotFoundException;
import com.gmail.podkutin.dmitry.voting_system.web.json.JsonUtil;

import java.util.Comparator;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static com.gmail.podkutin.dmitry.voting_system.DishTestData.*;
import static com.gmail.podkutin.dmitry.voting_system.RestaurantTestData.*;
import static com.gmail.podkutin.dmitry.voting_system.TestUtil.readFromJson;
import static com.gmail.podkutin.dmitry.voting_system.TestUtil.userHttpBasic;
import static com.gmail.podkutin.dmitry.voting_system.UserTestData.admin;
import static com.gmail.podkutin.dmitry.voting_system.UserTestData.user;

public class DishControllerTest extends AbstractControllerTest {

    private static final String REST_URL = "/admin/restaurants/100003/dishes/";

    @Autowired
    DishService service;

    @Test
    public void getAllWithRestaurant() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(RESTAURANT_2.getMenu().stream()
                        .sorted(Comparator.comparing(Dish::getDate))
                        .collect(Collectors.toList())));
    }

    @Test
    public void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + DISH_ID)
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(RESTAURANT_2.getMenu().get(1)));
    }

    @Test
    public void getNotAdmin() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + DISH_ID)
                .with(userHttpBasic(user)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    public void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + DISH_ID_NOT_FOUND)
                .with(userHttpBasic(UserTestData.admin)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void create() throws Exception {
        Dish newDish = DishTestData.getNewDish();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newDish)))
                .andDo(print())
                .andExpect(status().isCreated());
        Dish created = readFromJson(action, Dish.class);
        int newId = created.id();
        newDish.setId(newId);
        DISH_MATCHER.assertMatch(created, newDish);
        DISH_MATCHER.assertMatch(service.get(newId, RESTAURANT_2.getId()), newDish);
    }

    @Test
    public void createNotAdmin() throws Exception {
        Dish newDish = DishTestData.getNewDish();
        perform(MockMvcRequestBuilders.post(REST_URL)
                .with(userHttpBasic(UserTestData.user))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newDish)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    public void update() throws Exception {
        Dish updated = DishTestData.getUpdatedDish();
        perform(MockMvcRequestBuilders.put(REST_URL + DISH_ID)
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());
        DISH_MATCHER.assertMatch(service.get(DISH_ID, RESTAURANT_2.getId()), updated);
    }

    @Test
    public void updateNotAdmin() throws Exception {
        Dish updated = DishTestData.getUpdatedDish();
        perform(MockMvcRequestBuilders.put(REST_URL + DISH_ID)
                .with(userHttpBasic(user))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isForbidden());

    }

    @Test
    public void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + DISH_ID)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isNoContent());
        Assertions.assertThrows(NotFoundException.class, () -> service.get(RESTAURANT_2.getId(), DISH_ID));
    }

    @Test
    public void deleteNotAdmin() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + DISH_ID)
                .with(userHttpBasic(user)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    public void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + DISH_ID_NOT_FOUND)
                .with(userHttpBasic(UserTestData.admin)))
                .andExpect(status().isUnprocessableEntity());
    }
}
