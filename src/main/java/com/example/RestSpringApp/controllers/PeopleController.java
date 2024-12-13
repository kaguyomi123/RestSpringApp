package com.example.RestSpringApp.controllers;

import com.example.RestSpringApp.models.Person;
import com.example.RestSpringApp.services.PeopleService;
import com.example.RestSpringApp.util.PersonErrorResponse;
import com.example.RestSpringApp.util.PersonNotCreatedException;
import com.example.RestSpringApp.util.PersonNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PeopleController {

    PeopleService peopleService;

    @Autowired
    public PeopleController(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @GetMapping("/people")
    public List<Person> getPeople() {
        return peopleService.findAll();
    }

    @GetMapping("/people/{id}")
    public Person getPersonById(@PathVariable("id") int id) {
        return peopleService.findOne(id);
    }

    @PostMapping("/people")
    public ResponseEntity<HttpStatus> create (@RequestBody @Valid Person person,
                                              BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();

            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                errorMsg.append(error.getField())
                        .append(": ")
                        .append(error.getDefaultMessage())
                        .append(";");
            }
            throw new PersonNotCreatedException(errorMsg.toString());
        }

        peopleService.save(person);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler()
    private ResponseEntity<PersonErrorResponse> handleException (PersonNotFoundException e) {
        PersonErrorResponse response = new PersonErrorResponse(
                System.currentTimeMillis(),
                "Person with this id wasn't found"
                );

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler()
    private ResponseEntity<PersonErrorResponse> handleException (PersonNotCreatedException e) {
        PersonErrorResponse response = new PersonErrorResponse(
                System.currentTimeMillis(),
                e.getMessage()
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
