package com.gmail.podkutin.dmitry.voting_system;

import com.gmail.podkutin.dmitry.voting_system.model.restaurant.Vote;

import java.time.LocalDate;

public class VoteTestData {
    public static final TestMatcher<Vote> VOTE_MATCHER = TestMatcher.usingIgnoringFieldsComparator(Vote.class, "user", "restaurant.menu");
    public static final String VOTE3_REST_URL = "/profile/restaurants/100004/votes/100017";
    public static final Vote VOTE_1 = new Vote(100014, UserTestData.user, RestaurantTestData.RESTAURANT_3);
    public static final Vote VOTE_2 = new Vote(100015, UserTestData.admin, RestaurantTestData.RESTAURANT_3);
    public static final Vote VOTE_3 = new Vote(100017, UserTestData.user, RestaurantTestData.RESTAURANT_3);
    public static final Vote VOTE_4 = new Vote(100018, UserTestData.user, RestaurantTestData.RESTAURANT_1);

    public static Vote getNewUserVote() {
        return new Vote(UserTestData.user, RestaurantTestData.RESTAURANT_2);
    }

    public static Vote getUpdatedVote() {
        return new Vote(100017, UserTestData.user, RestaurantTestData.RESTAURANT_3, LocalDate.of(2020,11,9));
    }
}
