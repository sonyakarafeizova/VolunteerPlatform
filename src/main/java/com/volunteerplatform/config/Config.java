package com.volunteerplatform.config;

import com.volunteerplatform.model.User;
import com.volunteerplatform.web.dto.UserRegisterDTO;
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

       // Explicitly map fullName
       modelMapper.typeMap(UserRegisterDTO.class, User.class).addMappings(mapper ->
               mapper.map(UserRegisterDTO::getFullName, User::setFullName)
       );

       return modelMapper;
   }
}
