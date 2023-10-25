package com.uber.driver.controller;

import com.uber.driver.model.Address;
import com.uber.driver.model.DriverVehicle;
import com.uber.driver.model.UberDriver;
import com.uber.driver.service.DriverService;
import com.uber.driver.service.VehicleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Slf4j
@RestController
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @PostMapping("/driver/vehicle")
    public ResponseEntity<DriverVehicle> saveVehicle(@RequestBody DriverVehicle vehicle){
        log.info("Request Received to save vehicle for driverID : {}", vehicle.getDriverId());
        DriverVehicle driverVehicle = vehicleService.saveVehicle(vehicle);
        return ResponseEntity.ok(driverVehicle);
    }

    @GetMapping("/driver/{id}/vehicle")
    public ResponseEntity<DriverVehicle> getVehicle(@PathVariable("id") long driverId){
        log.info("Request Received to get vehicle for driverID : {}", driverId);
        DriverVehicle vehicle = vehicleService.getVehicle(driverId);
        return ResponseEntity.ok(vehicle);
    }

    @PutMapping("/driver/{id}/vehicle")
    public ResponseEntity<DriverVehicle> updateVehicle(@PathVariable("id") long driverId, @RequestBody DriverVehicle driverVehicle){
        log.info("Request Received to update vehicle for driverID : {}", driverId);
        DriverVehicle driver = vehicleService.updateVehicle(driverVehicle, driverId);
        return ResponseEntity.ok(driver);
    }

}
