package com.uber.driver.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uber.driver.enums.DocumentStatus;
import com.uber.driver.enums.DocumentType;
import com.uber.driver.model.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Random;

public class TestUtil {
    public static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Address getAddress() {
        return Address.builder()
                .driverId(1)
                .addressLine1("123")
                .city("Bangalore")
                .country("India")
                .zipCode("560066")
                .build();
    }

    public static DriverDocument getDriverDocument(){
        return DriverDocument.builder()
                .driverId(1)
                .documentId(new Random().nextInt())
                .type(DocumentType.IDENTITY_PROOF.getType())
                .label(DocumentType.IDENTITY_PROOF.getLabel())
                .status(DocumentStatus.IN_REVIEW)
                .build();
    }

    public static DriverLicence getDriverLicence() throws ParseException {
        return DriverLicence.builder()
                .driverID(123)
                .licenceNo("L123")
                .expirationDate(new SimpleDateFormat("yyyy-MM-dd").parse("2030-12-31"))
                .build();
    }

    public static DriverVehicle getDriverVehicle(){
        return DriverVehicle.builder()
                .driverId(123)
                .vehicleNo("1503")
                .type("Four Wheeler")
                .build();
    }

    public static UberDriver getDriver(){
       return UberDriver.builder()
                .driverId(123)
                .city("Bangalore")
                .build();
    }

    public static DriverProfile getDriverProfile() throws ParseException {
        return DriverProfile.builder()
                .uberDriver(getDriver())
                .address(getAddress())
                .driverLicence(getDriverLicence())
                .driverVehicle(getDriverVehicle())
                .build();
    }



}
