package com.uber.driver.controller;

import com.uber.driver.enums.DocumentStatus;
import com.uber.driver.model.Address;
import com.uber.driver.model.DriverDocument;
import com.uber.driver.model.UrlResponse;
import com.uber.driver.service.DocumentService;
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

import java.util.ArrayList;

import static com.uber.driver.util.TestUtil.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = DocumentController.class)
public class DocumentControllerTest {

    @MockBean
    private DocumentService documentService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testSaveDriverDocument() throws Exception {
        DriverDocument document = getDriverDocument();
        Mockito.when(documentService.saveDocument(Mockito.any(DriverDocument.class))).thenReturn(document);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post("/driver/document")
                .content(asJsonString(document))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        JSONAssert.assertEquals(asJsonString(document), mvcResult.getResponse().getContentAsString(), false);
    }

    @Test
    public void testGetDriverDocument() throws Exception {
        DriverDocument document = getDriverDocument();
        Mockito.when(documentService.getDocument(Mockito.anyString())).thenReturn(document);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/driver/document/{id}", "123"))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        JSONAssert.assertEquals(asJsonString(document), mvcResult.getResponse().getContentAsString(), false);
    }


    @Test
    public void testUpdateDriverDocument() throws Exception {
        DriverDocument document = getDriverDocument();
        Mockito.when(documentService.updateDocument(Mockito.any(DriverDocument.class), Mockito.anyString())).thenReturn(document);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .put("/driver/document/{id}", "123")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(asJsonString(document)))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        JSONAssert.assertEquals(asJsonString(document), mvcResult.getResponse().getContentAsString(), false);
    }

    @Test
    public void testGetAllDriverDocuments() throws Exception {
        Mockito.when(documentService.getAllDocuments(Mockito.anyString())).thenReturn(new ArrayList<>());
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/driver/{id}/documents", "123")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        Assert.assertNotNull(mvcResult.getResponse());
    }

    @Test
    public void testGetDocumentUrl() throws Exception {
        UrlResponse urlResponse = new UrlResponse("docUrl");
        Mockito.when(documentService.getDocumentUrl()).thenReturn("docUrl");
        MvcResult mvcResult =  mockMvc.perform(MockMvcRequestBuilders
                .get("/getPresignedUrl")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        JSONAssert.assertEquals(asJsonString(urlResponse), mvcResult.getResponse().getContentAsString(), false);
    }

    @Test
    public void testUpdateDocumentStatus() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/updateDocumentStatus")
                .param("documentId", "123")
                .param("documentStatus", DocumentStatus.IN_REVIEW.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

    }

}
