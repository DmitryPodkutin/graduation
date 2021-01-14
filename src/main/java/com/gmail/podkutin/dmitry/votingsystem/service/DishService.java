package com.gmail.podkutin.dmitry.votingsystem.service;

import com.gmail.podkutin.dmitry.votingsystem.repository.DishRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import com.gmail.podkutin.dmitry.votingsystem.model.restaurant.Dish;
import com.gmail.podkutin.dmitry.votingsystem.util.exception.NotFoundException;

import java.util.List;

import static com.gmail.podkutin.dmitry.votingsystem.util.ValidationUtil.*;

@Service
public class DishService {
    protected final Logger log = LoggerFactory.getLogger(DishService.class);

    private final DishRepository repository;
    private final RestaurantService restaurantService;

    public DishService(@Autowired DishRepository repository, @Autowired RestaurantService restaurantService) {
        this.repository = repository;
        this.restaurantService = restaurantService;
    }

    public List<Dish> getAllForRestaurant(int restaurantId) {
        log.info("getAll");
        return repository.getAllForRestaurant(restaurantId);
    }

    public Dish get(int id, int restaurantId) {
        log.info("get {}", id);
        return repository.findById(id)
                .filter(dish -> dish.getRestaurant().getId() == restaurantId)
                .orElseThrow(() -> new NotFoundException(" Not found entity with " + id));
    }

    public Dish create(Dish dish, int restaurantId) {
        log.info("create {}", dish);
        checkNew(dish);
        dish.setRestaurant(restaurantService.get(restaurantId, false));
        return repository.save(dish);
    }

    public void update(Dish dish, int id, int restaurantId) {
        Assert.notNull(dish, "dish must not be null");
        assureIdConsistent(dish, id);
        dish.setRestaurant(restaurantService.get(restaurantId, false));
        get(id, restaurantId);
        log.info("update {} with id={}", dish, id);
        repository.save(dish);
    }

    public void delete(int id, int restaurantId) {
        log.info("delete {}", id);
        checkNotFoundWithId(repository.delete(id, restaurantId), id);
    }
}
