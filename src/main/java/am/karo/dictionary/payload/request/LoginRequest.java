package am.karo.dictionary.payload.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginRequest {

    @NotBlank
    private String name;
    @NotBlank
    private String surname;
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
