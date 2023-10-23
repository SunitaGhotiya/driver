package com.uber.driver.service;

import com.uber.driver.enums.DocumentStatus;
import com.uber.driver.enums.DocumentType;
import com.uber.driver.enums.DriverComplianceStatus;
import com.uber.driver.model.DriverDocument;
import com.uber.driver.reposiotry.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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
            driverDocument.setStatus(DocumentStatus.IN_REVIEW);
            return documentRepository.save(driverDocument);
        }
        else
            return null;
    }

    @Override
    public DriverDocument getDocument(long documentId) {
        return documentRepository.findById(documentId).orElse(null);
    }

    @Override
    public DriverDocument updateDocument(DriverDocument driverDocument, long documentId) {
        if(checkIfDocumentExist(documentId))
            return documentRepository.save(driverDocument);
        else
            return null;
    }

    private boolean checkIfDocumentExist(long driverId){
        return documentRepository.existsById(driverId);
    }

    @Override
    public List<DriverDocument> getAllDocuments(long driverId) {
        List<DriverDocument> documents = documentRepository.findAllByDriverId(driverId);

        Arrays.stream(DocumentType.values())
                .filter(documentType -> documents.stream().map(DriverDocument::getType)
                        .noneMatch(type -> documentType.toString().equals(type)))
                .forEach(documentType -> documents.add(new DriverDocument(driverId, documentType)));

        return documents;
    }

    @Override
    public String getDocumentUrl(long documentId) {
        String fileName = UUID.randomUUID().toString();
        return amazonS3Service.generateUrl(fileName);
    }

    @Override
    public void updateDocumentStatus(long documentId, DocumentStatus documentStatus) {
        if(checkIfDocumentExist(documentId)){
            documentRepository.updateDocumentStatus(documentId, documentStatus.toString());
            DriverDocument driverDocument = getDocument(documentId);

            if(documentStatus == DocumentStatus.VERIFIED) {
                List<DriverDocument> driverDocuments = getAllDocuments(driverDocument.getDriverId());
                onboardingService.updateDriverOnboardingStatus(driverDocument.getDriverId(), driverDocuments);
            }
            else if (documentStatus == DocumentStatus.REJECTED) {
                driverService.updateComplianceStatus(driverDocument.getDriverId(), DriverComplianceStatus.DOC_REJECTED);
            }
        }
    }

}
