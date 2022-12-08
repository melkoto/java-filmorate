package ru.yandex.practicum.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.security.auth.login.LoginContext;
import java.time.LocalDate;

@Data
public class User {
    @NotNull
    private int id;
    @Email(message = "Use valid email")
    private String email;
    @NotEmpty
    private String login;
    @NotEmpty
    private String name;
    @Past
    @JsonFormat(pattern = "dd.MM.yyyy")
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private LocalDate birthDate;
}
