package com.uber.driver.service;

import com.uber.driver.model.DriverDocument;

import java.util.List;

public interface OnboardingService {
    void updateDriverOnboardingStatus(String driverId, List<DriverDocument> driverDocuments);
}
