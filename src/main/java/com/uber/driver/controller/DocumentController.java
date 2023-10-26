package com.uber.driver.controller;

import com.uber.driver.enums.DocumentStatus;
import com.uber.driver.model.DriverDocument;
import com.uber.driver.model.UrlResponse;
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
    public ResponseEntity<DriverDocument> getDriverDocument(@PathVariable("id") String documentId){
        log.info("Request Received to get driver Document for documentID : {}", documentId);
        DriverDocument driverDocument = documentService.getDocument(documentId);
        return ResponseEntity.ok(driverDocument);
    }

    @PutMapping("/driver/document/{id}")
    public ResponseEntity<DriverDocument> updateDriverDocument(@RequestBody DriverDocument driverDocument, @PathVariable("id") String documentId){
        log.info("Request Received to update driver Document for driverID : {} and documentID : {}", driverDocument.getDriverId(), documentId);
        DriverDocument document = documentService.updateDocument(driverDocument, documentId);
        return ResponseEntity.ok(document);
    }

    @GetMapping("/driver/{id}/documents")
    public ResponseEntity<List<DriverDocument>> getAllDriverDocuments(@PathVariable("id") String driverId){
        log.info("Request Received to get all Documents for driverID : {}", driverId);
        List<DriverDocument> driverDocuments = documentService.getAllDocuments(driverId);
        return ResponseEntity.ok(driverDocuments);
    }

    @PostMapping("/updateDocumentUrl")
    public void updateDocumentUrl(@RequestParam String documentId, @RequestParam String documentURL){
        log.info("Request Received to update Document URl : {} for documentID : {}", documentURL, documentId);
        documentService.updateDocumentUrl(documentId, documentURL);
    }

    @GetMapping("/getPresignedUrl")
    public ResponseEntity<UrlResponse> getPresignedUrl(){
        log.info("Request Received to get presigned URL");
        String documentUrl = documentService.getDocumentUrl();
        log.info("Generated URL : {}", documentUrl);
        return ResponseEntity.ok(new UrlResponse(documentUrl));
    }

    @PostMapping("/updateDocumentStatus")
    public void updateDocumentStatus(@RequestParam String documentId, @RequestParam DocumentStatus documentStatus){
        log.info("Request Received to update Document status : {} for documentID : {}", documentStatus, documentId);
        documentService.updateDocumentStatus(documentId, documentStatus);
    }

}
