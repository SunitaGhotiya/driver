package com.uber.driver.reposiotry;

import com.uber.driver.model.UserOtpAuthentication;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserOtpAuthenticationRepository extends CrudRepository<UserOtpAuthentication, String> {
}
