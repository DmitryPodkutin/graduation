package com.gmail.podkutin.dmitry.votingsystem.web.restaurant;

import com.gmail.podkutin.dmitry.votingsystem.model.restaurant.Restaurant;
import com.gmail.podkutin.dmitry.votingsystem.service.RestaurantService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = ProfileRestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "Profile_Restaurant_Controller", description = "User browsing of restaurants")
public class ProfileRestaurantController {

    static final String REST_URL = "/profile/restaurants";
    private final RestaurantService service;

    public ProfileRestaurantController(@Autowired RestaurantService service) {
        this.service = service;
    }

    @GetMapping()
    @ApiOperation(value = "GET_ALL Restaurant (Param : withMenu=false/true , default withMenu=true)")
    public List<Restaurant> getAll(@RequestParam(value = "withMenu", required = false, defaultValue = "true") boolean withMenu) {
        return service.getAll(withMenu);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "GET One Restaurant by ID (Param : withMenu=false/true , default withMenu=true)")
    public Restaurant get(@PathVariable int id,
                          @RequestParam(value = "withMenu", required = false, defaultValue = "true") boolean withMenu) {
        return service.get(id, withMenu);
    }
}
