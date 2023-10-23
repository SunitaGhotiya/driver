package com.uber.driver.controller;

import com.uber.driver.model.Address;
import com.uber.driver.model.UberDriver;
import com.uber.driver.service.AddressService;
import com.uber.driver.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;


@RestController
public class AddressController {

    @Autowired
    private AddressService addressService;

    @Autowired
    private DriverService driverService;

    @PostMapping("/driver/address")
    public ResponseEntity<Address> saveDriverAddress(@RequestBody Address address){
        Address driverAddress = addressService.saveAddress(address);
        if(Objects.nonNull(driverAddress))
            return ResponseEntity.ok(driverAddress);
        else
            return ResponseEntity.notFound().build();
    }

    @GetMapping("/driver/{id}/address")
    public ResponseEntity<Address> getDriverAddress(@PathVariable("id") long driverId){
        Address driverAddress = addressService.getAddress(driverId);
        if(Objects.nonNull(driverAddress))
            return ResponseEntity.ok(driverAddress);
        else
            return ResponseEntity.notFound().build();
    }

    @PutMapping("/driver/{id}/address")
    public ResponseEntity<Address> updateDriverAddress(@RequestBody Address address, @PathVariable("id") long driverId){
        Address driverAddress = addressService.updateAddress(address, driverId);
        if(Objects.nonNull(driverAddress))
            return ResponseEntity.ok(driverAddress);
        else
            return ResponseEntity.notFound().build();
    }

}
