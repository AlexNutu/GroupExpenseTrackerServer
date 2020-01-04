package ro.unibuc.master.groupexpensetracker.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ro.unibuc.master.groupexpensetracker.data.userprofile.UserProfile;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long>, JpaSpecificationExecutor<UserProfile> {
    UserProfile findByEmail(String email);

    UserProfile findByEmailAndPassword(String email, String password);

    UserProfile findById(long id);
}
