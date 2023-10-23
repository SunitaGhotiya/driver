package com.uber.driver.reposiotry;

import com.uber.driver.model.DriverLicence;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LicenceRepository extends CrudRepository<DriverLicence, Long> {
}
