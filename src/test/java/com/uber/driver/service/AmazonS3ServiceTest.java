package com.uber.driver.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;

@RunWith(MockitoJUnitRunner.class)
public class AmazonS3ServiceTest {

    @InjectMocks
    private AmazonS3Service amazonS3Service;

    @Mock
    private AmazonS3 amazonS3;

    @Test
    public void generateUrl() throws MalformedURLException {
        URL url = new URL("http://driverbucket.s3.amazonaws.com/fileName");
        Mockito.when(amazonS3.generatePresignedUrl(anyString(), anyString(), any(Date.class), any(HttpMethod.class)))
                .thenReturn(url);

        ReflectionTestUtils.setField(amazonS3Service, "s3BucketName", "bucket");
        Assert.assertEquals(url.toString(), amazonS3Service.generateUrl("fineName"));
    }

}
