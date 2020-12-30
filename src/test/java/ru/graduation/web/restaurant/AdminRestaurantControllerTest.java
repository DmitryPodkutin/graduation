package ru.graduation.web.restaurant;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.graduation.RestaurantTestData;
import ru.graduation.UserTestData;
import ru.graduation.model.restaurant.Restaurant;
import ru.graduation.service.RestaurantService;
import ru.graduation.util.exception.NotFoundException;
import ru.graduation.web.AbstractControllerTest;
import ru.graduation.web.json.JsonUtil;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.graduation.RestaurantTestData.*;
import static ru.graduation.TestUtil.readFromJson;
import static ru.graduation.TestUtil.userHttpBasic;
import static ru.graduation.UserTestData.user;

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
