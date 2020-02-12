package com.nj.websystem;

import com.nj.websystem.enums.UserRoles;
import com.nj.websystem.model.UserAdmin;
import com.nj.websystem.service.UserAdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@EnableJpaRepositories("com.nj.websystem.service")
@SpringBootApplication
@EnableTransactionManagement
@EnableScheduling
@Configuration

public class WebSystemApplication {

    static Logger logger = LoggerFactory.getLogger(WebSystemApplication.class);

    @Autowired
    private UserAdminService services;

    public static void main(String[] args) {
        String sysHome = System.getProperty("HOST_HOME");
        String sysEnv = System.getProperty("HOST_ENV");
        String pidName = sysHome + "/apphome/" + sysEnv + ".pid";

        SpringApplication sApp = new SpringApplication(WebSystemApplication.class);
        sApp.addListeners(new ApplicationPidFileWriter(pidName));
        sApp.run(args).registerShutdownHook();
    }

    @Bean
    public CommandLineRunner CommandLineRunner() {
        System.out.println("System starting ......");
        List<UserAdmin> adminUsers = new ArrayList<>();
        adminUsers.add(new UserAdmin(1L, "admin", "admin", "admin", "admin", UserRoles.ADMIN, "admin", new Date(), "system"));
        adminUsers.add(new UserAdmin(2L, "manjula", "Manjula", "Ranathunga", "manjula", UserRoles.ADMIN, "admin", new Date(), "system"));
        adminUsers.add(new UserAdmin(3L, "nuwan", "Nuwan", "Jayasooriya", "nuwan", UserRoles.ADMIN, "admin", new Date(), "system"));
        adminUsers.add(new UserAdmin(4L, "test", "Test", "User", "test", UserRoles.USER, "admin", new Date(), "system"));
        services.saveAll(adminUsers);
        return args -> {
            logger.info("Started ...");
        };
    }

    CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(Arrays.asList(
                new ConcurrentMapCache("onlineStatus"),
                new ConcurrentMapCache("syncSummeryStatus")
        ));
        return cacheManager;
    }

    @CacheEvict(allEntries = true, value = {"onlineStatus"})
    @Scheduled(fixedDelay = 15 * 60 * 1000)
    public void evicOnlineStatus() {
        logger.debug("Flushed online status cached ...");
    }

    @CacheEvict(allEntries = true, value = {"syncSummeryStatus"})
    @Scheduled(fixedDelay = 15 * 60 * 1000)
    public void syncSummeryStatus() {
        logger.debug("Flushed Sync Summery status cached ...");
    }


}
