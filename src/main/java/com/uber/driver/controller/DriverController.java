package com.uber.driver.controller;

import com.uber.driver.enums.BackgroundCheckStatus;
import com.uber.driver.enums.TrackingDeviceStatus;
import com.uber.driver.model.UberDriver;
import com.uber.driver.service.DriverService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class DriverController {

    @Autowired
    private DriverService driverService;

    @GetMapping("/driver/{id}")
    public ResponseEntity<UberDriver> getDriver(@PathVariable("id") String driverId){
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
    public UberDriver saveDriver(@RequestBody UberDriver uberDriver){
        log.info("Request Received to save Driver with driverID : {}", uberDriver.getDriverId());
        return driverService.saveDriver(uberDriver);
    }

    @PutMapping("/driver/{id}")
    public ResponseEntity<UberDriver> updateDriver(@RequestBody UberDriver uberDriver, @PathVariable("id") String driverId){
        log.info("Request Received to update Driver with driverID : {}", driverId);
        UberDriver driver = driverService.updateDriver(uberDriver, driverId);
        return ResponseEntity.ok(driver);
    }

    @PostMapping("/updateActivationStatus")
    public ResponseEntity<UberDriver> updateActivationStatus(@RequestParam String driverId, @RequestParam boolean isActive){
        log.info("Request Received to update Driver Activation status : {} for driverID : {}", isActive, driverId);
        UberDriver driver = driverService.updateActivationStatus(driverId, isActive);
        log.info("Driver activation status updated to : {} for driverID : {}", driver.isActive(), driverId);
        return ResponseEntity.ok(driver);
    }

    @PostMapping("/updateBgCheckStatus")
    public ResponseEntity<UberDriver> updateBgCheckStatus(@RequestParam String driverId, @RequestParam BackgroundCheckStatus backgroundCheckStatus){
        log.info("Request Received to update Driver Background Check status : {} for driverID : {}", backgroundCheckStatus, driverId);
        UberDriver driver = driverService.updateBgCheckStatus(driverId, backgroundCheckStatus);
        log.info("Driver Background check status updated to : {} for driverID : {}", driver.isActive(), driverId);
        return ResponseEntity.ok(driver);
    }

    @PostMapping("/updateTrackingDeviceStatus")
    public ResponseEntity<UberDriver> updateTrackingDeviceStatus(@RequestParam String driverId, @RequestParam TrackingDeviceStatus deviceStatus){
        log.info("Request Received to update Driver Compliance status : {} for driverID : {}", deviceStatus, driverId);
        UberDriver driver = driverService.updateTrackingDeviceStatus(driverId, deviceStatus);
        log.info("Driver Tracking Device status updated to : {} for driverID : {}", driver, driver.getTrackingDeviceStatus());
        return ResponseEntity.ok(driver);
    }

    @PostMapping("/updateOnboardingStatus")
    public ResponseEntity<UberDriver> updateOnboardingStatus(@RequestParam String driverId, @RequestParam boolean isOnboarded){
        log.info("Request Received to update Driver Onboarding status : {} for driverID : {}", isOnboarded, driverId);
        UberDriver driver = driverService.updateOnboardingStatus(driverId, isOnboarded);
        log.info("Driver Onboarding status updated to : {} for driverID : {}", driver, driver.isOnboarded());
        return ResponseEntity.ok(driver);
    }

}
