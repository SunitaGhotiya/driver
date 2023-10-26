package com.uber.driver.service;

import com.uber.driver.enums.DocumentStatus;
import com.uber.driver.enums.DocumentType;
import com.uber.driver.model.DriverDocument;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class OnboardingServiceImplTest {

    @InjectMocks
    private OnboardingServiceImpl onboardingService;

    @Mock
    private DriverService driverService;

    @Captor
    ArgumentCaptor<String> driverIdCaptor;

    @Captor
    ArgumentCaptor<Boolean> documentVerifiedStatus;

    @Test
    public void updateDriverOnboardingStatus() {
        List<DriverDocument> documentList = new ArrayList<>();
        documentList.add(createDocument("123", DocumentType.IDENTITY_PROOF, DocumentStatus.VERIFIED));
        documentList.add(createDocument("123", DocumentType.DRIVER_LICENCE, DocumentStatus.VERIFIED));

        onboardingService.updateDriverOnboardingStatus("123", documentList);
        verify(driverService).updateDocVerifiedStatus(driverIdCaptor.capture(), documentVerifiedStatus.capture());

        Assert.assertEquals("123", driverIdCaptor.getValue());
        Assert.assertTrue(documentVerifiedStatus.getValue());

    }

    @Test
    public void updateDriverOnboardingStatus_allDocsNotVerified() {
        List<DriverDocument> documentList = new ArrayList<>();
        documentList.add(createDocument("123", DocumentType.IDENTITY_PROOF, DocumentStatus.VERIFIED));
        documentList.add(createDocument("123", DocumentType.DRIVER_LICENCE, DocumentStatus.IN_REVIEW));

        onboardingService.updateDriverOnboardingStatus("123", documentList);
        verify(driverService, Mockito.never()).updateDocVerifiedStatus(anyString(), anyBoolean());
     }


    private DriverDocument createDocument(String driverId, DocumentType documentType, DocumentStatus documentStatus){
        return DriverDocument.builder()
                .driverId(driverId)
                .documentId(String.valueOf(new Random().nextInt()))
                .type(documentType.getType())
                .label(documentType.getLabel())
                .status(documentStatus)
                .build();
    }
}
