package com.gmail.podkutin.dmitry.voting_system;

import com.gmail.podkutin.dmitry.voting_system.model.AbstractBaseEntity;
import com.gmail.podkutin.dmitry.voting_system.model.restaurant.Dish;

import java.time.LocalDate;

public class DishTestData {
    public static final TestMatcher<Dish> DISH_MATCHER = TestMatcher.usingIgnoringFieldsComparator(Dish.class,"restaurant");
    public static final Integer DISH_ID = AbstractBaseEntity.START_SEQ + 9;
    public static final Integer DISH_ID_NOT_FOUND = 9;

    public static Dish getNewDish() {
        return new Dish("NewDish", LocalDate.of(2020,12,11), 777);
    }

    public static Dish getUpdatedDish() {
        return new Dish(DISH_ID, "UpdatedName", LocalDate.of(2020,7,24), 670);
    }

}
