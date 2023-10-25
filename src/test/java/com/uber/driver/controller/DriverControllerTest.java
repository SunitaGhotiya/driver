package com.uber.driver.controller;

import com.uber.driver.constant.DriverConstants;
import com.uber.driver.model.UberDriver;
import com.uber.driver.service.DriverService;
import org.junit.Assert;
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
import static com.uber.driver.util.TestUtil.getDriver;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = DriverController.class)
public class DriverControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DriverService driverService;

    @Test
    public void testSaveDriver() throws Exception {
        UberDriver driver = getDriver();
        Mockito.when(driverService.saveDriver(Mockito.any(UberDriver.class))).thenReturn(driver);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post("/driver")
                .content(asJsonString(driver))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        JSONAssert.assertEquals(asJsonString(driver), mvcResult.getResponse().getContentAsString(), false);
    }

    @Test
    public void testGetDriver() throws Exception {
        UberDriver driver = getDriver();
        Mockito.when(driverService.getDriver(Mockito.anyLong())).thenReturn(driver);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/driver/{id}", 123))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        JSONAssert.assertEquals(asJsonString(driver), mvcResult.getResponse().getContentAsString(), false);
    }


    @Test
    public void testGetDriverUsingPhoneNumber() throws Exception {
        UberDriver driver = getDriver();
        Mockito.when(driverService.getDriverByPhoneNumber(Mockito.anyString())).thenReturn(driver);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/driver")
                .param("phoneNumber", "9876543210")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(asJsonString(driver)))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        Assert.assertEquals("driverId : "+driver.getDriverId(), mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void testUpdateDriverAddress() throws Exception {
        UberDriver driver = getDriver();
        Mockito.when(driverService.updateDriver(Mockito.any(UberDriver.class), Mockito.anyLong())).thenReturn(driver);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .put("/driver/{id}", 123)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(asJsonString(driver)))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        JSONAssert.assertEquals(asJsonString(driver), mvcResult.getResponse().getContentAsString(), false);
    }



    @Test
    public void testUpdateDriverStatus() throws Exception {
        UberDriver driver = getDriver();

        Mockito.when(driverService.updateActivationStatus(Mockito.anyLong(), Mockito.anyString())).thenReturn(driver);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .patch("/updateActivationStatus")
                .param("driverId", "123")
                .param("activationStatus", "Active")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        JSONAssert.assertEquals(asJsonString(driver), mvcResult.getResponse().getContentAsString(), false);
    }


}
