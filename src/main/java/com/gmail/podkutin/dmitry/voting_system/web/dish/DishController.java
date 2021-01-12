package com.gmail.podkutin.dmitry.voting_system.web.dish;

import com.gmail.podkutin.dmitry.voting_system.model.restaurant.Dish;
import com.gmail.podkutin.dmitry.voting_system.service.DishService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = DishController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "Controller_For_Admin", value = "Requests used by the admins")

public class DishController {
    static final String REST_URL = "/admin/restaurants/{restaurantId}/dishes";
    private final DishService service;

    public DishController(@Autowired DishService service) {
        this.service = service;
    }

    @GetMapping()
    @ApiOperation(value = "GET_ALL Restaurant Dishes")
    public List<Dish> getAllForRestaurant(@PathVariable int restaurantId) {
        return service.getAllForRestaurant(restaurantId);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "GET One Restaurant Dish by ID")
    public Dish get(@PathVariable int id, @PathVariable int restaurantId) {
        return service.get(id, restaurantId);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "CREATE new Dish for Restaurant")
    public ResponseEntity<Dish> create(@Valid @RequestBody Dish dish, @PathVariable int restaurantId) {
        Dish created = service.create(dish, restaurantId);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(restaurantId, created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @ApiOperation(value = "UPDATE Restaurant Dish by ID")
    public void update(@Valid @RequestBody Dish dish, @PathVariable int id, @PathVariable int restaurantId) {
        service.update(dish, id, restaurantId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "DELETE Restaurant Dish by ID")
    public void delete(@PathVariable int id, @PathVariable int restaurantId) {
        service.delete(id, restaurantId);
    }
}
