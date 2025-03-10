package com.volunteerplatform.config.cloudinary;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.cloudinary.utils.ObjectUtils;

@Configuration
public class CloudinaryConfig {
@Bean
    public Cloudinary  cloudinary(CloudinaryConfigProperties cloudinaryConfigProperties){
        return new Cloudinary(ObjectUtils.asMap(
                "api_key", cloudinaryConfigProperties.getApiKey(),
                "api_secret", cloudinaryConfigProperties.getApiSecret(),
                "cloud_name", cloudinaryConfigProperties.getCloudName()
        ));
    }
}