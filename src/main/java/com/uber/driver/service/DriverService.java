package com.uber.driver.service;

import com.uber.driver.enums.BackgroundCheckStatus;
import com.uber.driver.enums.TrackingDeviceStatus;
import com.uber.driver.model.UberDriver;

public interface DriverService {
    UberDriver getDriver(String driverId);
    UberDriver getDriverByPhoneNumber(String phoneNumber);
    UberDriver saveDriver(UberDriver uberDriver);
    UberDriver updateDriver(UberDriver uberDriver, String driverId);
    void deleteDriver(String driverId);
    UberDriver updateBgCheckStatus(String driverId, BackgroundCheckStatus backgroundCheckStatus);
    UberDriver updateTrackingDeviceStatus(String driverId, TrackingDeviceStatus backgroundCheckStatus);
    UberDriver updateActivationStatus(String driverId, boolean isActive);
    UberDriver updateOnboardingStatus(String driverId, boolean isOnboarded);
    UberDriver updateDocVerifiedStatus(String driverId, boolean isDocVerified);
    boolean checkIfDriverExist(String driverId);
}
