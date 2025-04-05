package com.volunteerplatform.config.cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration

public class CloudinaryConfig {
@Bean
    public Cloudinary  cloudinary(CloudinaryConfigProperties cloudinaryConfigProperties){
    System.out.println("Cloud Name: " + cloudinaryConfigProperties.getCloudName());
    System.out.println("API Key: " + cloudinaryConfigProperties.getApiKey());
    System.out.println("API Secret: " + cloudinaryConfigProperties.getApiSecret());

    return new Cloudinary(ObjectUtils.asMap(
                "api_key", cloudinaryConfigProperties.getApiKey(),
                "api_secret", cloudinaryConfigProperties.getApiSecret(),
                "cloud_name", cloudinaryConfigProperties.getCloudName()
        ));
    }
}