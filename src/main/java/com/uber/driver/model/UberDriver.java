package com.uber.driver.model;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.uber.driver.enums.BackgroundCheckStatus;
import com.uber.driver.enums.TrackingDeviceStatus;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UberDriver {

    @Id
    @GeneratedValue(generator = "uuid-hibernate-generator")
    @GenericGenerator(name = "uuid-hibernate-generator", strategy = "org.hibernate.id.UUIDGenerator")
    private String driverId;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String emailAddress;
    private String profilePhoto;
    private String city;
    private int rating;
    @JsonFormat(pattern="dd-mm-yyyy")
    private Date dateOfBirth;
    private BackgroundCheckStatus backgroundCheckStatus;
    private TrackingDeviceStatus trackingDeviceStatus;
    private boolean isDocVerified;
    private boolean isOnboarded;
    private boolean isActive;
}
