package com.uber.driver.reposiotry;

import com.uber.driver.model.UserToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTokenRepository extends CrudRepository<UserToken, String> {
}
