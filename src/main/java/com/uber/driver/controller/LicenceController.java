package com.uber.driver.controller;

import com.uber.driver.model.Address;
import com.uber.driver.model.DriverLicence;
import com.uber.driver.service.LicenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;


@RestController
public class LicenceController {

    @Autowired
    private LicenceService LicenceService;

    @PostMapping("/driver/licence")
    public ResponseEntity<DriverLicence> saveLicence(@RequestBody DriverLicence licence){
        DriverLicence driverLicence = LicenceService.saveLicence(licence);
        if(Objects.nonNull(driverLicence))
            return ResponseEntity.ok(driverLicence);
        else
            return ResponseEntity.notFound().build();
    }

    @GetMapping("/driver/{id}/licence")
    public ResponseEntity<DriverLicence> getLicence(@PathVariable("id") long driverId){
        DriverLicence Licence = LicenceService.getLicence(driverId);
        if(Objects.nonNull(Licence))
            return ResponseEntity.ok(Licence);
        else
            return ResponseEntity.notFound().build();
    }

    @PutMapping("/driver/{id}/licence")
    public ResponseEntity<DriverLicence> updateLicence(@PathVariable("id") long driverId, @RequestBody DriverLicence driverLicence){
        DriverLicence driver = LicenceService.updateLicence(driverLicence, driverId);
        if(Objects.nonNull(driver))
            return ResponseEntity.ok(driver);
        else
            return ResponseEntity.notFound().build();
    }
 
}
