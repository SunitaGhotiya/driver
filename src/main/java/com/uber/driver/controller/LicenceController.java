package com.uber.driver.controller;

import com.uber.driver.model.Address;
import com.uber.driver.model.DriverLicence;
import com.uber.driver.service.LicenceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;


@Slf4j
@RestController
public class LicenceController {

    @Autowired
    private LicenceService LicenceService;

    @PostMapping("/driver/licence")
    public ResponseEntity<DriverLicence> saveLicence(@RequestBody DriverLicence licence){
        log.info("Request Received to save Driver Licence for driverID : {}", licence.getDriverID());
        DriverLicence driverLicence = LicenceService.saveLicence(licence);
        return ResponseEntity.ok(driverLicence);
    }

    @GetMapping("/driver/{id}/licence")
    public ResponseEntity<DriverLicence> getLicence(@PathVariable("id") long driverId){
        log.info("Request Received to get Driver Licence for driverID : {}", driverId);
        DriverLicence Licence = LicenceService.getLicence(driverId);
        return ResponseEntity.ok(Licence);
    }

    @PutMapping("/driver/{id}/licence")
    public ResponseEntity<DriverLicence> updateLicence(@PathVariable("id") long driverId, @RequestBody DriverLicence driverLicence){
        log.info("Request Received to update Driver Licence for driverID : {}", driverLicence.getDriverID());
        DriverLicence driver = LicenceService.updateLicence(driverLicence, driverId);
        return ResponseEntity.ok(driver);
    }
 
}
