package com.uber.driver.service;

import com.uber.driver.model.Address;

public interface AddressService {
    Address saveAddress(Address address);
    Address getAddress(String driverId);
    Address updateAddress(Address address, String driverId);
}
