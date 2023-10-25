package com.uber.driver.controller;

import com.uber.driver.model.DriverVehicle;
import com.uber.driver.service.VehicleService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.uber.driver.util.TestUtil.asJsonString;
import static com.uber.driver.util.TestUtil.getDriverVehicle;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = VehicleController.class)
public class VehicleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VehicleService vehicleService;

    @Test
    public void testSaveDriverLicence_DriverExists() throws Exception {
        DriverVehicle driverVehicle = getDriverVehicle();
        Mockito.when(vehicleService.saveVehicle(Mockito.any(DriverVehicle.class))).thenReturn(driverVehicle);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post("/driver/vehicle")
                .content(asJsonString(driverVehicle))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        JSONAssert.assertEquals(asJsonString(driverVehicle), mvcResult.getResponse().getContentAsString(), false);
    }

    @Test
    public void testSaveDriverLicence_DriverDoesNotExists() throws Exception {
        DriverVehicle driverVehicle = getDriverVehicle();
        Mockito.when(vehicleService.saveVehicle(Mockito.any(DriverVehicle.class))).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders
                .post("/driver/vehicle")
                .content(asJsonString(driverVehicle))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

    }

    @Test
    public void testGetDriverLicence_LicenceExists() throws Exception {
        DriverVehicle driverVehicle = getDriverVehicle();
        Mockito.when(vehicleService.getVehicle(Mockito.anyLong())).thenReturn(driverVehicle);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/driver/{id}/vehicle", 123))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        JSONAssert.assertEquals(asJsonString(driverVehicle), mvcResult.getResponse().getContentAsString(), false);
    }


    @Test
    public void testGetDriverLicence_LicenceDoesNotExists() throws Exception {
        Mockito.when(vehicleService.getVehicle(Mockito.anyLong())).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders
                .get("/driver/{id}/vehicle", 234))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void testUpdateDriverAddress_DriverExists() throws Exception {
        DriverVehicle driverVehicle = getDriverVehicle();
        Mockito.when(vehicleService.updateVehicle(Mockito.any(DriverVehicle.class), Mockito.anyLong())).thenReturn(driverVehicle);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .put("/driver/{id}/vehicle", 123)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(asJsonString(driverVehicle)))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        JSONAssert.assertEquals(asJsonString(driverVehicle), mvcResult.getResponse().getContentAsString(), false);
    }

    @Test
    public void testUpdateDriverAddress_DriverDoesNotExists() throws Exception {
        DriverVehicle driverVehicle = getDriverVehicle();
        Mockito.when(vehicleService.updateVehicle(Mockito.any(DriverVehicle.class), Mockito.anyLong())).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders
                .put("/driver/{id}/vehicle", 234)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(asJsonString(driverVehicle)))
                .andExpect(status().isNotFound())
                .andReturn();

    }

}
