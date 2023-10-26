package com.uber.driver.model;

import com.uber.driver.enums.DocumentStatus;
import com.uber.driver.enums.DocumentType;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

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
    @GeneratedValue(generator = "uuid-hibernate-generator")
    @GenericGenerator(name = "uuid-hibernate-generator", strategy = "org.hibernate.id.UUIDGenerator")
    private String documentId;

    private String driverId;
    private String type;
    private String label;
    private String location;
    private DocumentStatus status;
    private String statusDetails;

    public DriverDocument(String driverId, DocumentType documentType){
        this.driverId = driverId;
        this.type = documentType.getType();
        this.label = documentType.getLabel();
        this.status = DocumentStatus.PENDING;
    }
}
