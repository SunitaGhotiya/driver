package com.uber.driver.service;

import com.uber.driver.constant.DriverConstants;
import com.uber.driver.enums.DocumentStatus;
import com.uber.driver.enums.DocumentType;
import com.uber.driver.exception.ResourceNotFoundException;
import com.uber.driver.model.DriverDocument;
import com.uber.driver.reposiotry.DocumentRepository;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class DocumentServiceImpl implements DocumentService{
    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private AmazonS3Service amazonS3Service;

    @Autowired
    private DriverService driverService;

    @Autowired
    private OnboardingService onboardingService;

    @Override
    public DriverDocument saveDocument(DriverDocument driverDocument) {
        if(driverService.checkIfDriverExist(driverDocument.getDriverId())) {
            log.info("Setting document status to {} for driverID : {}", DocumentStatus.IN_REVIEW, driverDocument.getDriverId());
            if(!StringUtil.isNullOrEmpty(driverDocument.getLocation()))
                driverDocument.setStatus(DocumentStatus.IN_REVIEW);
            return documentRepository.save(driverDocument);
        }
        else
            throw new ResourceNotFoundException(DriverConstants.DRIVER_DOES_NOT_EXIST);
    }

    @Override
    public DriverDocument getDocument(String documentId) {
        return documentRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFoundException(DriverConstants.DOCUMENT_DOES_NOT_EXIST));
    }

    @Override
    public DriverDocument updateDocument(DriverDocument driverDocument, String documentId) {
        if(checkIfDocumentExist(documentId))
            return documentRepository.save(driverDocument);
        else
            throw new ResourceNotFoundException(DriverConstants.DOCUMENT_DOES_NOT_EXIST);
    }

    private boolean checkIfDocumentExist(String driverId){
        return documentRepository.existsById(driverId);
    }

    @Override
    public List<DriverDocument> getAllDocuments(String driverId) {
        List<DriverDocument> documents = documentRepository.findAllByDriverId(driverId);
        log.info("Sending empty document objects for the type of documents which are not yet created to be displayed on UI");

        Arrays.stream(DocumentType.values())
                .filter(documentType -> documents.stream().map(DriverDocument::getType)
                        .noneMatch(type -> documentType.toString().equals(type)))
                .forEach(documentType -> {
                    DriverDocument driverDocument = saveDocument(new DriverDocument(driverId, documentType));
                    documents.add(driverDocument);
                });

        return documents;
    }

    @Override
    public String getDocumentUrl() {
        String fileName = UUID.randomUUID().toString();
        return amazonS3Service.generateUrl(fileName);
    }

    @Override
    public void updateDocumentUrl(String documentId, String documentURL) {
        documentRepository.updateDocumentLocation(documentId, documentURL);
        updateDocumentStatus(documentId, DocumentStatus.IN_REVIEW);
    }

    @Override
    public void updateDocumentStatus(String documentId, DocumentStatus documentStatus) {
        DriverDocument driverDocument = getDocument(documentId);
        if(Objects.nonNull(driverDocument)){
            documentRepository.updateDocumentStatus(documentId, documentStatus.toString());

            if(documentStatus == DocumentStatus.VERIFIED) {
                log.info("Document Status : {} : Check status of all documents to update driver onboarding status", DocumentStatus.VERIFIED);
                List<DriverDocument> driverDocuments = getAllDocuments(driverDocument.getDriverId());
                onboardingService.updateDriverOnboardingStatus(driverDocument.getDriverId(), driverDocuments);
            }
            else {
                log.info("Document Status : {} : update driver Document verification status to false", documentStatus);
                driverService.updateDocVerifiedStatus(driverDocument.getDriverId(), false);
            }
        }
        else
            throw new ResourceNotFoundException(DriverConstants.DOCUMENT_DOES_NOT_EXIST);
    }

}
