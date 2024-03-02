package edu.example.hw1_s2.dto;


import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDto {

    @NotEmpty
    private String username;

    @NotEmpty
//    @PasswordComplexityConstraint
    private String password;
}
