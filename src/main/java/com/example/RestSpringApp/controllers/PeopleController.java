package com.example.RestSpringApp.controllers;

import com.example.RestSpringApp.dto.PersonDTO;
import com.example.RestSpringApp.models.Person;
import com.example.RestSpringApp.services.PeopleService;
import com.example.RestSpringApp.util.PersonErrorResponse;
import com.example.RestSpringApp.util.PersonNotCreatedException;
import com.example.RestSpringApp.util.PersonNotFoundException;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class PeopleController {

    private final PeopleService peopleService;
    private final ModelMapper modelMapper;

    @Autowired
    public PeopleController(PeopleService peopleService, ModelMapper modelMapper) {
        this.peopleService = peopleService;
        this.modelMapper = modelMapper;
    }


    @GetMapping("/people")
    public List<PersonDTO> getPeople() {
        return peopleService.findAll().stream()
                .map(this::convertToPersonDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/people/{id}")
    public PersonDTO getPersonById(@PathVariable("id") int id) {
        return convertToPersonDTO(peopleService.findOne(id));
    }

    @PostMapping("/people")
    public ResponseEntity<HttpStatus> create (@RequestBody @Valid PersonDTO personDTO,
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

        peopleService.save(convertToPerson(personDTO));

        return ResponseEntity.ok(HttpStatus.OK);
    }

    private Person convertToPerson(PersonDTO personDTO) {
        return modelMapper.map(personDTO, Person.class);
    }

    private PersonDTO convertToPersonDTO(Person person) {
        return modelMapper.map(person, PersonDTO.class);
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
