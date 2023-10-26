package com.uber.driver.service;

import com.uber.driver.constant.DriverConstants;
import com.uber.driver.enums.BackgroundCheckStatus;
import com.uber.driver.enums.DocumentStatus;
import com.uber.driver.enums.TrackingDeviceStatus;
import com.uber.driver.exception.ResourceCannotBeUpdatedException;
import com.uber.driver.exception.ResourceNotFoundException;
import com.uber.driver.model.UberDriver;
import com.uber.driver.reposiotry.DriverRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class DriverServiceImplTest {

    @InjectMocks
    private DriverServiceImpl driverService;

    @Mock
    private DriverRepository driverRepository;

    @Captor
    ArgumentCaptor<String> driverIdArgumentCaptor;

    @Captor
    ArgumentCaptor<String> driverComplianceStatusCaptor;


    private UberDriver uberDriver;

    @Before
    public void init(){
        uberDriver = UberDriver.builder()
                .driverId("123")
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
        Mockito.when(driverRepository.findById(Mockito.anyString())).thenReturn(Optional.of(uberDriver));
        Assert.assertNotNull(driverService.getDriver("123"));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void getDriverIfDriverDoesNotExists(){
        Mockito.when(driverRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());
        driverService.getDriver("122");
    }

    @Test
    public void saveDriver(){
        Mockito.when(driverRepository.save(Mockito.any(UberDriver.class))).thenReturn(uberDriver);
        Assert.assertEquals(uberDriver, driverService.saveDriver(uberDriver));
    }

    @Test
    public void updateDriverIfDriverExists(){
        Mockito.when(driverRepository.existsById(Mockito.anyString())).thenReturn(true);
        Mockito.when(driverRepository.save(Mockito.any(UberDriver.class))).thenReturn(uberDriver);

        Assert.assertEquals(uberDriver, driverService.updateDriver(uberDriver, "123"));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void updateDriverIfDriverDoesNotExist(){
        Mockito.when(driverRepository.existsById(Mockito.anyString())).thenReturn(false);
        driverService.updateDriver(uberDriver, "123");
    }

    @Test
    public void deleteDriver(){
        driverService.deleteDriver(uberDriver.getDriverId());
        verify(driverRepository).deleteById(driverIdArgumentCaptor.capture());

        Assert.assertEquals(uberDriver.getDriverId(), driverIdArgumentCaptor.getValue());
    }

    //Activation Status UTs

    @Test(expected = ResourceNotFoundException.class)
    public void updateActivationStatusIfDriverDoesNotExist(){
        Mockito.when(driverRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());
        driverService.updateActivationStatus(uberDriver.getDriverId(), true);
    }

    @Test(expected = ResourceCannotBeUpdatedException.class)
    public void updateActivationStatusIfDriverExistsAndNotOnboarded(){
        uberDriver.setOnboarded(false);
        Mockito.when(driverRepository.findById(Mockito.anyString())).thenReturn(Optional.of(uberDriver));
        driverService.updateActivationStatus(uberDriver.getDriverId(), true);
    }

    @Test
    public void updateActivationStatusIfDriverExistsAndOnboarded(){
        uberDriver.setOnboarded(true);
        Mockito.when(driverRepository.findById(Mockito.anyString())).thenReturn(Optional.of(uberDriver));
        Mockito.when(driverRepository.save(Mockito.any(UberDriver.class))).thenReturn(uberDriver);

        UberDriver driver = driverService.updateActivationStatus(uberDriver.getDriverId(), true);
        Assert.assertTrue(driver.isActive());
    }

    //Onboarding Status UTs

    @Test(expected = ResourceNotFoundException.class)
    public void updateOnboardingStatusIfDriverDoesNotExist(){
        Mockito.when(driverRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());
        driverService.updateOnboardingStatus(uberDriver.getDriverId(), true);
    }

    @Test(expected = ResourceCannotBeUpdatedException.class)
    public void updateOnboardingStatus_AlreadySet(){
        Mockito.when(driverRepository.findById(Mockito.anyString())).thenReturn(Optional.of(uberDriver));
        driverService.updateOnboardingStatus(uberDriver.getDriverId(), false);
    }

    @Test
    public void updateOnboardingStatusTrue_IfDriverHasAllValidStatus(){
        uberDriver.setTrackingDeviceStatus(TrackingDeviceStatus.DEVICE_ACTIVE);
        uberDriver.setBackgroundCheckStatus(BackgroundCheckStatus.BG_CHECK_DONE);
        uberDriver.setDocVerified(true);

        Mockito.when(driverRepository.findById(Mockito.anyString())).thenReturn(Optional.of(uberDriver));
        Mockito.when(driverRepository.save(Mockito.any(UberDriver.class))).thenReturn(uberDriver);

        UberDriver driver = driverService.updateOnboardingStatus(uberDriver.getDriverId(), true);
        Assert.assertTrue(driver.isOnboarded());
    }

    @Test(expected = ResourceCannotBeUpdatedException.class)
    public void updateOnboardingStatusTrue_DeviceNotActive(){
        uberDriver.setTrackingDeviceStatus(TrackingDeviceStatus.DEVICE_DISPATCHED);
        uberDriver.setBackgroundCheckStatus(BackgroundCheckStatus.BG_CHECK_DONE);
        uberDriver.setDocVerified(true);

        Mockito.when(driverRepository.findById(Mockito.anyString())).thenReturn(Optional.of(uberDriver));
        driverService.updateOnboardingStatus(uberDriver.getDriverId(), true);
    }

    @Test(expected = ResourceCannotBeUpdatedException.class)
    public void updateOnboardingStatusTrue_BgCheckNotDone(){
        uberDriver.setTrackingDeviceStatus(TrackingDeviceStatus.DEVICE_ACTIVE);
        uberDriver.setBackgroundCheckStatus(BackgroundCheckStatus.BG_CHECK_INIT);
        uberDriver.setDocVerified(true);

        Mockito.when(driverRepository.findById(Mockito.anyString())).thenReturn(Optional.of(uberDriver));
        driverService.updateOnboardingStatus(uberDriver.getDriverId(), true);
    }

    @Test(expected = ResourceCannotBeUpdatedException.class)
    public void updateOnboardingStatusTrue_DocNotVerified(){
        uberDriver.setTrackingDeviceStatus(TrackingDeviceStatus.DEVICE_ACTIVE);
        uberDriver.setBackgroundCheckStatus(BackgroundCheckStatus.BG_CHECK_DONE);
        uberDriver.setDocVerified(false);

        Mockito.when(driverRepository.findById(Mockito.anyString())).thenReturn(Optional.of(uberDriver));
        driverService.updateOnboardingStatus(uberDriver.getDriverId(), true);
    }


    @Test
    public void updateOnboardingStatusFalse_DeviceNotActive(){
        uberDriver.setTrackingDeviceStatus(TrackingDeviceStatus.DEVICE_ERROR);
        uberDriver.setBackgroundCheckStatus(BackgroundCheckStatus.BG_CHECK_DONE);
        uberDriver.setDocVerified(true);
        uberDriver.setOnboarded(true);

        Mockito.when(driverRepository.findById(Mockito.anyString())).thenReturn(Optional.of(uberDriver));
        Mockito.when(driverRepository.save(Mockito.any(UberDriver.class))).thenReturn(uberDriver);

        UberDriver driver = driverService.updateOnboardingStatus(uberDriver.getDriverId(), false);
        Assert.assertFalse(driver.isOnboarded());
    }

    @Test
    public void updateOnboardingStatusFalse_IfDriverHastatus_NotBgCheckDone(){
        uberDriver.setTrackingDeviceStatus(TrackingDeviceStatus.DEVICE_ACTIVE);
        uberDriver.setBackgroundCheckStatus(BackgroundCheckStatus.BG_CHECK_REJECTED);
        uberDriver.setDocVerified(true);
        uberDriver.setOnboarded(true);

        Mockito.when(driverRepository.findById(Mockito.anyString())).thenReturn(Optional.of(uberDriver));
        Mockito.when(driverRepository.save(Mockito.any(UberDriver.class))).thenReturn(uberDriver);

        UberDriver driver = driverService.updateOnboardingStatus(uberDriver.getDriverId(), false);

        Assert.assertFalse(driver.isOnboarded());
    }

    @Test
    public void updateOnboardingStatusFalse_DocNotVerified(){
        uberDriver.setTrackingDeviceStatus(TrackingDeviceStatus.DEVICE_ACTIVE);
        uberDriver.setBackgroundCheckStatus(BackgroundCheckStatus.BG_CHECK_DONE);
        uberDriver.setDocVerified(false);
        uberDriver.setOnboarded(true);

        Mockito.when(driverRepository.findById(Mockito.anyString())).thenReturn(Optional.of(uberDriver));
        Mockito.when(driverRepository.save(Mockito.any(UberDriver.class))).thenReturn(uberDriver);

        UberDriver driver = driverService.updateOnboardingStatus(uberDriver.getDriverId(), false);

        Assert.assertFalse(driver.isOnboarded());
    }

    @Test(expected = ResourceCannotBeUpdatedException.class)
    public void updateOnboardingStatusFalse_IfDriverHasAllValidStatus(){
        uberDriver.setTrackingDeviceStatus(TrackingDeviceStatus.DEVICE_ACTIVE);
        uberDriver.setBackgroundCheckStatus(BackgroundCheckStatus.BG_CHECK_DONE);
        uberDriver.setDocVerified(true);
        uberDriver.setOnboarded(true);

        Mockito.when(driverRepository.findById(Mockito.anyString())).thenReturn(Optional.of(uberDriver));
        driverService.updateOnboardingStatus(uberDriver.getDriverId(), false);
    }


    //BackgroundCheck Status

    @Test(expected = ResourceNotFoundException.class)
    public void updateBgCheckStatusIfDriverDoesNotExist(){
        Mockito.when(driverRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());
        driverService.updateBgCheckStatus(uberDriver.getDriverId(), BackgroundCheckStatus.BG_CHECK_INIT);
    }

    @Test(expected = ResourceCannotBeUpdatedException.class)
    public void updateBgCheckStatus_DocsNotVerified(){
        uberDriver.setDocVerified(false);
        Mockito.when(driverRepository.findById(Mockito.anyString())).thenReturn(Optional.of(uberDriver));
        driverService.updateBgCheckStatus(uberDriver.getDriverId(), BackgroundCheckStatus.BG_CHECK_INIT);
    }

    @Test(expected = ResourceCannotBeUpdatedException.class)
    public void updateBgChecktatusDone_IfDriverExists_BgCheckNotInitiated(){
        uberDriver.setDocVerified(true);
        Mockito.when(driverRepository.findById(Mockito.anyString())).thenReturn(Optional.of(uberDriver));
        driverService.updateBgCheckStatus(uberDriver.getDriverId(), BackgroundCheckStatus.BG_CHECK_DONE);
    }

    @Test(expected = ResourceCannotBeUpdatedException.class)
    public void updateBgCheckStatusRejected_IfDriverExists_BgCheckNotInitiated(){
        uberDriver.setDocVerified(true);
        Mockito.when(driverRepository.findById(Mockito.anyString())).thenReturn(Optional.of(uberDriver));
        driverService.updateBgCheckStatus(uberDriver.getDriverId(), BackgroundCheckStatus.BG_CHECK_REJECTED);
    }

    @Test
    public void updateBgCheckStatusDone_BgCheckInitiated(){
        uberDriver.setDocVerified(true);
        uberDriver.setBackgroundCheckStatus(BackgroundCheckStatus.BG_CHECK_INIT);
        Mockito.when(driverRepository.findById(Mockito.anyString())).thenReturn(Optional.of(uberDriver));
        Mockito.when(driverRepository.save(Mockito.any(UberDriver.class))).thenReturn(uberDriver);

        UberDriver driver = driverService.updateBgCheckStatus(uberDriver.getDriverId(), BackgroundCheckStatus.BG_CHECK_DONE);
        Assert.assertEquals(BackgroundCheckStatus.BG_CHECK_DONE, driver.getBackgroundCheckStatus());
    }

    @Test
    public void updateBgCheckStatusRejected_BgCheckInitiated(){
        uberDriver.setDocVerified(true);
        uberDriver.setBackgroundCheckStatus(BackgroundCheckStatus.BG_CHECK_INIT);
        Mockito.when(driverRepository.findById(Mockito.anyString())).thenReturn(Optional.of(uberDriver));
        Mockito.when(driverRepository.save(Mockito.any(UberDriver.class))).thenReturn(uberDriver);

        UberDriver driver = driverService.updateBgCheckStatus(uberDriver.getDriverId(), BackgroundCheckStatus.BG_CHECK_REJECTED);
        Assert.assertEquals(BackgroundCheckStatus.BG_CHECK_REJECTED, driver.getBackgroundCheckStatus());
    }

    //Tracking Device Status

    @Test(expected = ResourceNotFoundException.class)
    public void updateDeviceStatusIfDriverDoesNotExist(){
        Mockito.when(driverRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());
        driverService.updateTrackingDeviceStatus(uberDriver.getDriverId(), TrackingDeviceStatus.DEVICE_DISPATCHED);
    }

    @Test(expected = ResourceCannotBeUpdatedException.class)
    public void updateDeviceStatus_BgCheckNotDone(){
        uberDriver.setBackgroundCheckStatus(BackgroundCheckStatus.BG_CHECK_INIT);
        Mockito.when(driverRepository.findById(Mockito.anyString())).thenReturn(Optional.of(uberDriver));
        driverService.updateTrackingDeviceStatus(uberDriver.getDriverId(), TrackingDeviceStatus.DEVICE_DELIVERED);
    }

    @Test(expected = ResourceCannotBeUpdatedException.class)
    public void updateDeviceStatusActive_DeviceNotDelivered(){
        uberDriver.setBackgroundCheckStatus(BackgroundCheckStatus.BG_CHECK_DONE);
        uberDriver.setTrackingDeviceStatus(TrackingDeviceStatus.DEVICE_DISPATCHED);
        Mockito.when(driverRepository.findById(Mockito.anyString())).thenReturn(Optional.of(uberDriver));
        driverService.updateTrackingDeviceStatus(uberDriver.getDriverId(), TrackingDeviceStatus.DEVICE_ACTIVE);
    }

    @Test
    public void updateDeviceStatusActive_DeviceDelivered(){
        uberDriver.setBackgroundCheckStatus(BackgroundCheckStatus.BG_CHECK_DONE);
        uberDriver.setTrackingDeviceStatus(TrackingDeviceStatus.DEVICE_DELIVERED);

        Mockito.when(driverRepository.findById(Mockito.anyString())).thenReturn(Optional.of(uberDriver));
        Mockito.when(driverRepository.save(Mockito.any(UberDriver.class))).thenReturn(uberDriver);

        UberDriver driver = driverService.updateTrackingDeviceStatus(uberDriver.getDriverId(), TrackingDeviceStatus.DEVICE_ACTIVE);
        Assert.assertEquals(TrackingDeviceStatus.DEVICE_ACTIVE, driver.getTrackingDeviceStatus());
    }

    @Test(expected = ResourceCannotBeUpdatedException.class)
    public void updateDeviceStatusError_DeviceNotDelivered(){
        uberDriver.setBackgroundCheckStatus(BackgroundCheckStatus.BG_CHECK_DONE);
        uberDriver.setTrackingDeviceStatus(TrackingDeviceStatus.DEVICE_DISPATCHED);
        Mockito.when(driverRepository.findById(Mockito.anyString())).thenReturn(Optional.of(uberDriver));
        driverService.updateTrackingDeviceStatus(uberDriver.getDriverId(), TrackingDeviceStatus.DEVICE_ERROR);
    }

    @Test
    public void updateDeviceStatusError_DeviceDelivered(){
        uberDriver.setBackgroundCheckStatus(BackgroundCheckStatus.BG_CHECK_DONE);
        uberDriver.setTrackingDeviceStatus(TrackingDeviceStatus.DEVICE_DELIVERED);

        Mockito.when(driverRepository.findById(Mockito.anyString())).thenReturn(Optional.of(uberDriver));
        Mockito.when(driverRepository.save(Mockito.any(UberDriver.class))).thenReturn(uberDriver);

        UberDriver driver = driverService.updateTrackingDeviceStatus(uberDriver.getDriverId(), TrackingDeviceStatus.DEVICE_ERROR);
        Assert.assertEquals(TrackingDeviceStatus.DEVICE_ERROR, driver.getTrackingDeviceStatus());
    }

    @Test(expected = ResourceCannotBeUpdatedException.class)
    public void updateDeviceDelivered_DeviceNotDispatched(){
        uberDriver.setBackgroundCheckStatus(BackgroundCheckStatus.BG_CHECK_DONE);
        uberDriver.setTrackingDeviceStatus(null);
        Mockito.when(driverRepository.findById(Mockito.anyString())).thenReturn(Optional.of(uberDriver));
        driverService.updateTrackingDeviceStatus(uberDriver.getDriverId(), TrackingDeviceStatus.DEVICE_DELIVERED);
    }

    @Test
    public void updateDeviceDelivered_DeviceDispatched(){
        uberDriver.setBackgroundCheckStatus(BackgroundCheckStatus.BG_CHECK_DONE);
        uberDriver.setTrackingDeviceStatus(TrackingDeviceStatus.DEVICE_DISPATCHED);

        Mockito.when(driverRepository.findById(Mockito.anyString())).thenReturn(Optional.of(uberDriver));
        Mockito.when(driverRepository.save(Mockito.any(UberDriver.class))).thenReturn(uberDriver);

        UberDriver driver = driverService.updateTrackingDeviceStatus(uberDriver.getDriverId(), TrackingDeviceStatus.DEVICE_DELIVERED);
        Assert.assertEquals(TrackingDeviceStatus.DEVICE_DELIVERED, driver.getTrackingDeviceStatus());
    }

    @Test
    public void updateDeviceDispatched(){
        uberDriver.setBackgroundCheckStatus(BackgroundCheckStatus.BG_CHECK_DONE);
        uberDriver.setTrackingDeviceStatus(null);

        Mockito.when(driverRepository.findById(Mockito.anyString())).thenReturn(Optional.of(uberDriver));
        Mockito.when(driverRepository.save(Mockito.any(UberDriver.class))).thenReturn(uberDriver);

        UberDriver driver = driverService.updateTrackingDeviceStatus(uberDriver.getDriverId(), TrackingDeviceStatus.DEVICE_DISPATCHED);
        Assert.assertEquals(TrackingDeviceStatus.DEVICE_DISPATCHED, driver.getTrackingDeviceStatus());
    }

    //Doc Verification Status
    @Test(expected = ResourceNotFoundException.class)
    public void updateDocVerifyStatusIfDriverDoesNotExist(){
        Mockito.when(driverRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());
        driverService.updateDocVerifiedStatus(uberDriver.getDriverId(), true);
    }

    @Test
    public void updateDocVerifiedStatus(){
        Mockito.when(driverRepository.findById(Mockito.anyString())).thenReturn(Optional.of(uberDriver));
        Mockito.when(driverRepository.save(Mockito.any(UberDriver.class))).thenReturn(uberDriver);

        UberDriver driver = driverService.updateDocVerifiedStatus(uberDriver.getDriverId(), true);
        Assert.assertTrue(driver.isDocVerified());
    }

    @Test
    public void checkIfDriverExist_DriverExist(){
        Mockito.when(driverRepository.existsById(Mockito.anyString())).thenReturn(true);
        Assert.assertTrue(driverService.checkIfDriverExist(uberDriver.getDriverId()));
    }

    @Test
    public void checkIfDriverExist_DriverDoesNotExist(){
        Mockito.when(driverRepository.existsById(Mockito.anyString())).thenReturn(false);
        Assert.assertFalse(driverService.checkIfDriverExist(uberDriver.getDriverId()));
    }

}
