package com.gmail.podkutin.dmitry.voting_system.web.vote;

import com.gmail.podkutin.dmitry.voting_system.AuthorizedUser;
import com.gmail.podkutin.dmitry.voting_system.model.restaurant.Vote;
import com.gmail.podkutin.dmitry.voting_system.repository.RestaurantRepository;
import com.gmail.podkutin.dmitry.voting_system.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.Objects;


@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class ProfileVoteController {
    static final String REST_URL = "/profile/restaurants/{restaurantId}/votes";
    final RestaurantRepository restaurantRepository;
    private final VoteService service;

    public ProfileVoteController(@Autowired RestaurantRepository restaurantRepository, @Autowired VoteService service) {
        this.restaurantRepository = restaurantRepository;
        this.service = service;
    }

    @GetMapping(value = "/profile/votes")
    public Vote get(@RequestParam(value = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return date == null ? service.getForeDate(LocalDate.now()) : service.getForeDate(date);
    }

    @PostMapping(REST_URL)
    public ResponseEntity<Vote> create(@PathVariable int restaurantId, @AuthenticationPrincipal AuthorizedUser authUser) {
        Vote created = service.create(restaurantId, authUser);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(restaurantId, Objects.requireNonNull(created).getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(REST_URL + "/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@PathVariable int restaurantId, @PathVariable int id, @AuthenticationPrincipal AuthorizedUser authUser) {
        service.update(id, restaurantId, authUser);
    }

}
