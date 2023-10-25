package com.uber.driver.service;

import com.uber.driver.enums.DocumentStatus;
import com.uber.driver.enums.DocumentType;
import com.uber.driver.enums.DriverComplianceStatus;
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
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.never;


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
    ArgumentCaptor<Long> driverIdCaptor;

    @Captor
    ArgumentCaptor<List<DriverDocument>> driverDocumentsCaptor;

    @Captor
    ArgumentCaptor<DriverComplianceStatus> driverComplianceStatus;

    @Before
    public void init(){
        driverDocument = createDocument(123, DocumentType.IDENTITY_PROOF);
    }

    @Test
    public void saveDocumentIfDriverExists(){
        when(driverService.checkIfDriverExist(anyLong())).thenReturn(true);
        when(documentRepository.save(any(DriverDocument.class))).thenReturn(driverDocument);

        Assert.assertEquals(driverDocument, documentService.saveDocument(driverDocument));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void saveAddressIfDriverDoesNotExist(){
        when(driverService.checkIfDriverExist(anyLong())).thenReturn(false);
        documentService.saveDocument(driverDocument);
    }

    @Test
    public void saveAddressWithDocumentStatusAsNull(){
        when(driverService.checkIfDriverExist(anyLong())).thenReturn(true);
        when(documentRepository.save(any(DriverDocument.class))).thenReturn(driverDocument);

        driverDocument.setStatus(null);
        DriverDocument actualDriverDocument = documentService.saveDocument(driverDocument);
        Assert.assertEquals(DocumentStatus.IN_REVIEW, actualDriverDocument.getStatus());
    }


    @Test
    public void getDocumentIfDriverExists() {
        when(documentRepository.findById(anyLong())).thenReturn(Optional.of(driverDocument));
        Assert.assertEquals(driverDocument, documentService.getDocument(driverDocument.getDocumentId()));
    }

    @Test (expected = ResourceNotFoundException.class)
    public void getDocumentIfDriverDoesNotExists() {
        when(documentRepository.findById(anyLong())).thenReturn(Optional.empty());
        documentService.getDocument(driverDocument.getDriverId());
    }

    @Test
    public void updateDocumentIfDriverExists(){
        when(documentRepository.existsById(anyLong())).thenReturn(true);
        when(documentRepository.save(any(DriverDocument.class))).thenReturn(driverDocument);

        Assert.assertEquals(driverDocument, documentService.updateDocument(driverDocument, driverDocument.getDocumentId()));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void updateDocumentIfDriverDoesNotExist(){
        when(documentRepository.existsById(anyLong())).thenReturn(false);
        documentService.updateDocument(driverDocument, driverDocument.getDriverId());
    }

    @Test
    public void getAllDocumentForNewDriver(){
        when(documentRepository.findAllByDriverId(anyLong())).thenReturn(new ArrayList<>());
        List<DriverDocument> expectedDocumentList = getAllIntitalDocuments(driverDocument.getDriverId());

        List<DriverDocument> actualDocumentList = documentService.getAllDocuments(driverDocument.getDriverId());
        Assert.assertTrue(expectedDocumentList.containsAll(actualDocumentList));
    }

    @Test
    public void getAllDocumentForExistingDriver(){
        List<DriverDocument> expectedDocumentList = getUserDocuments(driverDocument.getDriverId());
        when(documentRepository.findAllByDriverId(anyLong())).thenReturn(expectedDocumentList);

        List<DriverDocument> actualDocumentList = documentService.getAllDocuments(driverDocument.getDriverId());
        Assert.assertTrue(expectedDocumentList.containsAll(actualDocumentList));
    }

    @Test
    public void getDocumentUrl() {
        String s3Url = "http://driverbucket.s3.amazonaws.com/fileName";
        when(amazonS3Service.generateUrl(anyString())).thenReturn(s3Url);

        Assert.assertEquals(s3Url, documentService.getDocumentUrl(111));
    }

    @Test
    public void updateDocumentStatusIfDocumentExistWithStatusVerified() {
        List<DriverDocument> expectedDocumentList = getUserDocuments(driverDocument.getDriverId());
        driverDocument.setStatus(DocumentStatus.VERIFIED);

        when(documentRepository.findById(anyLong())).thenReturn(Optional.of(driverDocument));
        when(documentRepository.findAllByDriverId(anyLong())).thenReturn(expectedDocumentList);

        documentService.updateDocumentStatus(driverDocument.getDriverId(), driverDocument.getStatus());
        verify(onboardingService).updateDriverOnboardingStatus(driverIdCaptor.capture(), driverDocumentsCaptor.capture());

        Assert.assertEquals(driverDocument.getDriverId(), driverIdCaptor.getValue().longValue());
        Assert.assertEquals(expectedDocumentList, driverDocumentsCaptor.getValue());

    }

    @Test
    public void updateDocumentStatusIfDocumentExistWithStatusRejected() {
        List<DriverDocument> expectedDocumentList = getUserDocuments(driverDocument.getDriverId());
        driverDocument.setStatus(DocumentStatus.REJECTED);

        when(documentRepository.findById(anyLong())).thenReturn(Optional.of(driverDocument));
        documentService.updateDocumentStatus(driverDocument.getDriverId(), driverDocument.getStatus());

        verify(driverService).updateComplianceStatus(driverIdCaptor.capture(), driverComplianceStatus.capture());
        Assert.assertEquals(driverDocument.getDriverId(), driverIdCaptor.getValue().longValue());
        Assert.assertEquals(DriverComplianceStatus.DOC_REJECTED, driverComplianceStatus.getValue());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void updateDocumentStatusIfDocumentDoesNotExist() {
        when(documentRepository.findById(anyLong())).thenReturn(Optional.empty());
        documentService.updateDocumentStatus(driverDocument.getDriverId(), driverDocument.getStatus());
    }

    private List<DriverDocument> getAllIntitalDocuments(long driverId) {
        List<DriverDocument> documentList = new ArrayList<>();
        Arrays.stream(DocumentType.values())
                .forEach(documentType -> documentList.add(new DriverDocument(driverId, documentType)));

        return documentList;
    }

    private List<DriverDocument> getUserDocuments(long driverId) {
        List<DriverDocument> documentList = new ArrayList<>();
        Arrays.stream(DocumentType.values())
                .forEach(documentType -> documentList.add(createDocument(driverId, documentType)));

        return documentList;
    }

    private DriverDocument createDocument(long driverId, DocumentType documentType){
        return DriverDocument.builder()
                .driverId(driverId)
                .documentId(new Random().nextInt())
                .type(documentType.getType())
                .label(documentType.getLabel())
                .status(DocumentStatus.IN_REVIEW)
                .build();
    }

}
