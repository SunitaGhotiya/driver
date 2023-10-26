package com.uber.driver.controller;

import com.uber.driver.model.Address;
import com.uber.driver.service.AddressService;
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
import static com.uber.driver.util.TestUtil.getAddress;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = AddressController.class)
public class AddressControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AddressService addressService;

    @Test
    public void testSaveDriverAddress() throws Exception {
        Address address = getAddress();
        Mockito.when(addressService.saveAddress(Mockito.any(Address.class))).thenReturn(address);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post("/driver/address")
                .content(asJsonString(address))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        JSONAssert.assertEquals(asJsonString(address), mvcResult.getResponse().getContentAsString(), false);
    }

    @Test
    public void testGetDriverAddress() throws Exception {
        Address address = getAddress();
        Mockito.when(addressService.getAddress(Mockito.anyString())).thenReturn(address);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/driver/{id}/address", 123))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        JSONAssert.assertEquals(asJsonString(address), mvcResult.getResponse().getContentAsString(), false);
    }


    @Test
    public void testUpdateDriverAddress() throws Exception {
        Address address = getAddress();
        Mockito.when(addressService.updateAddress(Mockito.any(Address.class), Mockito.anyString())).thenReturn(address);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .put("/driver/{id}/address", 123)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(asJsonString(address)))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        JSONAssert.assertEquals(asJsonString(address), mvcResult.getResponse().getContentAsString(), false);
    }


}
