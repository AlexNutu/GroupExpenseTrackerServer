package ro.unibuc.master.groupexpensetracker.domain.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ro.unibuc.master.groupexpensetracker.common.utils.EntitySpecification;
import ro.unibuc.master.groupexpensetracker.common.utils.EntityUtils;
import ro.unibuc.master.groupexpensetracker.common.utils.SearchCriteria;
import ro.unibuc.master.groupexpensetracker.data.userprofile.UserProfile;
import ro.unibuc.master.groupexpensetracker.domain.repository.UserProfileRepository;
import ro.unibuc.master.groupexpensetracker.exception.EntityNotFoundException;
import ro.unibuc.master.groupexpensetracker.presentation.dto.UserDTO;

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

    public void save(UserProfile userProfile) {
        userProfileRepository.save(userProfile);
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

    public ResponseEntity getUserLoggedId(UserProfile userProfile) {
        UserProfile user = userProfileRepository.findByEmailAndPassword(userProfile.getEmail(), userProfile.getPassword());
        if (user != null) {
            return ResponseEntity.ok(UserProfile.toDto(user));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("The email or the password is invalid");
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

    public Page<UserDTO> findAll(Sort.Direction sortingDirection, String orderBy, final String search, final Integer offset, final Integer size) {
        final List<SearchCriteria> searchCriteriaList = EntityUtils.generateSearchCriteria(search);
        final Specification<UserProfile> spec = new EntitySpecification<>(searchCriteriaList);

        if (sortingDirection == null && orderBy == null) {
            orderBy = "createDate";
            sortingDirection = Sort.Direction.DESC;
        }

        if (size != null) {
            return userProfileRepository.findAll(spec,
                    EntityUtils.getPageRequest(sortingDirection, orderBy, offset, size))
                    .map(UserProfile::toDto);
        } else {
            return new PageImpl<>(userProfileRepository.findAll(spec))
                    .map(UserProfile::toDto);
        }
    }

//    public Page<StockTransactionTypeDTO> searchTransactionDTOS(Sort.Direction direction, String orderBy, String search, Integer offset, Integer size, String subinventoryCode) {
//        SystemOptions systemOptions = systemOptionsService.findOneByCode("COD_TT_REC");
//        return searchTransactionType(direction, orderBy, search, offset, size)
//                .map(transactionType -> toDto(transactionType, subinventoryCode, systemOptions));
//    }
//
//    private StockTransactionTypeDTO toDto(StockTransactionType stockTransactionType, String subinventoryCode, SystemOptions systemOptions) {
//        boolean associated = stockTransactionType.getStockSubinventories().stream()
//                .anyMatch(stockSubinventory -> stockSubinventory.getCode().equals(subinventoryCode));
//        boolean displayCategoies = stockTransactionType.getTransactionTypeId() == Long.parseLong(systemOptions.getValue());
//        boolean displayDestinations = stockTransactionType.getStockClassTransaction().getClassTransactionId() == TRANSFER_CLASS_ID;
//        return StockTransactionTypeDTO.builder()
//                .transactionTypeId(stockTransactionType.getTransactionTypeId())
//                .name(stockTransactionType.getName())
//                .associated(associated)
//                .displayDestinations(displayDestinations)
//                .displayCategoies(displayCategoies)
//                .build();
//    }
}
