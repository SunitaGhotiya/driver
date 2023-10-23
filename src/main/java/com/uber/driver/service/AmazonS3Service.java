package com.uber.driver.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.Calendar;
import java.util.Date;

@Component
public class AmazonS3Service {

    @Autowired
    private AmazonS3 amazonS3;

    @Value("${s3.bucket.name}")
    private String s3BucketName;

    public String generateUrl(String fileName) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, 5);
        URL url = amazonS3.generatePresignedUrl(s3BucketName, fileName, calendar.getTime(), HttpMethod.PUT);
        return url.toString();
    }

}
