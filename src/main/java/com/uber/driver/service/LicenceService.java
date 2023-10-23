package com.uber.driver.service;

import com.uber.driver.model.DriverLicence;

public interface LicenceService {
    DriverLicence saveLicence(DriverLicence driverLicence);
    DriverLicence getLicence(long driverId);
    DriverLicence updateLicence(DriverLicence driverLicence, long driverId);
}
