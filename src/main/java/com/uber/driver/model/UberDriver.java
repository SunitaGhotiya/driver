package com.uber.driver.model;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.uber.driver.enums.DriverComplianceStatus;
import lombok.*;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UberDriver {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long driverId;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String emailAddress;
    private String profilePhoto;
    private String city;
    private String activationStatus;
    private DriverComplianceStatus complianceStatus;
    private int rating;
    @JsonFormat(pattern="dd-mm-yyyy")
    Date dateOfBirth;
}
