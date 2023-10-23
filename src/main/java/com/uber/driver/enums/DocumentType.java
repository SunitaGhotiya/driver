package com.uber.driver.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public enum DocumentType {
    IDENTITY_PROOF("IDENTITY_PROOF", "Aadhar Card"),
    PROFILE_PHOTO("PROFILE_PHOTO", "Profile Photo"),
    DRIVER_LICENCE("DRIVER_LICENCE", "Driver Licence"),
    PAN_CARD("PAN_CARD", "Pan Card"),
    REGISTRATION_CERTIFICATE("REGISTRATION_CERTIFICATE", "Registeration Cretificate"),
    VEHICLE_INSURANCE("VEHICLE_INSURANCE", "Vehicel Insurence"),
    VEHICLE_PERMIT("VEHICLE_PERMIT", "Vehicle Permit");

    private final String type;
    private final String label;

}
