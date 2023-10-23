package com.uber.driver.reposiotry;

import com.uber.driver.model.DriverDocument;
import com.uber.driver.model.UberDriver;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DriverRepository extends CrudRepository<UberDriver, Long> {

    @Query("from UberDriver where phoneNumber = :phoneNumber")
    UberDriver findByPhoneNumber(@Param("phoneNumber") String phoneNumber);

    @Query("UPDATE UberDriver SET activationStatus = :activationStatus where driverId = :driverId")
    void updateActivationStatus(@Param("driverId") long driverId, @Param("activationStatus") String activationStatus);

    @Query("UPDATE UberDriver SET complianceStatus = :complianceStatus where driverId = :driverId")
    void updateComplianceStatus(@Param("driverId") long driverId,  @Param("complianceStatus") String complianceStatus);

}
