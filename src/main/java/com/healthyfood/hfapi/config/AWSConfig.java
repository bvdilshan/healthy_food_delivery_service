package com.healthyfood.hfapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class AWSConfig {
    @Value("${aws.access.key:}")
    private String accessKey;
    @Value("${aws.secret.key:}")
    private String secretKey;
    @Value("${aws.region:us-east-1}")
    private String region;

    @Bean
    @ConditionalOnProperty(name = {"aws.access.key", "aws.secret.key"}, matchIfMissing = false)
    public S3Client s3client(){
        if (accessKey == null || accessKey.trim().isEmpty() || secretKey == null || secretKey.trim().isEmpty()) {
            throw new IllegalStateException("AWS Access Key and Secret Key must not be blank");
        }
        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
                .build();
    }
}