package com.senstile.receiveordersystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "com.senstile.receiveordersystem")
@EnableScheduling
public class OrderDeliverSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderDeliverSystemApplication.class);
    }
}
