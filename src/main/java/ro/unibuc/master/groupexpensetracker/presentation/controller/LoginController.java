package ro.unibuc.master.groupexpensetracker.presentation.controller;

import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ro.unibuc.master.groupexpensetracker.data.userprofile.UserProfile;
import ro.unibuc.master.groupexpensetracker.data.userprofile.UserProfileInfo;
import ro.unibuc.master.groupexpensetracker.domain.service.UserProfileService;
import ro.unibuc.master.groupexpensetracker.presentation.dto.UserDTO;

import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@RequestMapping("/login")
public class LoginController {

    private final UserProfileService userProfileService;

    public LoginController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @GetMapping
    public Flux<UserProfile> filterBy(@RequestParam(value = "search", required = false) final String search,
                                      @RequestParam(value = "direction", required = false) Sort.Direction direction,
                                      @RequestParam(value = "orderBy", required = false) String orderBy,
                                      @RequestParam(value = "page", required = false) final Integer offset,
                                      @RequestParam(value = "size", required = false) final Integer size) {
        return Flux.fromIterable(userProfileService.findAll(direction, orderBy, search, offset, size));

    }

    @PostMapping("/")
    public Mono<ResponseEntity> authenticate(@RequestBody UserProfile userProfile) throws NoSuchAlgorithmException {
        Authentication auth = userProfileService.authenticate(userProfile);
        if (auth != null) {
            SecurityContextHolder.getContext().setAuthentication(auth);
            UserProfile u = userProfileService.getByEmail(auth.getPrincipal().toString());
            return Mono.just(ResponseEntity.ok(new UserProfileInfo(u.getId(), u.getFirstName(), u.getLastName(), u.getPassword(), u.getEmail(), u.getReceiveNotifications())));
        } else {
            return Mono.just(ResponseEntity.badRequest().body("Invalid email or password"));
        }
    }
}
