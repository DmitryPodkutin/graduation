package com.gmail.podkutin.dmitry.voting_system.service;

import com.gmail.podkutin.dmitry.voting_system.AuthorizedUser;
import com.gmail.podkutin.dmitry.voting_system.repository.RestaurantRepository;
import com.gmail.podkutin.dmitry.voting_system.repository.VoteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import com.gmail.podkutin.dmitry.voting_system.model.restaurant.Vote;
import com.gmail.podkutin.dmitry.voting_system.util.exception.NotFoundException;
import com.gmail.podkutin.dmitry.voting_system.web.SecurityUtil;

import java.time.LocalDate;
import java.util.List;

import static com.gmail.podkutin.dmitry.voting_system.util.ValidationUtil.*;

@Service
public class VoteService {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final VoteRepository voteRepository;
    private final RestaurantRepository restaurantRepository;

    public VoteService(@Autowired VoteRepository voteRepository, @Autowired RestaurantRepository restaurantRepository) {
        this.voteRepository = voteRepository;
        this.restaurantRepository = restaurantRepository;
    }

    public Vote get(int id) {
        log.info("get {}", id);
        return voteRepository.findById(id)
                .filter(vote -> vote.getUser().getId() == SecurityUtil.authUserId())
                .orElseThrow(() -> new NotFoundException(" Not found entity with " + id));
    }

    public List<Vote> getAllForRestaurant(int restaurantId) {
        log.info("getAll");
        return voteRepository.getAllForRestaurant(restaurantId);
    }

    public Vote getForeDate(LocalDate date) {
        int userId = SecurityUtil.authUserId();
        log.info("getForeDate date{},UserId: {}", date, userId);
        return voteRepository.getForeDate(date, userId)
                .orElseThrow(() -> new NotFoundException(" The user's (" + userId + ") voice was not found "));
    }

    public Vote create(int restaurantId, AuthorizedUser authUser) {
        Vote vote = new Vote(authUser.getUser(), restaurantRepository.getOne(restaurantId));
        checkNew(vote);
        log.info("create {}", vote);
        return voteRepository.save(vote);
    }

    public void update(int id, int restaurantId, AuthorizedUser authUser) {
        Vote vote = get(id);
        vote.setRestaurant(restaurantRepository.getOne(restaurantId));
        vote.setUser(authUser.getUser());
        Assert.notNull(vote, "vote must not be null");
        assureIdConsistent(vote, id);
        checkTimeForDedLine();
        log.info("update {} with id={}", vote, id);
        voteRepository.save(vote);
    }
}
