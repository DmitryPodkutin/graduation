package com.gmail.podkutin.dmitry.voting_system.web.vote;

import com.gmail.podkutin.dmitry.voting_system.AuthorizedUser;
import com.gmail.podkutin.dmitry.voting_system.model.restaurant.Vote;
import com.gmail.podkutin.dmitry.voting_system.service.VoteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import springfox.documentation.annotations.ApiIgnore;

import java.net.URI;
import java.time.LocalDate;
import java.util.Objects;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "Controller_For_Users", value = "Requests used by the users")
public class ProfileVoteController {
    private static final String REST_URL = "/profile/restaurants/{restaurantId}/votes";
    private final VoteService service;

    public ProfileVoteController(@Autowired VoteService service) {
        this.service = service;
    }

    @GetMapping(value = "/profile/votes")
    @ApiOperation(value = "GET Vote for Restaurant by Date (Param : Date , default Date = NowDate)")
    public Vote get(@RequestParam(value = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return date == null ? service.getForeDate(LocalDate.now()) : service.getForeDate(date);
    }

    @PostMapping(REST_URL)
    @ApiOperation(value = "CREATE Vote for Restaurant")
    public ResponseEntity<Vote> create(@PathVariable int restaurantId, @ApiIgnore @AuthenticationPrincipal AuthorizedUser authUser) {
        Vote created = service.create(restaurantId, authUser);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(restaurantId, Objects.requireNonNull(created).getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(REST_URL + "/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @ApiOperation(value = "UPDATE Vote by ID  (you can only change your voice until 11:00AM)")
    public void update(@PathVariable int restaurantId, @PathVariable int id, @ApiIgnore @AuthenticationPrincipal AuthorizedUser authUser) {
        service.update(id, restaurantId, authUser);
    }

}
