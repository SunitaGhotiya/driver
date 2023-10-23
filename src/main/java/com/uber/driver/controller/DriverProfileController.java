package com.uber.driver.controller;


import com.uber.driver.model.DriverProfile;
import com.uber.driver.model.UberDriver;
import com.uber.driver.service.DriverProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class DriverProfileController {

    @Autowired
    private DriverProfileService driverProfileService;

    @GetMapping("/driver/{id}/profile")
    public ResponseEntity<DriverProfile> getDriverProfile(@PathVariable("id") long driverId){
        DriverProfile driverProfile = driverProfileService.getDriverProfile(driverId);
        if(Objects.nonNull(driverProfile))
            return ResponseEntity.ok(driverProfile);
        else
            return ResponseEntity.notFound().build();
    }
}
