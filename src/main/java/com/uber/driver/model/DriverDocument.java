package com.uber.driver.model;

import com.uber.driver.enums.DocumentStatus;
import com.uber.driver.enums.DocumentType;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DriverDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    String documentId;
    long driverId;
    String type;
    String label;
    String location;
    DocumentStatus status;
    String statusDetails;

    public DriverDocument(long driverId, DocumentType documentType){
        this.driverId = driverId;
        this.type = documentType.getType();
        this.label = documentType.getLabel();
        this.status = DocumentStatus.PENDING;
    }
}
