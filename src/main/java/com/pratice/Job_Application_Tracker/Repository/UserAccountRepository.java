package com.pratice.Job_Application_Tracker.Repository;

import com.pratice.Job_Application_Tracker.domain.entity.UserAccount;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
    Optional<UserAccount> findByEmailIgnoreCase(String email);
    boolean existsByEmailIgnoreCase(String email);
    boolean existsByMobile(String mobile);
    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);
    boolean existsByMobileAndIdNot(String mobile, Long id);
}
