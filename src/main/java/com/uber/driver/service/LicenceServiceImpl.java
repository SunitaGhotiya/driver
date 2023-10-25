package com.uber.driver.service;

import com.uber.driver.constant.DriverConstants;
import com.uber.driver.exception.ResourceNotFoundException;
import com.uber.driver.model.DriverLicence;
import com.uber.driver.reposiotry.LicenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LicenceServiceImpl implements LicenceService{
    @Autowired
    private LicenceRepository licenceRepository;

    @Autowired
    private DriverService driverService;

    @Override
    public DriverLicence saveLicence(DriverLicence driverLicence) {
        if(driverService.checkIfDriverExist(driverLicence.getDriverID()))
            return licenceRepository.save(driverLicence);
        else
            throw new ResourceNotFoundException(DriverConstants.DRIVER_DOES_NOT_EXIST);
    }

    @Override
    public DriverLicence getLicence(long driverId) {
        return licenceRepository.findById(driverId)
                .orElseThrow(() -> new ResourceNotFoundException(DriverConstants.LICENCE_DOES_NOT_EXIST));
    }

    @Override
    public DriverLicence updateLicence(DriverLicence driverLicence, long driverId) {
        if(checkIfLicenceExist(driverId))
            return licenceRepository.save(driverLicence);
        else
            throw new ResourceNotFoundException(DriverConstants.LICENCE_DOES_NOT_EXIST);
    }

    private boolean checkIfLicenceExist(long driverId){
        return licenceRepository.existsById(driverId);
    }

}
