package ro.unibuc.master.groupexpensetracker.domain.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ro.unibuc.master.groupexpensetracker.data.userprofile.UserProfile;
import ro.unibuc.master.groupexpensetracker.domain.repository.UserProfileRepository;
import ro.unibuc.master.groupexpensetracker.exception.EntityNotFoundException;

import java.util.List;

@Service
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;

    public UserProfileService(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    public UserProfile getById(Long id) {
        return userProfileRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Could not find user by id"));
    }

    public void saveAll(List<UserProfile> userProfileList) {
        userProfileRepository.saveAll(userProfileList);
    }

    public ResponseEntity createUser(UserProfile userProfile) {
        UserProfile user = userProfileRepository.findByEmail(userProfile.getEmail());
        if (user != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("The email address is already used");
        } else {
            UserProfile userDB = userProfileRepository.save(userProfile);
            return ResponseEntity.ok(UserProfile.toDto(userDB));
        }
    }

    public ResponseEntity getUserDetails(UserProfile userProfile) {
        UserProfile user = userProfileRepository.findByEmailAndPassword(userProfile.getEmail(), userProfile.getPassword());
        if (user != null) {
            return ResponseEntity.ok(UserProfile.toDto(user));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("The email or the password is invalid");
        }
    }

}
