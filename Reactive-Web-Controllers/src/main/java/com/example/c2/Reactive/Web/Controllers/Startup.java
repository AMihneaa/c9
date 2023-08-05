package com.example.c2.Reactive.Web.Controllers;

import com.example.c2.Reactive.Web.Controllers.Employee.Employee;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import reactor.test.StepVerifier;

@Configuration
public class Startup {

    @Bean
    CommandLineRunner initDatabase(R2dbcEntityTemplate template){
        return args -> {
            template.getDatabaseClient() //
                    .sql("CREATE TABLE EMPLOYEE (id IDENTITY NOT NULL PRIMARY KEY , name VARCHAR(255), role VARCHAR(255))") //
                    .fetch() //
                    .rowsUpdated() //
                    .as(StepVerifier::create) //
                    .expectNextCount(1) //
                    .verifyComplete();

            template.insert(Employee.class)
                    .using(new Employee("Mihnea", "ROLE1"))
                    .as(StepVerifier::create)
                    .expectNextCount(1)
                    .verifyComplete();

            template.insert(Employee.class)
                    .using(new Employee("Stefan", "ROLE2"))
                    .as(StepVerifier::create)
                    .expectNextCount(1)
                    .verifyComplete();

            template.insert(Employee.class)
                    .using(new Employee("Ionut", "ROLE3"))
                    .as(StepVerifier::create)
                    .expectNextCount(1)
                    .verifyComplete();
        };
    }

}
