package com.uber.driver.controller;

import com.uber.driver.enums.DocumentStatus;
import com.uber.driver.model.DriverDocument;
import com.uber.driver.service.DocumentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @PostMapping("/driver/document")
    public ResponseEntity<DriverDocument> saveDriverDocument(@RequestBody DriverDocument document){
        log.info("Request Received to save driver Document for driverID : {} and documentID : {}", document.getDriverId(), document.getDocumentId());
        DriverDocument driverDocument = documentService.saveDocument(document);
        return ResponseEntity.ok(driverDocument);
    }

    @GetMapping("/driver/document/{id}")
    public ResponseEntity<DriverDocument> getDriverDocument(@PathVariable("id") long documentId){
        log.info("Request Received to get driver Document for documentID : {}", documentId);
        DriverDocument driverDocument = documentService.getDocument(documentId);
        return ResponseEntity.ok(driverDocument);
    }

    @PutMapping("/driver/document/{id}")
    public ResponseEntity<DriverDocument> updateDriverDocument(@RequestBody DriverDocument driverDocument, @PathVariable("id") long documentId){
        log.info("Request Received to update driver Document for driverID : {} and documentID : {}", driverDocument.getDriverId(), documentId);
        DriverDocument document = documentService.updateDocument(driverDocument, documentId);
        return ResponseEntity.ok(document);
    }

    @GetMapping("/driver/{id}/documents")
    public ResponseEntity<List<DriverDocument>> getAllDriverDocuments(@PathVariable("id") long driverId){
        log.info("Request Received to get all Documents for driverID : {}", driverId);
        List<DriverDocument> driverDocuments = documentService.getAllDocuments(driverId);
        return ResponseEntity.ok(driverDocuments);
    }

    @PostMapping("/getDocumentUrl")
    public ResponseEntity<String> getDocumentUrl(@RequestParam long documentId){
        log.info("Request Received to get new Document URL for documentID : {}", documentId);
        String documentUrl = documentService.getDocumentUrl(documentId);
        log.info("Generated URL : {}", documentUrl);
        return ResponseEntity.ok(documentUrl);
    }

    @PostMapping("/updateDocumentStatus")
    public void updateDocumentStatus(@RequestParam long documentId, @RequestParam DocumentStatus documentStatus){
        log.info("Request Received to update Document status : {} for documentID : {}", documentStatus, documentId);
        documentService.updateDocumentStatus(documentId, documentStatus);
    }

}
