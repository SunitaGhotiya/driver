package com.uber.driver.controller;

import com.uber.driver.model.Address;
import com.uber.driver.model.DriverLicence;
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

import static com.uber.driver.util.TestUtil.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = LicenceController.class)
public class LicenceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private com.uber.driver.service.LicenceService licenceService;

    @Test
    public void testSaveDriverLicence() throws Exception {
        DriverLicence driverLicence = getDriverLicence();
        Mockito.when(licenceService.saveLicence(Mockito.any(DriverLicence.class))).thenReturn(driverLicence);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post("/driver/licence")
                .content(asJsonString(driverLicence))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        JSONAssert.assertEquals(asJsonString(driverLicence), mvcResult.getResponse().getContentAsString(), false);
    }

    @Test
    public void testGetDriverLicence() throws Exception {
        DriverLicence driverLicence = getDriverLicence();
        Mockito.when(licenceService.getLicence(Mockito.anyLong())).thenReturn(driverLicence);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/driver/{id}/licence", 123))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        JSONAssert.assertEquals(asJsonString(driverLicence), mvcResult.getResponse().getContentAsString(), false);
    }

    @Test
    public void testUpdateDriverAddress() throws Exception {
        DriverLicence driverLicence = getDriverLicence();
        Mockito.when(licenceService.updateLicence(Mockito.any(DriverLicence.class), Mockito.anyLong())).thenReturn(driverLicence);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .put("/driver/{id}/licence", 123)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(asJsonString(driverLicence)))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        JSONAssert.assertEquals(asJsonString(driverLicence), mvcResult.getResponse().getContentAsString(), false);
    }

}
