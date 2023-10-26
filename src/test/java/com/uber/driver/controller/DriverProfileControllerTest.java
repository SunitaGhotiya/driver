package com.uber.driver.controller;


import com.uber.driver.model.DriverProfile;
import com.uber.driver.service.DriverProfileService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.uber.driver.util.TestUtil.asJsonString;
import static com.uber.driver.util.TestUtil.getDriverProfile;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = DriverProfileController.class)
public class DriverProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DriverProfileService driverProfileService;

    @Test
    public void testGetDriverProfile() throws Exception {
        DriverProfile driverProfile = getDriverProfile();
        Mockito.when(driverProfileService.getDriverProfile(Mockito.anyString())).thenReturn(driverProfile);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/driver/{id}/profile", 123))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        JSONAssert.assertEquals(asJsonString(driverProfile), mvcResult.getResponse().getContentAsString(), false);
    }

}
