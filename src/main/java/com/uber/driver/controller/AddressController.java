package com.uber.driver.controller;

import com.uber.driver.model.Address;
import com.uber.driver.model.UberDriver;
import com.uber.driver.service.AddressService;
import com.uber.driver.service.DriverService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Slf4j
@ControllerAdvice
@RestController
public class AddressController {

    @Autowired
    private AddressService addressService;

    @PostMapping("/driver/address")
    public ResponseEntity<Address> saveDriverAddress(@RequestBody Address address){
        log.info("Request Received to save driver Address for driverID : {}", address.getDriverId());
        Address driverAddress = addressService.saveAddress(address);
        return ResponseEntity.ok(driverAddress);
   }

    @GetMapping("/driver/{id}/address")
    public ResponseEntity<Address> getDriverAddress(@PathVariable("id") long driverId){
        log.info("Request Received to get driver address for driverID : {}", driverId);
        Address driverAddress = addressService.getAddress(driverId);
        return ResponseEntity.ok(driverAddress);
    }

    @PutMapping("/driver/{id}/address")
    public ResponseEntity<Address> updateDriverAddress(@RequestBody Address address, @PathVariable("id") long driverId){
        log.info("Request Received to update driver address for driverID : {}", driverId);
        Address driverAddress = addressService.updateAddress(address, driverId);
        return ResponseEntity.ok(driverAddress);
    }

}
