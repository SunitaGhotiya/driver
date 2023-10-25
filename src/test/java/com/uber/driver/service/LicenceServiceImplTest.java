package com.uber.driver.service;

import com.uber.driver.exception.ResourceNotFoundException;
import com.uber.driver.model.DriverLicence;
import com.uber.driver.reposiotry.LicenceRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class LicenceServiceImplTest {

    @InjectMocks
    private LicenceServiceImpl licenceService;

    @Mock
    private LicenceRepository licenceRepository;

    @Mock
    private DriverService driverService;

    private DriverLicence licence;

    @Before
    public void init() throws ParseException {
        licence = DriverLicence.builder()
                .driverID(123)
                .licenceNo("L123")
                .expirationDate(new SimpleDateFormat("yyyy-MM-dd").parse("2030-12-31"))
                .build();
    }

    @Test
    public void saveLicenceIfDriverExists(){
        Mockito.when(driverService.checkIfDriverExist(Mockito.anyLong())).thenReturn(true);
        Mockito.when(licenceRepository.save(Mockito.any(DriverLicence.class))).thenReturn(licence);

        Assert.assertEquals(licence, licenceService.saveLicence(licence));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void saveLicenceIfDriverDoesNotExist(){
        Mockito.when(driverService.checkIfDriverExist(Mockito.anyLong())).thenReturn(false);
        licenceService.saveLicence(licence);
    }


    @Test
    public void getLicenceIfDriverExists() {
        Mockito.when(licenceRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(licence));
        Assert.assertEquals(licence, licenceService.getLicence(123));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void getLicenceIfDriverDoesNotExists() {
        Mockito.when(licenceRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        licenceService.getLicence(123);
    }

    @Test
    public void updateLicenceIfDriverExists(){
        Mockito.when(licenceRepository.existsById(Mockito.anyLong())).thenReturn(true);
        Mockito.when(licenceRepository.save(Mockito.any(DriverLicence.class))).thenReturn(licence);

        Assert.assertEquals(licence, licenceService.updateLicence(licence, 123));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void updateLicenceIfDriverDoesNotExist(){
        Mockito.when(licenceRepository.existsById(Mockito.anyLong())).thenReturn(false);
        licenceService.updateLicence(licence, 123);
    }

}
