package ro.unibuc.master.groupexpensetracker.domain.service;

import org.apache.catalina.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ro.unibuc.master.groupexpensetracker.common.utils.EntitySpecification;
import ro.unibuc.master.groupexpensetracker.common.utils.EntityUtils;
import ro.unibuc.master.groupexpensetracker.common.utils.SearchCriteria;
import ro.unibuc.master.groupexpensetracker.data.userprofile.UserProfile;
import ro.unibuc.master.groupexpensetracker.data.userprofile.UserProfileInfo;
import ro.unibuc.master.groupexpensetracker.domain.repository.UserProfileRepository;
import ro.unibuc.master.groupexpensetracker.exception.EntityNotFoundException;
import ro.unibuc.master.groupexpensetracker.presentation.dto.UserDTO;
import ro.unibuc.master.groupexpensetracker.security.Security;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
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

    public UserProfile getByEmail(String id) {
        return userProfileRepository.findByEmail(id);
    }

    public void saveAll(List<UserProfile> userProfileList) {
        userProfileRepository.saveAll(userProfileList);
    }

    public void save(UserProfile userProfile) {
        userProfileRepository.save(userProfile);
    }

    public ResponseEntity createUser(UserProfile userProfile) throws NoSuchAlgorithmException {
        UserProfile user = userProfileRepository.findByEmail(userProfile.getEmail());
        if (user != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("The email address is already used");
        } else {
            userProfile.setPassword(Security.computeMD5(userProfile.getPassword()));
            userProfile.setReceiveNotifications(true);
            UserProfile u = userProfileRepository.save(userProfile);
            Authentication auth = this.authenticate(u);
            SecurityContextHolder.getContext().setAuthentication(auth);
            return ResponseEntity.ok(new UserProfileInfo(u.getId(), u.getFirstName(), u.getLastName(), u.getPassword(), u.getEmail(), u.getReceiveNotifications()));
        }
    }

    public Authentication authenticate(UserProfile userProfile) throws NoSuchAlgorithmException {
        UserProfile user = userProfileRepository.findByEmailAndPassword(userProfile.getEmail(), Security.computeMD5(userProfile.getPassword()));
        if (user != null) {
            List<GrantedAuthority> grantedAuths = new ArrayList<>();
            grantedAuths.add(new SimpleGrantedAuthority("USER"));
            return new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword(), grantedAuths);
        } else {
            return null;
        }
    }

    public ResponseEntity getUserDetails(long userId) {
        UserProfile user = userProfileRepository.findById(userId);
        if (user != null) {
            return ResponseEntity.ok(UserProfile.toDto(user));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Could not find user by id");
        }
    }

    public List<UserDTO> findAll(Sort.Direction sortingDirection, String orderBy, final String search, final Integer offset, final Integer size) {
        final List<SearchCriteria> searchCriteriaList = EntityUtils.generateSearchCriteria(search);
        final Specification<UserProfile> spec = new EntitySpecification<>(searchCriteriaList);

        if (sortingDirection == null && orderBy == null) {
            orderBy = "createDate";
            sortingDirection = Sort.Direction.DESC;
        }

        if (size != null) {
            return userProfileRepository.findAll(spec,
                    EntityUtils.getPageRequest(sortingDirection, orderBy, offset, size))
                    .map(UserProfile::toDto).getContent();
        } else {
            return new PageImpl<>(userProfileRepository.findAll(spec))
                    .map(UserProfile::toDto).getContent();
        }
    }

    public ResponseEntity updateUser(UserProfile userProfile, long userId) {
        UserProfile userProfileDB = userProfileRepository.findById(userId);
        userProfileDB.setFirstName(userProfile.getFirstName());
        userProfileDB.setLastName(userProfile.getLastName());
        userProfileDB.setEmail(userProfile.getEmail());
        userProfileDB.setReceiveNotifications(userProfile.getReceiveNotifications());
        userProfileRepository.save(userProfileDB);
        return ResponseEntity.ok().build();
    }
}
