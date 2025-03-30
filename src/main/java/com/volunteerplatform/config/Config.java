package com.volunteerplatform.config;

import com.volunteerplatform.data.RoleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

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

    @Bean
    public DataSourceInitializer dataSourceInitializer(DataSource dataSource,
                                                       RoleRepository roleRepository,
                                                       ResourceLoader resourceLoader) {
        DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(dataSource);

        if (roleRepository.count() == 0) {
            ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
            populator.addScript(resourceLoader.getResource("classpath:data.sql"));
            initializer.setDatabasePopulator(populator);
        }

        return initializer;
    }
}
