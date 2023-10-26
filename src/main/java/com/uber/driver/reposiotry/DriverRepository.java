package com.uber.driver.reposiotry;

import com.uber.driver.model.UberDriver;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface DriverRepository extends CrudRepository<UberDriver, String> {

    @Query("from UberDriver where phoneNumber = :phoneNumber")
    UberDriver findByPhoneNumber(@Param("phoneNumber") String phoneNumber);

}
