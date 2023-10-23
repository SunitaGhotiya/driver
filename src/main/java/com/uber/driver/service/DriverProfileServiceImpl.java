package com.uber.driver.service;

import com.uber.driver.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DriverProfileServiceImpl implements DriverProfileService {

    @Autowired
    private DriverService driverService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private LicenceService licenceService;

    @Autowired
    private VehicleService vehicleService;

    @Override
    public DriverProfile getDriverProfile(long driverId) {
        UberDriver uberDriver = driverService.getDriver(driverId);
        Address address = addressService.getAddress(driverId);
        DriverLicence driverLicence = licenceService.getLicence(driverId);
        DriverVehicle driverVehicle = vehicleService.getVehicle(driverId);

        DriverProfile driverProfile = DriverProfile.builder()
                .uberDriver(uberDriver)
                .address(address)
                .driverLicence(driverLicence)
                .driverVehicle(driverVehicle)
                .build();

        return driverProfile;
    }
}
