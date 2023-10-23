package com.uber.driver.service;

import com.uber.driver.model.DriverProfile;

public interface DriverProfileService {
    DriverProfile getDriverProfile(long driverId);
}
