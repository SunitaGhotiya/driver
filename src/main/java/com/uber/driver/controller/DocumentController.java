package com.uber.driver.controller;

import com.uber.driver.enums.DocumentStatus;
import com.uber.driver.model.DriverDocument;
import com.uber.driver.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @PostMapping("/driver/document")
    public ResponseEntity<DriverDocument> saveDriverDocument(@RequestBody DriverDocument document){
        DriverDocument driverDocument = documentService.saveDocument(document);
        if(Objects.nonNull(driverDocument))
            return ResponseEntity.ok(driverDocument);
        else
            return ResponseEntity.notFound().build();
    }

    @GetMapping("/driver/document/{id}")
    public ResponseEntity<DriverDocument> getDriverDocument(@PathVariable("id") long documentId){
        DriverDocument driverDocument = documentService.getDocument(documentId);
        if(Objects.nonNull(driverDocument))
            return ResponseEntity.ok(driverDocument);
        else
            return ResponseEntity.notFound().build();
    }

    @PutMapping("/driver/document/{id}")
    public ResponseEntity<DriverDocument> updateDriverDocument(@RequestBody DriverDocument driverDocument, @PathVariable("id") long documentId){
        DriverDocument driver = documentService.updateDocument(driverDocument, documentId);
        if(Objects.nonNull(driver))
            return ResponseEntity.ok(driver);
        else
            return ResponseEntity.notFound().build();
    }

    @GetMapping("/driver/{id}/documents")
    public ResponseEntity<List<DriverDocument>> getAllDriverDocuments(@PathVariable("id") long driverId){
        List<DriverDocument> driverDocuments = documentService.getAllDocuments(driverId);
        return ResponseEntity.ok(driverDocuments);
    }

    @PostMapping("/getDocumentUrl")
    public ResponseEntity<String> getDocumentUrl(@RequestParam long documentId){
        String documentUrl = documentService.getDocumentUrl(documentId);
        return ResponseEntity.ok(documentUrl);
    }

    @PostMapping("/updateDocumentStatus")
    public void updateDocumentStatus(@RequestParam long documentId, @RequestParam DocumentStatus documentStatus){
        documentService.updateDocumentStatus(documentId, documentStatus);
    }

}
