package com.gmail.podkutin.dmitry.voting_system.service;

import com.gmail.podkutin.dmitry.voting_system.repository.RestaurantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import com.gmail.podkutin.dmitry.voting_system.model.restaurant.Restaurant;
import com.gmail.podkutin.dmitry.voting_system.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;

import static com.gmail.podkutin.dmitry.voting_system.util.ValidationUtil.*;

@Service
public class RestaurantService {
    protected final Logger log = LoggerFactory.getLogger(RestaurantService.class);
    private final RestaurantRepository repository;

    public RestaurantService(@Autowired RestaurantRepository repository) {
        this.repository = repository;
    }

    public List<Restaurant> getAll() {
        log.info("getAll");
        return repository.getAll();
    }

    public Restaurant get(int id) {
        log.info("get {}", id);
        return repository.findById(id).orElseThrow(() -> new NotFoundException(" Not found entity with " + id));
    }

    public Restaurant create(Restaurant restaurant) {
        log.info("create {}", restaurant);
        checkNew(restaurant);
        return repository.save(restaurant);
    }

    public void update(Restaurant restaurant, int id) {
        log.info("update {} with id={}", restaurant, id);
        assureIdConsistent(restaurant, id);
        Assert.notNull(restaurant, "restaurant must not be null");
        repository.save(restaurant);
    }

    public void delete(int id) {
        log.info("delete {}", id);
        checkNotFoundWithId(repository.delete(id), id);
    }

    public List<Restaurant> getAllWithMenuDay(LocalDate date) {
        return repository.getAllWithMenuDay(date);
    }

    public Restaurant getWithMenuDay(int id, LocalDate date) {
        return repository.getWithMenuDay(id,date).orElseThrow(() -> new NotFoundException(" Not found entity with " + id));
    }
}
