package com.uber.driver.service;

import com.uber.driver.model.DriverLicence;

public interface LicenceService {
    DriverLicence saveLicence(DriverLicence driverLicence);
    DriverLicence getLicence(String driverId);
    DriverLicence updateLicence(DriverLicence driverLicence, String driverId);
}
