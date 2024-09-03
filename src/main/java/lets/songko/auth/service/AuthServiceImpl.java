package lets.songko.auth.service;

import lets.songko.auth.dto.LoginRequest;
import lets.songko.auth.dto.LoginResponse;
import lets.songko.auth.dto.SignUpRequest;
import lets.songko.auth.dto.SingUpResponse;
import lets.songko.auth.jwt.TokenProvider;
import lets.songko.auth.repository.AuthRepository;
import lets.songko.users.entity.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {
    private final PasswordEncoder passwordEncoder;  // BCryptPasswordEncoder
    private final TokenProvider tokenProvider;      // JwtService
    private final AuthRepository authRepository;

    @Override
    public Mono<LoginResponse> login(LoginRequest request) {
        return authRepository.findByUserId(request.userId())
                .filter(user -> passwordEncoder.matches(request.userPassword(), user.getPassword()))
                .map(tokenProvider::generateToken)
                .map(LoginResponse::new)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED)));
    }

    @Override
    public Mono<SingUpResponse> signUp(SignUpRequest request) {
        Users newUser = Users.builder()
                .userId(request.userId())
                .userPassword(passwordEncoder.encode(request.userPassword()))
                .enabled(true) // 예시로 활성화 상태를 true로 설정
                .build();

        return authRepository.findByUserId(request.userId())
                .flatMap(existingUser -> Mono.error(new RuntimeException("User already exists")))
                .switchIfEmpty(Mono.defer(() -> authRepository.save(newUser)))
                .map(savedUser -> new SingUpResponse("mock-token-for-" + ((Users) savedUser).getUserId()));
    }
}
