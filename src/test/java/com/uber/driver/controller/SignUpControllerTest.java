//package com.uber.driver.controller;
//
//import com.uber.driver.constant.DriverConstants;
//import com.uber.driver.service.SignUpService;
//import org.junit.Assert;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//
//@WebMvcTest
//@AutoConfigureMockMvc(addFilters = false)
//@ContextConfiguration(classes = SignUpController.class)
//public class SignUpControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private SignUpService signUpService;
//
//
//    @Test
//    public void testGenerateOtp() throws Exception {
//        Mockito.when(signUpService.generateAndSendOTP(Mockito.anyString())).thenReturn(DriverConstants.OTP_GENERATED_SUCCESS);
//        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
//                .post("/signup/generateOtp")
//                .param("phoneNumber", "9876543210"))
//                .andExpect(status().is2xxSuccessful())
//                .andReturn();
//
//        Assert.assertEquals(DriverConstants.OTP_GENERATED_SUCCESS, mvcResult.getResponse().getContentAsString());
//    }
//
//    @Test
//    public void testValidateOtp() throws Exception {
//        Mockito.when(signUpService.validateOTPAndGenerateToken(Mockito.anyString(), Mockito.anyInt())).thenReturn("token123");
//        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
//                .post("/signup/validateOtpAndGenerateToken")
//                .param("phoneNumber", "9876543210")
//                .param("otp", "1234")
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().is2xxSuccessful())
//                .andReturn();
//
//        Assert.assertEquals("token123", mvcResult.getResponse().getContentAsString());
//    }
//
//}
