package com.uber.driver.service;

import com.uber.driver.enums.DocumentStatus;
import com.uber.driver.enums.DocumentType;
import com.uber.driver.exception.ResourceNotFoundException;
import com.uber.driver.model.DriverDocument;
import com.uber.driver.reposiotry.DocumentRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;


@RunWith(MockitoJUnitRunner.class)
public class DocumentServiceImplTest {

    @InjectMocks
    private DocumentServiceImpl documentService;

    @Mock
    private DocumentRepository documentRepository;

    @Mock
    private AmazonS3Service amazonS3Service;

    @Mock
    private DriverService driverService;

    @Mock
    private OnboardingService onboardingService;

    private DriverDocument driverDocument;

    @Captor
    ArgumentCaptor<String> driverIdCaptor;

    @Captor
    ArgumentCaptor<List<DriverDocument>> driverDocumentsCaptor;

    @Captor
    ArgumentCaptor<Boolean> documentVerificationStatus;

    @Before
    public void init(){
        driverDocument = createDocument("123", DocumentType.IDENTITY_PROOF);
    }

    @Test
    public void saveDocumentIfDriverExists(){
        when(driverService.checkIfDriverExist(anyString())).thenReturn(true);
        when(documentRepository.save(any(DriverDocument.class))).thenReturn(driverDocument);

        Assert.assertEquals(driverDocument, documentService.saveDocument(driverDocument));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void saveDocumentIfDriverDoesNotExist(){
        when(driverService.checkIfDriverExist(anyString())).thenReturn(false);
        documentService.saveDocument(driverDocument);
    }

    @Test
    public void saveDocument_ifLocationNotPresent(){
        when(driverService.checkIfDriverExist(anyString())).thenReturn(true);
        when(documentRepository.save(any(DriverDocument.class))).thenReturn(driverDocument);

        DriverDocument actualDriverDocument = documentService.saveDocument(driverDocument);
        Assert.assertEquals(DocumentStatus.PENDING, actualDriverDocument.getStatus());
    }

    @Test
    public void saveDocument_ifLocationPresent(){
        driverDocument.setLocation("https://abcd.com/1234");
        when(driverService.checkIfDriverExist(anyString())).thenReturn(true);
        when(documentRepository.save(any(DriverDocument.class))).thenReturn(driverDocument);

        DriverDocument actualDriverDocument = documentService.saveDocument(driverDocument);
        Assert.assertEquals(DocumentStatus.IN_REVIEW, actualDriverDocument.getStatus());
    }

    @Test
    public void getDocumentIfDriverExists() {
        when(documentRepository.findById(anyString())).thenReturn(Optional.of(driverDocument));
        Assert.assertEquals(driverDocument, documentService.getDocument(driverDocument.getDocumentId()));
    }

    @Test (expected = ResourceNotFoundException.class)
    public void getDocumentIfDriverDoesNotExists() {
        when(documentRepository.findById(anyString())).thenReturn(Optional.empty());
        documentService.getDocument(driverDocument.getDriverId());
    }

    @Test
    public void updateDocumentIfDriverExists(){
        when(documentRepository.existsById(anyString())).thenReturn(true);
        when(documentRepository.save(any(DriverDocument.class))).thenReturn(driverDocument);

        Assert.assertEquals(driverDocument, documentService.updateDocument(driverDocument, driverDocument.getDocumentId()));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void updateDocumentIfDriverDoesNotExist(){
        when(documentRepository.existsById(anyString())).thenReturn(false);
        documentService.updateDocument(driverDocument, driverDocument.getDriverId());
    }

    @Test
    public void getAllDocumentForDriver(){
        List<DriverDocument> expectedDocumentList = getDocuments(driverDocument.getDriverId());
        when(documentRepository.findAllByDriverId(anyString())).thenReturn(expectedDocumentList);

        List<DriverDocument> actualDocumentList = documentService.getAllDocuments(driverDocument.getDriverId());
        Assert.assertTrue(expectedDocumentList.containsAll(actualDocumentList));
    }

    @Test
    public void getDocumentUrl() {
        String s3Url = "http://driverbucket.s3.amazonaws.com/fileName";
        when(amazonS3Service.generateUrl(anyString())).thenReturn(s3Url);

        Assert.assertEquals(s3Url, documentService.getDocumentUrl());
    }

    @Test
    public void updateDocumentUrl() {
        driverDocument.setLocation("http://driverbucket.s3.amazonaws.com/fileName");
        when(documentRepository.findById(anyString())).thenReturn(Optional.of(driverDocument));
        documentService.updateDocumentUrl(driverDocument.getDocumentId(), driverDocument.getLocation());
        verify(driverService).updateDocVerifiedStatus(driverIdCaptor.capture(), documentVerificationStatus.capture());

        Assert.assertEquals(driverDocument.getDriverId(), driverIdCaptor.getValue());
        Assert.assertFalse(documentVerificationStatus.getValue());
    }

    @Test
    public void updateDocumentStatusIfDocumentExistWithStatusVerified() {
        List<DriverDocument> expectedDocumentList = getDocuments(driverDocument.getDriverId());
        driverDocument.setStatus(DocumentStatus.VERIFIED);

        when(documentRepository.findById(anyString())).thenReturn(Optional.of(driverDocument));
        when(documentRepository.findAllByDriverId(anyString())).thenReturn(expectedDocumentList);

        documentService.updateDocumentStatus(driverDocument.getDriverId(), driverDocument.getStatus());
        verify(onboardingService).updateDriverOnboardingStatus(driverIdCaptor.capture(), driverDocumentsCaptor.capture());

        Assert.assertEquals(driverDocument.getDriverId(), driverIdCaptor.getValue());
        Assert.assertEquals(expectedDocumentList, driverDocumentsCaptor.getValue());

    }

    @Test
    public void updateDocumentStatusIfDocumentExistWithStatusRejected() {
        driverDocument.setStatus(DocumentStatus.REJECTED);

        when(documentRepository.findById(anyString())).thenReturn(Optional.of(driverDocument));
        documentService.updateDocumentStatus(driverDocument.getDriverId(), driverDocument.getStatus());

        verify(driverService).updateDocVerifiedStatus(driverIdCaptor.capture(), documentVerificationStatus.capture());
        Assert.assertEquals(driverDocument.getDriverId(), driverIdCaptor.getValue());
        Assert.assertFalse(documentVerificationStatus.getValue());
    }

    @Test
    public void updateDocumentStatusIfDocumentExistWithStatusPendingOrInReview() {
        driverDocument.setStatus(DocumentStatus.PENDING);

        when(documentRepository.findById(anyString())).thenReturn(Optional.of(driverDocument));
        documentService.updateDocumentStatus(driverDocument.getDriverId(), driverDocument.getStatus());

        verify(driverService).updateDocVerifiedStatus(driverIdCaptor.capture(), documentVerificationStatus.capture());
        Assert.assertEquals(driverDocument.getDriverId(), driverIdCaptor.getValue());
        Assert.assertFalse(documentVerificationStatus.getValue());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void updateDocumentStatusIfDocumentDoesNotExist() {
        when(documentRepository.findById(anyString())).thenReturn(Optional.empty());
        documentService.updateDocumentStatus(driverDocument.getDriverId(), driverDocument.getStatus());
    }

    private List<DriverDocument> getDocuments(String driverId) {
        List<DriverDocument> documentList = new ArrayList<>();
        Arrays.stream(DocumentType.values())
                .forEach(documentType -> documentList.add(createDocument(driverId, documentType)));

        return documentList;
    }

    private DriverDocument createDocument(String driverId, DocumentType documentType){
        return DriverDocument.builder()
                .driverId(driverId)
                .documentId(UUID.randomUUID().toString())
                .type(documentType.getType())
                .label(documentType.getLabel())
                .status(DocumentStatus.PENDING)
                .build();
    }

}
