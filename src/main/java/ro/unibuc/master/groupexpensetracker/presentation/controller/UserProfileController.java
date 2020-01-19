package ro.unibuc.master.groupexpensetracker.presentation.controller;

import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ro.unibuc.master.groupexpensetracker.data.userprofile.UserProfile;
import ro.unibuc.master.groupexpensetracker.domain.service.UserProfileService;
import ro.unibuc.master.groupexpensetracker.presentation.dto.UserDTO;

import java.util.List;

@RestController
@RequestMapping("${user.url}")
public class UserProfileController {

    private final UserProfileService userProfileService;

    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @GetMapping
    public Flux<UserDTO> filterBy(@RequestParam(value = "search", required = false) final String search,
                                  @RequestParam(value = "direction", required = false) Sort.Direction direction,
                                  @RequestParam(value = "orderBy", required = false) String orderBy,
                                  @RequestParam(value = "page", required = false) final Integer offset,
                                  @RequestParam(value = "size", required = false) final Integer size) {
        return Flux.fromIterable(userProfileService.findAll(direction, orderBy, search, offset, size));
    }

    @GetMapping("/{userId}")
    public Mono<ResponseEntity> getUserDetails(@PathVariable("userId") Long userId) {
        return Mono.just(userProfileService.getUserDetails(userId));
    }
}
