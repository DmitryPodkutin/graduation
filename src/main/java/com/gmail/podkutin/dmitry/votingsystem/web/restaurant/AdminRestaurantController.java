package com.gmail.podkutin.dmitry.votingsystem.web.restaurant;

import com.gmail.podkutin.dmitry.votingsystem.model.restaurant.Restaurant;
import com.gmail.podkutin.dmitry.votingsystem.service.RestaurantService;
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

import static com.gmail.podkutin.dmitry.votingsystem.util.ValidationUtil.assureIdConsistent;
import static com.gmail.podkutin.dmitry.votingsystem.util.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = AdminRestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "Admin_Restaurant_Controller", description = "Restaurant item management")
public class AdminRestaurantController {

    private final RestaurantService service;

    public AdminRestaurantController(@Autowired RestaurantService service) {
        this.service = service;
    }

    static final String REST_URL = "/admin/restaurants";

    @ApiOperation(value = "CREATE new Restaurant")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Restaurant> create(@RequestBody @Valid Restaurant restaurant) {
        checkNew(restaurant);
        Restaurant created = service.create(restaurant);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @ApiOperation(value = "UPDATE Restaurant by ID")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@RequestBody @Valid Restaurant restaurant, @PathVariable int id) {
        assureIdConsistent(restaurant, id);
        service.update(restaurant, id);
    }

    @ApiOperation(value = "DELETE Restaurant by ID")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        service.delete(id);
    }
}
