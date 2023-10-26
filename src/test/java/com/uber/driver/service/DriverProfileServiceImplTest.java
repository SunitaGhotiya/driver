package com.uber.driver.service;

import com.uber.driver.model.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@RunWith(MockitoJUnitRunner.class)
public class DriverProfileServiceImplTest {

    @InjectMocks
    private DriverProfileServiceImpl driverProfileService;

    @Mock
    private DriverService driverService;

    @Mock
    private AddressService addressService;

    @Mock
    private LicenceService licenceService;

    @Mock
    private VehicleService vehicleService;

    @Test
    public void getDriverProfile() {
        Mockito.when(driverService.getDriver(Mockito.anyString())).thenReturn(UberDriver.builder().build());
        Mockito.when(addressService.getAddress(Mockito.anyString())).thenReturn(Address.builder().build());
        Mockito.when(licenceService.getLicence(Mockito.anyString())).thenReturn(DriverLicence.builder().build());
        Mockito.when(vehicleService.getVehicle(Mockito.anyString())).thenReturn(DriverVehicle.builder().build());

        DriverProfile driverProfile = driverProfileService.getDriverProfile("123");
        Assert.assertNotNull(driverProfile.getUberDriver());
        Assert.assertNotNull(driverProfile.getAddress());
        Assert.assertNotNull(driverProfile.getDriverLicence());
        Assert.assertNotNull(driverProfile.getDriverVehicle());

    }
}
