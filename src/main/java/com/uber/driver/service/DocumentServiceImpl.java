package com.uber.driver.service;

import com.uber.driver.constant.DriverConstants;
import com.uber.driver.enums.DocumentStatus;
import com.uber.driver.enums.DocumentType;
import com.uber.driver.enums.DriverComplianceStatus;
import com.uber.driver.exception.ResourceNotFoundException;
import com.uber.driver.model.DriverDocument;
import com.uber.driver.reposiotry.DocumentRepository;
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
            driverDocument.setStatus(DocumentStatus.IN_REVIEW);
            return documentRepository.save(driverDocument);
        }
        else
            throw new ResourceNotFoundException(DriverConstants.DRIVER_DOES_NOT_EXIST);
    }

    @Override
    public DriverDocument getDocument(long documentId) {
        return documentRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFoundException(DriverConstants.DOCUMENT_DOES_NOT_EXIST));
    }

    @Override
    public DriverDocument updateDocument(DriverDocument driverDocument, long documentId) {
        if(checkIfDocumentExist(documentId))
            return documentRepository.save(driverDocument);
        else
            throw new ResourceNotFoundException(DriverConstants.DOCUMENT_DOES_NOT_EXIST);
    }

    private boolean checkIfDocumentExist(long driverId){
        return documentRepository.existsById(driverId);
    }

    @Override
    public List<DriverDocument> getAllDocuments(long driverId) {
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
    public String getDocumentUrl(long documentId) {
        String fileName = UUID.randomUUID().toString();
        return amazonS3Service.generateUrl(fileName);
    }

    @Override
    public void updateDocumentStatus(long documentId, DocumentStatus documentStatus) {
        DriverDocument driverDocument = getDocument(documentId);
        if(Objects.nonNull(driverDocument)){
            documentRepository.updateDocumentStatus(documentId, documentStatus.toString());

            if(documentStatus == DocumentStatus.VERIFIED) {
                log.info("Document Status : {} : Check status of all documents to update driver onboarding status", DocumentStatus.VERIFIED);
                List<DriverDocument> driverDocuments = getAllDocuments(driverDocument.getDriverId());
                onboardingService.updateDriverOnboardingStatus(driverDocument.getDriverId(), driverDocuments);
            }
            else if (documentStatus == DocumentStatus.REJECTED) {
                log.info("Document Status : {} : update driver onboarding status to {}", DocumentStatus.VERIFIED, DriverComplianceStatus.DOC_REJECTED);
                driverService.updateComplianceStatus(driverDocument.getDriverId(), DriverComplianceStatus.DOC_REJECTED);
            }
            else {
                log.info("Document Status : {} : update driver onboarding status to {}", documentStatus, DriverComplianceStatus.IN_PROGRESS);
                driverService.updateComplianceStatus(driverDocument.getDriverId(), DriverComplianceStatus.IN_PROGRESS);
            }
        }
        else
            throw new ResourceNotFoundException(DriverConstants.DOCUMENT_DOES_NOT_EXIST);
    }

}
