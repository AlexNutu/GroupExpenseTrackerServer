package ro.unibuc.master.groupexpensetracker.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.unibuc.master.groupexpensetracker.data.userprofile.UserProfile;
import ro.unibuc.master.groupexpensetracker.domain.service.UserProfileService;

@RestController
@RequestMapping("${user.url}")
public class UserProfileController {

    private final UserProfileService userProfileService;

    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @PostMapping
    public ResponseEntity createUser(@RequestBody UserProfile userProfile) {
        return userProfileService.createUser(userProfile);
    }

    @PostMapping("/details")
    public ResponseEntity getUserDetails(@RequestBody UserProfile userProfile) {
        return userProfileService.getUserDetails(userProfile);
    }
}
