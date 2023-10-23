package com.uber.driver.service;

import com.uber.driver.enums.DocumentStatus;
import com.uber.driver.model.DriverDocument;

import java.util.List;

public interface DocumentService {
    DriverDocument saveDocument(DriverDocument driverDocument);
    DriverDocument getDocument(long documentId);
    DriverDocument updateDocument(DriverDocument driverDocument, long documentId);
    List<DriverDocument> getAllDocuments(long driverId);
    String getDocumentUrl(long documentId);
    void updateDocumentStatus(long documentId, DocumentStatus documentStatus);
}
