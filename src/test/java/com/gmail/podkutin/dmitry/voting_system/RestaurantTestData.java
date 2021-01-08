package com.gmail.podkutin.dmitry.voting_system;

import com.gmail.podkutin.dmitry.voting_system.model.AbstractBaseEntity;
import com.gmail.podkutin.dmitry.voting_system.model.restaurant.Dish;
import com.gmail.podkutin.dmitry.voting_system.model.restaurant.Restaurant;

import java.time.LocalDate;
import java.util.List;

public class RestaurantTestData {
    public static final TestMatcher<Restaurant> RESTAURANT_MATCHER = TestMatcher.usingIgnoringFieldsComparator(Restaurant.class);
    public static final TestMatcher<Restaurant> RESTAURANT_MATCHER_WITHOUT_MENU = TestMatcher.usingIgnoringFieldsComparator(Restaurant.class, "menu");
    public static final Integer RESTAURANT_ID = AbstractBaseEntity.START_SEQ + 3;
    public static final Integer RESTAURANT_ID_NOT_FOUND = 5;

    public static final Restaurant RESTAURANT_1 = new Restaurant(100002, "Debasus", List.of(new Dish(100005, "Bear", LocalDate.now(), 120),
            new Dish(100006, "Garlic bread", LocalDate.now(), 670), new Dish(100007, "BBQ ribs", LocalDate.now(), 340)));
    public static final Restaurant RESTAURANT_2 = new Restaurant(100003, "Colonies",
            List.of(new Dish(100008, "Tea", LocalDate.now(), 60), new Dish(100009, "Pizza", LocalDate.now(), 500), new Dish(100010, "Bacon and eggs", LocalDate.now(), 70)));
    public static final Restaurant RESTAURANT_3 = new Restaurant(100004, "The Lounge Cafe",
            List.of(new Dish(100011, "Fresh juice", LocalDate.now(), 160), new Dish(100012, "Tomato soup", LocalDate.now(), 920), new Dish(100013, "Pasta Carbonara", LocalDate.now(), 270)));
    public static final Restaurant RESTAURANT_2_ORDERED = new Restaurant(100003, "Colonies",
            List.of(new Dish(100010, "Bacon and eggs", LocalDate.now(), 70), new Dish(100009, "Pizza", LocalDate.now(), 500), new Dish(100008, "Tea", LocalDate.now(), 60)));
    public static final Restaurant RESTAURANT_2_WITHOUT_MENU = new Restaurant(100003, "Colonies");

    public static Restaurant getUpdatedRestaurant() {
        return new Restaurant(RESTAURANT_ID, "UpdatedName");
    }

    public static Restaurant getNewRestaurant() {
        return new Restaurant("NewRestaurant");
    }

    public static List<Restaurant> getRestaurants() {
        return List.of(RESTAURANT_2, RESTAURANT_1, RESTAURANT_3);
    }

    public static List<Restaurant> getRestaurantsWithoutMenu() {
        return List.of(new Restaurant(100003, "Colonies"), new Restaurant(100002, "Debasus"), new Restaurant(100004, "The Lounge Cafe"));
    }
}

