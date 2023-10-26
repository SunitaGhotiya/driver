package com.uber.driver.reposiotry;

import com.uber.driver.model.DriverVehicle;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleRepository extends CrudRepository<DriverVehicle, String> {
}
