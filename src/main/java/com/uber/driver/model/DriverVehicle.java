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
    long driverId;
    String vehicleNo;
    String type;
    String name;
    String description;
}
