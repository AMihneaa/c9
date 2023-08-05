package com.example.c2.Reactive.Web.Controllers.Home;

import com.example.c2.Reactive.Web.Controllers.Employee.Employee;
import com.example.c2.Reactive.Web.Controllers.Employee.EmployeeRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;

@Controller
public class HomeController {

    private final EmployeeRepository repository;

    public HomeController(EmployeeRepository repository){
        this.repository = repository;
    }

    @GetMapping("/")
    public Mono<Rendering> index(){
        return repository
                .findAll()
                .collectList()
                .map(employees ->
                        Rendering.view("index")
                                .modelAttribute("employees", employees)
                                .modelAttribute("newEmployee", new Employee("", ""))
                                .build()
                );
    }

    @PostMapping("/new-employee")
    Mono<String> newEmployee(@ModelAttribute Mono<Employee> newEmployee){
        return newEmployee.flatMap(employee -> {
            Employee saveEmployee = new Employee(employee.getName(), employee.getRole());
            return repository.save(saveEmployee);
        }).map(employee -> "redirect:/");
    }

}
