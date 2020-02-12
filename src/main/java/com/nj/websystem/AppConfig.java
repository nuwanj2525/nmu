package com.nj.websystem;

import org.postgresql.ds.PGPoolingDataSource;
import org.postgresql.ds.PGSimpleDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@EnableCaching
//@Profile("!test")
public class AppConfig {
    static Logger logger = LoggerFactory.getLogger(AppConfig.class);




/*    @Bean
    public DataSource dataSource(){
        PGPoolingDataSource source = new PGPoolingDataSource();
        //source.setDataSourceName(url);
        source.setServerName("localhost");
        source.setDatabaseName("njsysdb");
        source.setUser("postgres");
        source.setPassword("123456");
        source.setMaxConnections(10);
        return source;
    }*/


/*
    @Value("oracle.net.wallet.location")
    private String walletLocation;

    @Value("oracle.net.wallet.url")
    private String walletUrl;



   @Bean
    @ConfigurationProperties
    public DataSource dataSource(){
        PGSimpleDataSource  ds = null;
        ds = new PGSimpleDataSource();
        Properties prop = new Properties();
        prop.forEach((propertyKey, propertyValue) -> {
            try {
                ds.setProperty(propertyKey, propertyValue);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

    }*/

/*    private static DataSource postgreDataSource() {
        PGSimpleDataSource  ds = null;
        synchronized (LOCK) {
            if (ds == null) {
                final PGSimpleDataSource pgDataSource = new PGSimpleDataSource();

                pgDataSource.setServerName("localhost");
                pgDataSource.setPortNumber(5432);
                pgDataSource.setDatabaseName("iws");
                pgDataSource.setUser("iws_user");
                pgDataSource.setPassword("iws");

                ds = pgDataSource;
            }

            return dataSource;
        }
    }*/



}
