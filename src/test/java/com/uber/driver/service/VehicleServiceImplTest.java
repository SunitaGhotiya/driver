package com.uber.driver.service;

import com.uber.driver.exception.ResourceNotFoundException;
import com.uber.driver.model.DriverVehicle;
import com.uber.driver.reposiotry.VehicleRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class VehicleServiceImplTest {

    @InjectMocks
    private VehicleServiceImpl vehicleService;

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private DriverService driverService;

    private DriverVehicle vehicle;

    @Before
    public void init(){
        vehicle = DriverVehicle.builder()
                .driverId("123")
                .vehicleNo("1503")
                .type("Four Wheeler")
                .build();
    }


    @Test
    public void getVehicleIfDriverExists() {
        Mockito.when(vehicleRepository.findById(Mockito.anyString())).thenReturn(Optional.of(vehicle));
        Assert.assertEquals(vehicle, vehicleService.getVehicle("123"));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void getVehicleIfDriverDoesNotExists() {
        Mockito.when(vehicleRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());
        vehicleService.getVehicle("123");
    }


    @Test
    public void saveVehicleIfDriverExists(){
        Mockito.when(driverService.checkIfDriverExist(Mockito.anyString())).thenReturn(true);
        Mockito.when(vehicleRepository.save(Mockito.any(DriverVehicle.class))).thenReturn(vehicle);

        Assert.assertEquals(vehicle, vehicleService.saveVehicle(vehicle));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void saveVehicleIfDriverDoesNotExist(){
        Mockito.when(driverService.checkIfDriverExist(Mockito.anyString())).thenReturn(false);
        vehicleService.saveVehicle(vehicle);
    }

    @Test
    public void updateVehicleIfDriverExists(){
        Mockito.when(vehicleRepository.existsById(Mockito.anyString())).thenReturn(true);
        Mockito.when(vehicleRepository.save(Mockito.any(DriverVehicle.class))).thenReturn(vehicle);

        Assert.assertEquals(vehicle, vehicleService.updateVehicle(vehicle, "123"));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void updateVehicleIfDriverDoesNotExist(){
        Mockito.when(vehicleRepository.existsById(Mockito.anyString())).thenReturn(false);
        vehicleService.updateVehicle(vehicle, "123");
    }


}
