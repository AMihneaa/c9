package com.example.c2.Reactive.Web.Controllers.API;

import com.example.c2.Reactive.Web.Controllers.Employee.Employee;
import com.example.c2.Reactive.Web.Controllers.Employee.EmployeeRepository;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class ApiController {
    public static Map<String, Employee> DATABASE = new LinkedHashMap<>();

    private final EmployeeRepository repository;

    public ApiController(EmployeeRepository repository) {
        this.repository = repository;
    }

//
//    @GetMapping("/api/employees")
//    Flux<Employee> employees() {
//        return Flux.just( //
//                new Employee("Mihnea", "management"), //
//                new Employee("Stefan", "payroll"));
//    }
//
//    @PostMapping("/api/employees")
//    Mono<Employee> add(@RequestBody Mono<Employee> newEmployee){
//        return newEmployee.map(employee -> {
//            DATABASE.put(employee.name(), employee);
//            return employee;
//        });
//    }

    @GetMapping("/api/employees")
    Flux<Employee> employees(){
        return repository.findAll();
    }

    @PostMapping("/api/employees")
    Mono<Employee> add(@RequestBody Mono<Employee> newEmployee){
        return newEmployee.flatMap(employee -> {
            Employee emploteeToLoad = new Employee(employee.getName(), employee.getRole());
            return repository.save(emploteeToLoad);
        });
    }

}
