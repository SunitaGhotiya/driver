package com.uber.driver.service;

import com.uber.driver.enums.DocumentStatus;
import com.uber.driver.model.DriverDocument;

import java.util.List;

public interface DocumentService {
    DriverDocument saveDocument(DriverDocument driverDocument);
    DriverDocument getDocument(String documentId);
    DriverDocument updateDocument(DriverDocument driverDocument, String documentId);
    List<DriverDocument> getAllDocuments(String driverId);
    String getDocumentUrl();
    void updateDocumentStatus(String documentId, DocumentStatus documentStatus);
    void updateDocumentUrl(String documentId, String documentUrl);
}
