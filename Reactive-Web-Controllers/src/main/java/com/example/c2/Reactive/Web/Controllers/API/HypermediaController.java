package com.example.c2.Reactive.Web.Controllers.API;

import com.example.c2.Reactive.Web.Controllers.Employee.Employee;
import com.example.c2.Reactive.Web.Controllers.Employee.EmployeeRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkRelation;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.hateoas.server.core.Relation;

import java.nio.file.LinkOption;

import static org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType.HAL;

@RestController
@EnableHypermediaSupport(type = HAL)
public class HypermediaController {

    private final EmployeeRepository repository;

    public HypermediaController(EmployeeRepository repository){
        this.repository = repository;
    }

    @GetMapping("/hypermedia/employees/{key}")
    Mono<EntityModel<Employee>> employee(@PathVariable("key") Long key){
        Mono<Link> selfLink = Mono.just(linkTo(methodOn(HypermediaController.class).employee(key)).withSelfRel());

        Mono<Link> aggregateRoot = Mono.just(linkTo(methodOn(HypermediaController.class).employees()).withRel(LinkRelation.of("employees")));

        Mono<Tuple2<Link, Link>> links = Mono.zip(selfLink, aggregateRoot);

        return repository.findById(key)
                .flatMap(employee -> links.map(objects -> EntityModel.of(employee, objects.getT1(), objects.getT2())));
    }

    @GetMapping("/hypermedia/employees")
    Mono<CollectionModel<EntityModel<Employee>>> employees(){
        Mono<Link> selfLink = Mono.just(linkTo(methodOn(HypermediaController.class).employees()).withSelfRel());

        return selfLink
                .flatMap(self -> repository.findAll()
                        .flatMap(employee -> employee(employee.getId()))
                        .collectList().map(entityModels -> CollectionModel.of(entityModels, self)));
    }
}
