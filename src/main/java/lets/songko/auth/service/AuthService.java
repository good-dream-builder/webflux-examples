package lets.songko.auth.service;

import lets.songko.auth.dto.LoginRequest;
import lets.songko.auth.dto.LoginResponse;
import lets.songko.auth.dto.SignUpRequest;
import lets.songko.auth.dto.SingUpResponse;
import reactor.core.publisher.Mono;


public interface AuthService {
    Mono<LoginResponse> login(LoginRequest request);

    Mono<SingUpResponse> signUp(SignUpRequest request);
}
