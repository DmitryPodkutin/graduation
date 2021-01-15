package com.gmail.podkutin.dmitry.votingsystem.service;

import com.gmail.podkutin.dmitry.votingsystem.model.restaurant.Vote;
import com.gmail.podkutin.dmitry.votingsystem.model.user.User;
import com.gmail.podkutin.dmitry.votingsystem.repository.VoteRepository;
import com.gmail.podkutin.dmitry.votingsystem.web.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDate;

import static com.gmail.podkutin.dmitry.votingsystem.util.ValidationUtil.*;

@Service
public class VoteService {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final VoteRepository voteRepository;
    private final RestaurantService restaurantService;

    public VoteService(@Autowired VoteRepository voteRepository, @Autowired RestaurantService restaurantService) {
        this.voteRepository = voteRepository;
        this.restaurantService = restaurantService;
    }

    public Vote get(int id) {
        log.info("get {}", id);
        return checkNotFoundWithEntity(voteRepository.findById(id)
                .filter(vote -> vote.getUser().getId() == SecurityUtil.authUserId()), " Not found entity with " + id);

    }

    public Vote getForeDate(LocalDate date) {
        int userId = SecurityUtil.authUserId();
        log.info("getForeDate date{},UserId: {}", date, userId);
        return checkNotFoundWithEntity(voteRepository.getForeDate(date, userId), " The user's (" + userId + ") voice was not found ");
    }

    @Transactional
    public Vote create(int restaurantId, User user) {
        Vote vote = new Vote(user, restaurantService.get(restaurantId, false));
        checkNew(vote);
        log.info("create {}", vote);
        return voteRepository.save(vote);
    }

    @Transactional
    public void update(int id, int restaurantId, User user) {
        checkTimeForDedLine();
        Vote vote = get(id);
        vote.setRestaurant(restaurantService.get(restaurantId, false));
        vote.setUser(user);
        Assert.notNull(vote, "vote must not be null");
        assureIdConsistent(vote, id);
        log.info("update {} with id={}", vote, id);
        voteRepository.save(vote);
    }
}
