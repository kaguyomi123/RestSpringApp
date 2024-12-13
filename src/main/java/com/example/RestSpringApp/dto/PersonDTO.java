package com.example.RestSpringApp.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class PersonDTO {

    @NotEmpty(message = "name should not be empty")
    @Size(min = 2, max = 30, message = "name should be between 2 and 30 chars")
    private String name;

    @Min(value = 0, message = "should be > 0")
    private int age;

    @Email
    @NotEmpty(message = "should not be empty")
    private String email;

    public @NotEmpty(message = "name should not be empty") @Size(min = 2, max = 30, message = "name should be between 2 and 30 chars") String getName() {
        return name;
    }

    public void setName(@NotEmpty(message = "name should not be empty") @Size(min = 2, max = 30, message = "name should be between 2 and 30 chars") String name) {
        this.name = name;
    }

    @Min(value = 0, message = "should be > 0")
    public int getAge() {
        return age;
    }

    public void setAge(@Min(value = 0, message = "should be > 0") int age) {
        this.age = age;
    }

    public @Email @NotEmpty(message = "should not be empty") String getEmail() {
        return email;
    }

    public void setEmail(@Email @NotEmpty(message = "should not be empty") String email) {
        this.email = email;
    }
}
