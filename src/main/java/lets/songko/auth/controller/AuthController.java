package lets.songko.auth.controller;

import lets.songko.auth.dto.LoginRequest;
import lets.songko.auth.dto.LoginResponse;
import lets.songko.auth.dto.SignUpRequest;
import lets.songko.auth.dto.SingUpResponse;
import lets.songko.auth.service.AuthService;
import lets.songko.common.dto.CResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public Mono<CResponse<LoginResponse>> login(@RequestBody LoginRequest request) {
        return this.authService.login(request)
                .map(response -> new CResponse<>(response, true, "Login successful"))
                .onErrorResume(e -> Mono.just(new CResponse<>(null, false, e.getMessage())));
    }

    @PostMapping("/signUp")
    public Mono<CResponse<SingUpResponse>> signUp(@RequestBody SignUpRequest request) {
        return this.authService.signUp(request)
                .map(response -> new CResponse<>(response, true, "User signed up successfully"))
                .onErrorResume(e -> Mono.just(new CResponse<>(null, false, e.getMessage())));
    }
}
