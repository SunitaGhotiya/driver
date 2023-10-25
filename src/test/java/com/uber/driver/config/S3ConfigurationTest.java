package com.uber.driver.config;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

@RunWith(MockitoJUnitRunner.class)
public class S3ConfigurationTest {

    ApplicationContextRunner context = new ApplicationContextRunner().withUserConfiguration(S3Configuration.class);

    @Test
    public void testGetAmazonS3Client() {
        context.run(it -> Assertions.assertThat(it).hasSingleBean(S3Configuration.class));
    }

}