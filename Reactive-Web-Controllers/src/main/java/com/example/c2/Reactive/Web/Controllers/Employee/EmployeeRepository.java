package com.example.c2.Reactive.Web.Controllers.Employee;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.Optional;

public interface EmployeeRepository extends ReactiveCrudRepository<Employee, Long> {

}
