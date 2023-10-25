package com.uber.driver.service;

import com.uber.driver.constant.DriverConstants;
import com.uber.driver.enums.DriverComplianceStatus;
import com.uber.driver.exception.ResourceNotFoundException;
import com.uber.driver.model.Address;
import com.uber.driver.model.UberDriver;
import com.uber.driver.reposiotry.DriverRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class DriverServiceImplTest {

    @InjectMocks
    private DriverServiceImpl driverService;

    @Mock
    private DriverRepository driverRepository;

    @Captor
    ArgumentCaptor<Long> driverIdArgumentCaptor;

    @Captor
    ArgumentCaptor<String> driverComplianceStatusCaptor;


    private UberDriver uberDriver;

    @Before
    public void init(){
        uberDriver = UberDriver.builder()
                .driverId(123)
                .city("Bangalore")
                .build();
    }

    @Test
    public void getDriverByPhoneNumberIfDriverExists(){
        Mockito.when(driverRepository.findByPhoneNumber(Mockito.anyString())).thenReturn(UberDriver.builder().build());
        Assert.assertNotNull(driverService.getDriverByPhoneNumber("9876543210"));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void getDriverByPhoneNumberIfDriverDoesNotExists(){
        Mockito.when(driverRepository.findByPhoneNumber(Mockito.anyString())).thenReturn(null);
        driverService.getDriverByPhoneNumber("9876543211");
    }

    @Test
    public void getDriverIfDriverExists(){
        Mockito.when(driverRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(uberDriver));
        Assert.assertNotNull(driverService.getDriver(123));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void getDriverIfDriverDoesNotExists(){
        Mockito.when(driverRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
       driverService.getDriver(122);
    }

    @Test
    public void saveDriver(){
        Mockito.when(driverRepository.save(Mockito.any(UberDriver.class))).thenReturn(uberDriver);
        Assert.assertEquals(uberDriver, driverService.saveDriver(uberDriver));
    }

    @Test
    public void updateDriverIfDriverExists(){
        Mockito.when(driverRepository.existsById(Mockito.anyLong())).thenReturn(true);
        Mockito.when(driverRepository.save(Mockito.any(UberDriver.class))).thenReturn(uberDriver);

        Assert.assertEquals(uberDriver, driverService.updateDriver(uberDriver, 123));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void updateDriverIfDriverDoesNotExist(){
        Mockito.when(driverRepository.existsById(Mockito.anyLong())).thenReturn(false);
        driverService.updateDriver(uberDriver, 123);
    }

    @Test
    public void deleteDriver(){
        driverService.deleteDriver(uberDriver.getDriverId());
        verify(driverRepository).deleteById(driverIdArgumentCaptor.capture());

        Assert.assertEquals(uberDriver.getDriverId(), driverIdArgumentCaptor.getValue().longValue());
    }

//    @Test
//    public void updateActivationStatusIfDriverExistsAndNotOnboarded(){
//        uberDriver.setComplianceStatus(DriverComplianceStatus.IN_PROGRESS);
//        Mockito.when(driverRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(uberDriver));
//
//        UberDriver driver = driverService.updateActivationStatus(uberDriver.getDriverId(), "Active");
//
//        Assert.assertEquals(DriverConstants.DRIVER_NOT_ONBOARDED, statusUpdateResponse);
//    }
//
//    @Test
//    public void updateActivationStatusIfDriverExistsAndOnboarded(){
//        uberDriver.setComplianceStatus(DriverComplianceStatus.ONBOARDED);
//        Mockito.when(driverRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(uberDriver));
//        Mockito.when(driverRepository.save(Mockito.anyLong())).thenReturn(Optional.of(uberDriver));
//
//        String statusUpdateResponse = driverService.updateActivationStatus(uberDriver.getDriverId(), "Active");
//
//        Assert.assertEquals(DriverConstants.DRIVER_STATUS_UPDATED, statusUpdateResponse);
//    }
//
//    @Test
//    public void updateActivationStatusIfDriverDoesNotExist(){
//        Mockito.when(driverRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
//
//        String statusUpdateResponse = driverService.updateActivationStatus(uberDriver.getDriverId(), "Active");
//
//        Assert.assertEquals(DriverConstants.DRIVER_DOES_NOT_EXISTS, statusUpdateResponse);
//    }

    @Test
    public void updateComplianceStatus(){
        driverService.updateComplianceStatus(uberDriver.getDriverId(), DriverComplianceStatus.DOC_VERIFIED);
        verify(driverRepository).updateComplianceStatus(driverIdArgumentCaptor.capture(), driverComplianceStatusCaptor.capture());

        Assert.assertEquals(uberDriver.getDriverId(), driverIdArgumentCaptor.getValue().longValue());
        Assert.assertEquals(DriverComplianceStatus.DOC_VERIFIED.toString(), driverComplianceStatusCaptor.getValue());
    }

    @Test
    public void checkIfDriverExist_DriverExist(){
        Mockito.when(driverRepository.existsById(Mockito.anyLong())).thenReturn(true);
        Assert.assertTrue(driverService.checkIfDriverExist(uberDriver.getDriverId()));
    }

    @Test
    public void checkIfDriverExist_DriverDoesNotExist(){
        Mockito.when(driverRepository.existsById(Mockito.anyLong())).thenReturn(false);
        Assert.assertFalse(driverService.checkIfDriverExist(uberDriver.getDriverId()));
    }

}
