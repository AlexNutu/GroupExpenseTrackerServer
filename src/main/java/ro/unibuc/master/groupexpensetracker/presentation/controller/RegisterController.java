package ro.unibuc.master.groupexpensetracker.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.unibuc.master.groupexpensetracker.data.userprofile.UserProfile;
import ro.unibuc.master.groupexpensetracker.domain.service.UserProfileService;

import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/register")
public class RegisterController {

    private final UserProfileService userProfileService;

    public RegisterController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @PostMapping("/")
    public ResponseEntity createUser(@RequestBody UserProfile userProfile) throws NoSuchAlgorithmException {
        return userProfileService.createUser(userProfile);
    }
}
