package com.uber.driver.controller;

import com.uber.driver.model.Address;
import com.uber.driver.model.DriverVehicle;
import com.uber.driver.model.UberDriver;
import com.uber.driver.service.DriverService;
import com.uber.driver.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;


@RestController
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @PostMapping("/driver/vehicle")
    public ResponseEntity<DriverVehicle> saveVehicle(@RequestBody DriverVehicle vehicle){
        DriverVehicle driverVehicle = vehicleService.saveVehicle(vehicle);
        if(Objects.nonNull(driverVehicle))
            return ResponseEntity.ok(driverVehicle);
        else
            return ResponseEntity.notFound().build();
    }

    @GetMapping("/driver/{id}/vehicle")
    public ResponseEntity<DriverVehicle> getVehicle(@PathVariable("id") long driverId){
        DriverVehicle vehicle = vehicleService.getVehicle(driverId);
        if(Objects.nonNull(vehicle))
            return ResponseEntity.ok(vehicle);
        else
            return ResponseEntity.notFound().build();
    }

    @PutMapping("/driver/{id}/vehicle")
    public ResponseEntity<DriverVehicle> updateVehicle(@PathVariable("id") long driverId, @RequestBody DriverVehicle driverVehicle){
        DriverVehicle driver = vehicleService.updateVehicle(driverVehicle, driverId);
        if(Objects.nonNull(driver))
            return ResponseEntity.ok(driver);
        else
            return ResponseEntity.notFound().build();
    }

}
