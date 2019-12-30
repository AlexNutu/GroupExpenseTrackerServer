package ro.unibuc.master.groupexpensetracker.domain.service;

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
}
