package com.uber.driver.controller;

import com.uber.driver.model.UberDriver;
import com.uber.driver.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;


@RestController
public class DriverController {

    @Autowired
    private DriverService driverService;

    @GetMapping("/driver/{id}")
    public ResponseEntity<UberDriver> getDriver(@PathVariable("id") long driverId){
        UberDriver driver = driverService.getDriver(driverId);
        if(Objects.nonNull(driver))
            return ResponseEntity.ok(driver);
        else
            return ResponseEntity.notFound().build();
    }

    @GetMapping("/driver")
    public ResponseEntity<String> getDriverByPhoneNumber(@RequestParam("phoneNumber") String phoneNumber){
        UberDriver driver = driverService.getDriverByPhoneNumber(phoneNumber);
        if(Objects.nonNull(driver))
            return ResponseEntity.ok("driverId : " + driver.getDriverId());
        else
            return ResponseEntity.notFound().build();
    }

    @PostMapping("/driver")
    public UberDriver createDriver(@RequestBody UberDriver uberDriver){
        return driverService.saveDriver(uberDriver);
    }

    @PutMapping("/driver/{id}")
    public ResponseEntity<UberDriver> updateDriver(@RequestBody UberDriver uberDriver, @PathVariable("id") long driverId){
        UberDriver driver = driverService.updateDriver(uberDriver, driverId);
        if(Objects.nonNull(driver))
            return ResponseEntity.ok(driver);
        else
            return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/driver/{id}")
    public void deleteDriver(@PathVariable("id") long driverId){
        driverService.deleteDriver(driverId);
    }

    @PatchMapping("/updateActivationStatus")
    public ResponseEntity<String> updateActivationStatus(@RequestParam long driverId, @RequestParam String activationStatus){
        String response = driverService.updateActivationStatus(driverId, activationStatus);
        return ResponseEntity.ok(response);
    }

}
