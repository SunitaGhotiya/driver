package com.uber.driver.controller;

import com.uber.driver.constant.DriverConstants;
import com.uber.driver.model.UserOtpAuthentication;
import com.uber.driver.model.UserToken;
import com.uber.driver.service.SignUpService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = SignUpController.class)
public class SignUpControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SignUpService signUpService;


    @Test
    public void testGenerateOtp() throws Exception {
        UserOtpAuthentication userOtpAuthentication = new UserOtpAuthentication("9876543210", 1234);
        Mockito.when(signUpService.generateAndSendOTP(Mockito.anyString())).thenReturn(userOtpAuthentication);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post("/signup/generateOtp")
                .param("phoneNumber", "9876543210"))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        JSONAssert.assertEquals(asJsonString(userOtpAuthentication), mvcResult.getResponse().getContentAsString(), false);
    }

    @Test
    public void testValidateOtp() throws Exception {
        UserToken userToken = new UserToken("9876543210", "token123");

        Mockito.when(signUpService.validateOTPAndGenerateToken(Mockito.anyString(), Mockito.anyInt())).thenReturn(userToken);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post("/signup/validateOtpAndGenerateToken")
                .param("phoneNumber", "9876543210")
                .param("otp", "1234")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        JSONAssert.assertEquals(asJsonString(userToken), mvcResult.getResponse().getContentAsString(), false);
    }

}
