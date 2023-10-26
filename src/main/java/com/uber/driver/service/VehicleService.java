package com.uber.driver.service;

import com.uber.driver.model.DriverVehicle;
import com.uber.driver.model.UberDriver;

public interface VehicleService {
    DriverVehicle saveVehicle(DriverVehicle driverVehicle);
    DriverVehicle getVehicle(String driverId);
    DriverVehicle updateVehicle(DriverVehicle driverVehicle, String driverId);
}
