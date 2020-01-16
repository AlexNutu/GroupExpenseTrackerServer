package ro.unibuc.master.groupexpensetracker.presentation.controller;

import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ro.unibuc.master.groupexpensetracker.data.userprofile.UserProfile;
import ro.unibuc.master.groupexpensetracker.domain.service.UserProfileService;
import ro.unibuc.master.groupexpensetracker.presentation.dto.UserDTO;

@RestController
@RequestMapping("/login")
public class LoginController {

    private final UserProfileService userProfileService;

    public LoginController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @GetMapping
    public Flux<UserDTO> filterBy(@RequestParam(value = "search", required = false) final String search,
                                  @RequestParam(value = "direction", required = false) Sort.Direction direction,
                                  @RequestParam(value = "orderBy", required = false) String orderBy,
                                  @RequestParam(value = "page", required = false) final Integer offset,
                                  @RequestParam(value = "size", required = false) final Integer size) {
        return Flux.fromIterable(userProfileService.findAll(direction, orderBy, search, offset, size).getContent());
    }

    @PostMapping("/")
    public Mono<ResponseEntity> getUserLoggedId(@RequestBody UserProfile userProfile) {
        return Mono.just(userProfileService.getUserLoggedId(userProfile));
    }
}
