package com.uber.driver.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DriverLicence {
    @Id
    long driverID;
    String licenceNo;
    @JsonFormat(pattern="dd-mm-yyyy")
    Date expirationDate;
}
