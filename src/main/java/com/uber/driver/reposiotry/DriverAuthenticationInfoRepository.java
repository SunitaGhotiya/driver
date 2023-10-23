package com.uber.driver.reposiotry;

import com.uber.driver.model.DriverAuthenticationInfo;
import com.uber.driver.model.DriverDocument;
import org.springframework.data.repository.CrudRepository;

public interface DriverAuthenticationInfoRepository extends CrudRepository<DriverAuthenticationInfo, Integer> {
}
