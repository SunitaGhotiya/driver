package com.uber.driver.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DriverVehicle {
    @Id
    private long driverId;
    private String vehicleNo;
    private String type;
    private String name;
    private String description;
}
