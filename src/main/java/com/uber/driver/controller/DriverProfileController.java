package com.uber.driver.controller;


import com.uber.driver.model.DriverProfile;
import com.uber.driver.model.UberDriver;
import com.uber.driver.service.DriverProfileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@Slf4j
@RestController
public class DriverProfileController {

    @Autowired
    private DriverProfileService driverProfileService;

    @GetMapping("/driver/{id}/profile")
    public ResponseEntity<DriverProfile> getDriverProfile(@PathVariable("id") String driverId){
        log.info("Request Received to get Driver Profile for driverID : {}", driverId);
        DriverProfile driverProfile = driverProfileService.getDriverProfile(driverId);
        return ResponseEntity.ok(driverProfile);
    }
}
