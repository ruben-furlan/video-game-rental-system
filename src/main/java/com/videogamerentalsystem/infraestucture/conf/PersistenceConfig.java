package com.videogamerentalsystem.infraestucture.conf;

import javax.sql.DataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

@Configuration
public class PersistenceConfig {
    private static final String ORG_H_2_DRIVER = "org.h2.Driver";
    private static final String DB_JDBC_H_2_MEM_TEST = "jdbc:h2:mem:testdb";
    private static final String DB_USER_NAME = "SA";
    private static final String DB_PASS = "";

    @Profile("test")
    @Bean
    public DataSource testDataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName(ORG_H_2_DRIVER);
        dataSourceBuilder.url(DB_JDBC_H_2_MEM_TEST);
        dataSourceBuilder.username(DB_USER_NAME);
        dataSourceBuilder.password(DB_PASS);
        return dataSourceBuilder.build();
    }

   @Bean
    public DataSourceInitializer dataSourceInitializer(DataSource dataSource) {
        DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(dataSource);
        return initializer;
    }

}
