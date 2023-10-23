package com.uber.driver.service;

import com.uber.driver.enums.DriverComplianceStatus;
import com.uber.driver.model.UberDriver;

public interface DriverService {
    UberDriver getDriver(long driverId);
    UberDriver getDriverByPhoneNumber(String phoneNumber);
    UberDriver saveDriver(UberDriver uberDriver);
    UberDriver updateDriver(UberDriver uberDriver, long driverId);
    void deleteDriver(long driverId);
    String updateActivationStatus(long driverId, String activationStatus);
    void updateComplianceStatus(long driverId, DriverComplianceStatus complianceStatus);
    boolean checkIfDriverExist(long driverId);
}
