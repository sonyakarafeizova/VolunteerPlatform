package com.volunteerplatform.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class Config {
   @Bean
   public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
   }

   @Bean
    public ModelMapper mapper() {
       ModelMapper modelMapper = new ModelMapper();

       return modelMapper;
   }
}
