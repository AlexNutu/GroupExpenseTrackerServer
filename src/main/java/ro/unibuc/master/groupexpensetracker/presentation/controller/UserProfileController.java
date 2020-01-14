package ro.unibuc.master.groupexpensetracker.presentation.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.unibuc.master.groupexpensetracker.data.trip.Trip;
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
    public List<UserDTO> filterBy(@RequestParam(value = "search", required = false) final String search,
                                  @RequestParam(value = "direction", required = false) Sort.Direction direction,
                                  @RequestParam(value = "orderBy", required = false) String orderBy,
                                  @RequestParam(value = "page", required = false) final Integer offset,
                                  @RequestParam(value = "size", required = false) final Integer size) {
        return userProfileService.findAll(direction, orderBy, search, offset, size);
    }

    @PostMapping
    public ResponseEntity createUser(@RequestBody UserProfile userProfile) {
        return userProfileService.createUser(userProfile);
    }

    @PostMapping("/login")
    public ResponseEntity getUserLoggedId(@RequestBody UserProfile userProfile) {
        return userProfileService.getUserLoggedId(userProfile);
    }

    @GetMapping("/{userId}")
    public ResponseEntity getUserDetails(@PathVariable("userId") Long userId) {
        return userProfileService.getUserDetails(userId);
    }
}
