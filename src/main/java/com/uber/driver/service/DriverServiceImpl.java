package com.uber.driver.service;

import com.uber.driver.constant.DriverConstants;
import com.uber.driver.enums.DocumentStatus;
import com.uber.driver.enums.DriverComplianceStatus;
import com.uber.driver.exception.ResourceCannotBeUpdatedException;
import com.uber.driver.exception.ResourceNotFoundException;
import com.uber.driver.model.UberDriver;
import com.uber.driver.reposiotry.DriverRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
public class DriverServiceImpl implements DriverService{
    @Autowired
    private DriverRepository driverRepository;

    @Override
    public UberDriver getDriver(long driverId) {
        return driverRepository.findById(driverId)
                .orElseThrow(() -> new ResourceNotFoundException(DriverConstants.DRIVER_DOES_NOT_EXIST));
    }

    @Override
    public UberDriver getDriverByPhoneNumber(String phoneNumber) {
        UberDriver driver = driverRepository.findByPhoneNumber(phoneNumber);
        if(Objects.nonNull(driver))
            return driverRepository.findByPhoneNumber(phoneNumber);
        else
            throw new ResourceNotFoundException(DriverConstants.DRIVER_DOES_NOT_EXIST);
    }

    @Override
    public UberDriver saveDriver(UberDriver uberDriver) {
        return driverRepository.save(uberDriver);
    }

    @Override
    public UberDriver updateDriver(UberDriver uberDriver, long driverId) {
        if(checkIfDriverExist(driverId))
            return driverRepository.save(uberDriver);
        else
            throw new ResourceNotFoundException(DriverConstants.DRIVER_DOES_NOT_EXIST);
    }

    @Override
    public void deleteDriver(long driverId) {
        driverRepository.deleteById(driverId);
    }

    @Override
    public UberDriver updateActivationStatus(long driverId, String activationStatus) {
        UberDriver uberDriver = getDriver(driverId);
        if(Objects.nonNull(uberDriver))
        {
            if(DriverComplianceStatus.ONBOARDED == uberDriver.getComplianceStatus()){
                log.info("Driver Comliance status : {}, Set Driver ActivationStatus to : {} ",
                        DriverComplianceStatus.ONBOARDED, DocumentStatus.VERIFIED);
                uberDriver.setActivationStatus(activationStatus);
                return updateDriver(uberDriver, driverId);
            }
            else
                throw new ResourceCannotBeUpdatedException(DriverConstants.DRIVER_NOT_ONBOARDED);
        }
        else
            throw new ResourceNotFoundException(DriverConstants.DRIVER_DOES_NOT_EXIST);
    }

    @Override
    public void updateComplianceStatus(long driverId, DriverComplianceStatus complianceStatus){
        driverRepository.updateComplianceStatus(driverId, complianceStatus.toString());
    }

    public boolean checkIfDriverExist(long driverId){
        return driverRepository.existsById(driverId);
    }

}
