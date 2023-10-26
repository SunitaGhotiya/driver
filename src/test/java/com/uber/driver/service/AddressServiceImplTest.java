package com.uber.driver.service;

import com.uber.driver.exception.ResourceNotFoundException;
import com.uber.driver.model.Address;
import com.uber.driver.reposiotry.AddressRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class AddressServiceImplTest {
    @InjectMocks
    private AddressServiceImpl addressService;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private DriverService driverService;

    private Address address;

    @Before
    public void init(){
        address = Address.builder()
                .driverId("123")
                .addressLine1("Addresss")
                .city("Bangalore")
                .country("India")
                .zipCode("560066")
        .build();
    }

    @Test
    public void saveAddressIfDriverExists(){
        Mockito.when(driverService.checkIfDriverExist(Mockito.anyString())).thenReturn(true);
        Mockito.when(addressRepository.save(Mockito.any(Address.class))).thenReturn(address);

        Assert.assertEquals(address, addressService.saveAddress(address));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void saveAddressIfDriverDoesNotExist(){
        Mockito.when(driverService.checkIfDriverExist(Mockito.anyString())).thenReturn(false);
        addressService.saveAddress(address);
    }

    @Test
    public void getAddressIfDriverExists() {
        Mockito.when(addressRepository.findById(Mockito.anyString())).thenReturn(Optional.of(address));
        Assert.assertEquals(address, addressService.getAddress("123"));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void getAddressIfDriverDoesNotExists() {
        Mockito.when(addressRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());
        addressService.getAddress("123");
    }

    @Test
    public void updateAddressIfDriverExists(){
        Mockito.when(addressRepository.existsById(Mockito.anyString())).thenReturn(true);
        Mockito.when(addressRepository.save(Mockito.any(Address.class))).thenReturn(address);

        Assert.assertEquals(address, addressService.updateAddress(address, "123"));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void updateAddressIfDriverDoesNotExist(){
        Mockito.when(addressRepository.existsById(Mockito.anyString())).thenReturn(false);
        addressService.updateAddress(address, "123");
    }

}
