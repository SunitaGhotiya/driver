package com.uber.driver.service;

import com.uber.driver.constant.DriverConstants;
import com.uber.driver.enums.BackgroundCheckStatus;
import com.uber.driver.enums.TrackingDeviceStatus;
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
    public UberDriver getDriver(String driverId) {
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
    public UberDriver updateDriver(UberDriver uberDriver, String driverId) {
        if(checkIfDriverExist(driverId))
            return driverRepository.save(uberDriver);
        else
            throw new ResourceNotFoundException(DriverConstants.DRIVER_DOES_NOT_EXIST);
    }

    @Override
    public void deleteDriver(String driverId) {
        driverRepository.deleteById(driverId);
    }

    @Override
    public UberDriver updateBgCheckStatus(String driverId, BackgroundCheckStatus backgroundCheckStatus) {
        UberDriver uberDriver = getDriver(driverId);
        if(uberDriver.isDocVerified()) {
            if((backgroundCheckStatus == BackgroundCheckStatus.BG_CHECK_DONE
                    || backgroundCheckStatus == BackgroundCheckStatus.BG_CHECK_REJECTED)
                    && uberDriver.getBackgroundCheckStatus() == BackgroundCheckStatus.BG_CHECK_INIT){

                uberDriver.setBackgroundCheckStatus(backgroundCheckStatus);
                return driverRepository.save(uberDriver);
            }
            else
                throw new ResourceCannotBeUpdatedException("Background Check not initiated");
        }
        else
            throw new ResourceCannotBeUpdatedException("Documents not verified");
    }

    @Override
    public UberDriver updateTrackingDeviceStatus(String driverId, TrackingDeviceStatus trackingDeviceStatus) {
        UberDriver uberDriver = getDriver(driverId);
        if(uberDriver.getBackgroundCheckStatus() == BackgroundCheckStatus.BG_CHECK_DONE){
            if(shouldUpdateDeviceStatus(uberDriver, trackingDeviceStatus)){
                uberDriver.setTrackingDeviceStatus(trackingDeviceStatus);
                return driverRepository.save(uberDriver);
            }
            else
                throw new ResourceCannotBeUpdatedException("Tracking Device status cannot be updated");
        }
        else
            throw new ResourceCannotBeUpdatedException("Background check not completed");
    }

    @Override
    public UberDriver updateActivationStatus(String driverId, boolean isActive) {
        UberDriver uberDriver = getDriver(driverId);
        if(uberDriver.isOnboarded()){
            log.info("Driver ActivationStatus updated to : {} ", isActive);
            uberDriver.setActive(isActive);
            return driverRepository.save(uberDriver);
        }
        else
            throw new ResourceCannotBeUpdatedException(DriverConstants.DRIVER_NOT_ONBOARDED);
    }

    @Override
    public UberDriver updateOnboardingStatus(String driverId, boolean isOnboarded){
        UberDriver uberDriver = getDriver(driverId);
        if(uberDriver.isOnboarded() != isOnboarded)
        {
            if(isOnboarded){
                if(uberDriver.getBackgroundCheckStatus() == BackgroundCheckStatus.BG_CHECK_DONE
                        && uberDriver.getTrackingDeviceStatus() == TrackingDeviceStatus.DEVICE_ACTIVE
                        && uberDriver.isDocVerified()) {

                    uberDriver.setOnboarded(true);
                }
                else
                    throw new ResourceCannotBeUpdatedException("Driver Onboarding status cannot be set to "+ isOnboarded);
            }
            else if(uberDriver.getBackgroundCheckStatus() != BackgroundCheckStatus.BG_CHECK_DONE
                || uberDriver.getTrackingDeviceStatus() != TrackingDeviceStatus.DEVICE_ACTIVE
                || !uberDriver.isDocVerified()){

                uberDriver.setOnboarded(false);
            } else
                throw new ResourceCannotBeUpdatedException("Driver Onboarding status cannot be set to "+ isOnboarded);


            return driverRepository.save(uberDriver);
        }
        else
            throw new ResourceCannotBeUpdatedException("Driver Onboarding status is already set to "+ isOnboarded);
    }

    public boolean checkIfDriverExist(String driverId){
        return driverRepository.existsById(driverId);
    }

    private boolean shouldUpdateDeviceStatus(UberDriver driver, TrackingDeviceStatus newDeviceStatus){
        TrackingDeviceStatus currentDeviceStatus = driver.getTrackingDeviceStatus();
        switch (newDeviceStatus) {
            case DEVICE_ACTIVE:
            case DEVICE_ERROR:
                return TrackingDeviceStatus.DEVICE_DELIVERED == currentDeviceStatus;
            case DEVICE_DELIVERED:
                return TrackingDeviceStatus.DEVICE_DISPATCHED == currentDeviceStatus;
            case DEVICE_DISPATCHED:
                return true;
            default:
                return false;
        }
    }

    @Override
    public UberDriver updateDocVerifiedStatus(String driverId, boolean isDocVerifies) {
        UberDriver uberDriver = getDriver(driverId);
        log.info("Document Verification status updated to : {} ", isDocVerifies);
        uberDriver.setDocVerified(isDocVerifies);
        return driverRepository.save(uberDriver);
    }

}
