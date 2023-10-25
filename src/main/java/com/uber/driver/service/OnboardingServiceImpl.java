package com.uber.driver.service;

import com.uber.driver.enums.DocumentStatus;
import com.uber.driver.enums.DriverComplianceStatus;
import com.uber.driver.model.DriverDocument;
import com.uber.driver.model.DriverLicence;
import com.uber.driver.reposiotry.LicenceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class OnboardingServiceImpl implements OnboardingService{

    @Autowired
    private DriverService driverService;

    @Override
    public void updateDriverOnboardingStatus(long driverId, List<DriverDocument> driverDocuments) {
        log.info("Check status of all driver documents and Update Driver onboarding status for driverId : {}", driverId);

        Boolean allDocsNotVerified = driverDocuments.stream()
                .map(DriverDocument::getStatus)
                .anyMatch(docStatus -> !DocumentStatus.VERIFIED.equals(docStatus));
        if(!allDocsNotVerified)
            driverService.updateComplianceStatus(driverId, DriverComplianceStatus.DOC_VERIFIED);
    }

}
