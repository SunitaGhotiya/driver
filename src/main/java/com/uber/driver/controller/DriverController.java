package com.uber.driver.controller;

import com.uber.driver.model.UberDriver;
import com.uber.driver.service.DriverService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Slf4j
@RestController
public class DriverController {

    @Autowired
    private DriverService driverService;

    @GetMapping("/driver/{id}")
    public ResponseEntity<UberDriver> getDriver(@PathVariable("id") long driverId){
        log.info("Request Received to get Driver with driverID : {}", driverId);
        UberDriver driver = driverService.getDriver(driverId);
        return ResponseEntity.ok(driver);
    }

    @GetMapping("/driver")
    public ResponseEntity<String> getDriverByPhoneNumber(@RequestParam("phoneNumber") String phoneNumber){
        log.info("Request Received to get Driver with phoneNumber : {}", phoneNumber);
        UberDriver driver = driverService.getDriverByPhoneNumber(phoneNumber);
        return ResponseEntity.ok("driverId : " + driver.getDriverId());
    }

    @PostMapping("/driver")
    public UberDriver createDriver(@RequestBody UberDriver uberDriver){
        log.info("Request Received to save Driver with driverID : {}", uberDriver.getDriverId());
        return driverService.saveDriver(uberDriver);
    }

    @PutMapping("/driver/{id}")
    public ResponseEntity<UberDriver> updateDriver(@RequestBody UberDriver uberDriver, @PathVariable("id") long driverId){
        log.info("Request Received to update Driver with driverID : {}", driverId);
        UberDriver driver = driverService.updateDriver(uberDriver, driverId);
        return ResponseEntity.ok(driver);
    }

//    @DeleteMapping("/driver/{id}")
//    public void deleteDriver(@PathVariable("id") long driverId){
//        driverService.deleteDriver(driverId);
//    }

    @PatchMapping("/updateActivationStatus")
    public ResponseEntity<UberDriver> updateActivationStatus(@RequestParam long driverId, @RequestParam String activationStatus){
        log.info("Request Received to update Driver Activation status : {} for driverID : {}", activationStatus, driverId);
        UberDriver driver = driverService.updateActivationStatus(driverId, activationStatus);
        log.info("Driver sActivation status updated to : {} for driverID : {}", driver.getActivationStatus(), driverId);
        return ResponseEntity.ok(driver);
    }

}
