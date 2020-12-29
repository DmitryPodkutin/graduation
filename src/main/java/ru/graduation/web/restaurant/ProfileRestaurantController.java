package ru.graduation.web.restaurant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.graduation.model.restaurant.Restaurant;
import ru.graduation.service.RestaurantService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = ProfileRestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class ProfileRestaurantController {

    static final String REST_URL = "/profile/restaurants";
    private final RestaurantService service;

    public ProfileRestaurantController(@Autowired RestaurantService service) {
        this.service = service;
    }

    @GetMapping()
    public List<Restaurant> getAll(@RequestParam(value = "withMenu", required = false, defaultValue = "true") boolean withMenu) {
        if (withMenu) {
            return service.getAllWithMenuDay(LocalDate.now());
        }
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Restaurant get(@PathVariable int id,
                          @RequestParam(value = "withMenu", required = false, defaultValue = "true") boolean withMenu) {
        if (withMenu) {
            return service.getWithMenuDay(id, LocalDate.now());
        }
        return service.get(id);
    }
}
