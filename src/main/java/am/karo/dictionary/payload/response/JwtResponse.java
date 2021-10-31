package am.karo.dictionary.payload.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class JwtResponse {

    private String token;
    private String type = "Bearer";
    private String refreshToken;
    private Long id;
    private String username;
    private String email;
    private List<String> authorities;

    public JwtResponse(String accessToken, String refreshToken, Long id, String username, String email, List<String> authorities) {
        this.token = accessToken;
        this.refreshToken = refreshToken;
        this.id = id;
        this.username = username;
        this.email = email;
        this.authorities = authorities;
    }

}
