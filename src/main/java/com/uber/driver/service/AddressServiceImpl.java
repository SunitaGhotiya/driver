package com.uber.driver.service;

import com.uber.driver.constant.DriverConstants;
import com.uber.driver.exception.ResourceNotFoundException;
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
            throw new ResourceNotFoundException(DriverConstants.DRIVER_DOES_NOT_EXIST);
    }

    @Override
    public Address getAddress(String driverId) {
        return addressRepository.findById(driverId)
                .orElseThrow(() -> new ResourceNotFoundException(DriverConstants.ADDRESS_DOES_NOT_EXIST));
    }

    @Override
    public Address updateAddress(Address address, String driverId) {
        if(checkIfAddressExist(driverId))
            return addressRepository.save(address);
        else
            throw new ResourceNotFoundException(DriverConstants.ADDRESS_DOES_NOT_EXIST);
    }

    private boolean checkIfAddressExist(String driverId){
        return addressRepository.existsById(driverId);
    }

}
