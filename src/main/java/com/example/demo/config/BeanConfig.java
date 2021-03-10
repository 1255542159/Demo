package com.example.demo.config;

import com.example.demo.utils.SnowflakeIdWorker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author joy
 * @version 1.0
 * @date 2021/3/6 16:25
 */
@Configuration
public class BeanConfig {
    @Bean
    public SnowflakeIdWorker getIdWorker(){
        return new SnowflakeIdWorker(0,0);
    }
    @Bean
    public BCryptPasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
