package com.uber.driver.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DriverProfile {
    private UberDriver uberDriver;
    private Address address;
    private DriverLicence driverLicence;
    private DriverVehicle driverVehicle;
}
