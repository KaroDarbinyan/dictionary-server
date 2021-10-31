package am.karo.dictionary.controller;

import am.karo.dictionary.entity.RefreshToken;
import am.karo.dictionary.entity.Authority;
import am.karo.dictionary.entity.Authorities;
import am.karo.dictionary.entity.User;
import am.karo.dictionary.exception.TokenRefreshException;
import am.karo.dictionary.payload.request.LogOutRequest;
import am.karo.dictionary.payload.request.LoginRequest;
import am.karo.dictionary.payload.request.SignupRequest;
import am.karo.dictionary.payload.request.TokenRefreshRequest;
import am.karo.dictionary.payload.response.JwtResponse;
import am.karo.dictionary.payload.response.MessageResponse;
import am.karo.dictionary.payload.response.TokenRefreshResponse;
import am.karo.dictionary.repository.AuthorityRepository;
import am.karo.dictionary.repository.UserRepository;
import am.karo.dictionary.security.jwt.JwtUtils;
import am.karo.dictionary.security.service.RefreshTokenService;
import am.karo.dictionary.security.service.UserDetailsImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;

    public AuthController(
            AuthenticationManager authenticationManager,
            UserRepository userRepository,
            AuthorityRepository authorityRepository,
            PasswordEncoder encoder,
            JwtUtils jwtUtils,
            RefreshTokenService refreshTokenService
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String jwt = jwtUtils.generateJwtToken(userDetails);

        List<String> authorities = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

        return ResponseEntity.ok(new JwtResponse(jwt, refreshToken.getToken(), userDetails.getId(),
                userDetails.getUsername(), userDetails.getEmail(), authorities));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        User user = new User(signUpRequest.getName(), signUpRequest.getSurname(),signUpRequest.getUsername(), signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strAuthorities = signUpRequest.getAuthorities();
        Set<Authority> authorities = new HashSet<>();

        if (strAuthorities == null) {
            Authority userAuthority = authorityRepository.findByName(Authorities.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Authority is not found."));
            authorities.add(userAuthority);
        } else {
            strAuthorities.forEach(authority -> {
                switch (authority) {
                    case "admin":
                        Authority adminAuthority = authorityRepository.findByName(Authorities.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Authority is not found."));
                        authorities.add(adminAuthority);

                        break;
                    case "mod":
                        Authority modAuthority = authorityRepository.findByName(Authorities.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Authority is not found."));
                        authorities.add(modAuthority);

                        break;
                    default:
                        Authority userAuthority = authorityRepository.findByName(Authorities.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Authority is not found."));
                        authorities.add(userAuthority);
                }
            });
        }

        user.setAuthorities(authorities);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtUtils.generateTokenFromUsername(user.getUsername());
                    return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Refresh token is not in database!"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(@Valid @RequestBody LogOutRequest logOutRequest) {
        refreshTokenService.deleteByUserId(logOutRequest.getUserId());
        return ResponseEntity.ok(new MessageResponse("Log out successful!"));
    }

}
