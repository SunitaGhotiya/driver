package com.uber.driver.service;

import com.uber.driver.constant.DriverConstants;
import com.uber.driver.exception.ResourceNotFoundException;
import com.uber.driver.model.DriverVehicle;
import com.uber.driver.reposiotry.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VehicleServiceImpl implements VehicleService{
    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private DriverService driverService;

    @Override
    public DriverVehicle saveVehicle(DriverVehicle driverVehicle) {
        if(driverService.checkIfDriverExist(driverVehicle.getDriverId()))
            return vehicleRepository.save(driverVehicle);
        else
            throw new ResourceNotFoundException(DriverConstants.DRIVER_DOES_NOT_EXIST);
    }

    @Override
    public DriverVehicle getVehicle(long driverId) {
        return vehicleRepository.findById(driverId)
                .orElseThrow(() -> new ResourceNotFoundException(DriverConstants.VEHICLE_DOES_NOT_EXIST));
    }

    @Override
    public DriverVehicle updateVehicle(DriverVehicle driverVehicle, long driverId) {
        if(checkIfVehicleExist(driverId))
            return vehicleRepository.save(driverVehicle);
        else
            throw new ResourceNotFoundException(DriverConstants.VEHICLE_DOES_NOT_EXIST);
    }

    private boolean checkIfVehicleExist(long driverId){
        return vehicleRepository.existsById(driverId);
    }

}
