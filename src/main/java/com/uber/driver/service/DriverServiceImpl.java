package com.uber.driver.service;

import com.uber.driver.enums.DriverComplianceStatus;
import com.uber.driver.model.UberDriver;
import com.uber.driver.reposiotry.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class DriverServiceImpl implements DriverService{
    @Autowired
    private DriverRepository driverRepository;

    @Override
    public UberDriver getDriver(long driverId) {
        return driverRepository.findById(driverId).orElse(null);
    }

    @Override
    public UberDriver getDriverByPhoneNumber(String phoneNumber) {
        return driverRepository.findByPhoneNumber(phoneNumber);
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
            return null;
    }

    @Override
    public void deleteDriver(long driverId) {
        driverRepository.deleteById(driverId);
    }

    @Override
    public String updateActivationStatus(long driverId, String activationStatus) {
        UberDriver uberDriver = getDriver(driverId);
        if(Objects.nonNull(uberDriver))
        {
            if(uberDriver.getComplianceStatus().equals(DriverComplianceStatus.ONBOARDED))
                return "Activation status updated to : "+ activationStatus;
            else
                return "Driver Actuvation status Can't be updated as DriverComplianceStatus: "+ uberDriver.getComplianceStatus();
        }
        else
            return "Driver Does Not Exist!";
    }

    @Override
    public void updateComplianceStatus(long driverId, DriverComplianceStatus complianceStatus){
        driverRepository.updateComplianceStatus(driverId, complianceStatus.toString());
    }

    public boolean checkIfDriverExist(long driverId){
        return driverRepository.existsById(driverId);
    }

}
