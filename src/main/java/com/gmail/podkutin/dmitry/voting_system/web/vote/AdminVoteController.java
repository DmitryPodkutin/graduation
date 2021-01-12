package com.gmail.podkutin.dmitry.voting_system.web.vote;

import com.gmail.podkutin.dmitry.voting_system.model.restaurant.Vote;
import com.gmail.podkutin.dmitry.voting_system.service.VoteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "Controller_For_Admin", value = "Requests used by the admins")
public class AdminVoteController {

    private final VoteService service;
    static final String ADMIN_REST_URL = "/admin/restaurants/{restaurantId}/votes";

    public AdminVoteController(@Autowired VoteService service) {
        this.service = service;
    }

    @GetMapping(ADMIN_REST_URL)
    @ApiOperation(value = "GET Voice History for Restaurant")
    public List<Vote> getAllForRestaurant(@PathVariable("restaurantId") @NumberFormat int restaurantId) {
        return service.getAllForRestaurant(restaurantId);
    }
}
