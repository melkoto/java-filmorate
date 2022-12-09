package ru.yandex.practicum.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
public class User {
    @NotNull
    private int id;
    @Email(message = "Use valid email")
    private String email;
    private String name;
    @NotEmpty
    @NotNull
    private String login;
    @Past
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;
}
