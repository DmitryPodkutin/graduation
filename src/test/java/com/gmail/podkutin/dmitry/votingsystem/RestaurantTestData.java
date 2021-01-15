package com.gmail.podkutin.dmitry.votingsystem;

import com.gmail.podkutin.dmitry.votingsystem.model.AbstractBaseEntity;
import com.gmail.podkutin.dmitry.votingsystem.model.restaurant.Dish;
import com.gmail.podkutin.dmitry.votingsystem.model.restaurant.Restaurant;

import java.time.LocalDate;
import java.util.List;

public class RestaurantTestData {
    public static final TestMatcher<Restaurant> RESTAURANT_MATCHER = TestMatcher.usingIgnoringFieldsComparator(Restaurant.class);
    public static final TestMatcher<Restaurant> RESTAURANT_MATCHER_WITHOUT_MENU = TestMatcher.usingIgnoringFieldsComparator(Restaurant.class, "menu");
    public static final Integer RESTAURANT_ID = AbstractBaseEntity.START_SEQ + 3;
    public static final Integer RESTAURANT_ID_NOT_FOUND = 5;

    public static final Restaurant RESTAURANT_1 = new Restaurant(100002, "Debasus", List.of(new Dish(100005, "Bear", LocalDate.now(), 12000),
            new Dish(100006, "Garlic bread", LocalDate.now(), 67000), new Dish(100007, "BBQ ribs", LocalDate.now(), 34000)));
    public static final Restaurant RESTAURANT_2 = new Restaurant(100003, "Colonies",
            List.of(new Dish(100008, "Tea", LocalDate.now(), 6000), new Dish(100009, "Pizza", LocalDate.now(), 45000), new Dish(100010, "Bacon and eggs", LocalDate.now(), 7000)));
    public static final Restaurant RESTAURANT_3 = new Restaurant(100004, "The Lounge Cafe",
            List.of(new Dish(100011, "Fresh juice", LocalDate.now(), 16000), new Dish(100012, "Tomato soup", LocalDate.now(), 92000), new Dish(100013, "Pasta Carbonara", LocalDate.now(), 27000)));
    public static final Restaurant RESTAURANT_2_ORDERED = new Restaurant(100003, "Colonies",
            List.of(new Dish(100010, "Bacon and eggs", LocalDate.now(), 7000), new Dish(100009, "Pizza", LocalDate.now(), 45000), new Dish(100008, "Tea", LocalDate.now(), 6000)));
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

