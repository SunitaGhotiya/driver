package com.uber.driver.service;

import com.uber.driver.model.Address;
import com.uber.driver.reposiotry.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl implements AddressService {
    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private DriverService driverService;

    @Override
    public Address saveAddress(Address address) {
        if(driverService.checkIfDriverExist(address.getDriverId()))
            return addressRepository.save(address);
        else
            return null;
    }

    @Override
    public Address getAddress(long driverId) {
        return addressRepository.findById(driverId).orElse(null);
    }

    @Override
    public Address updateAddress(Address address, long driverId) {
        if(checkIfAddressExist(driverId))
            return addressRepository.save(address);
        else
            return null;
    }

    private boolean checkIfAddressExist(long driverId){
        return addressRepository.existsById(driverId);
    }

}
