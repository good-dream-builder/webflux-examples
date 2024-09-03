package lets.songko.auth.jwt;

import org.springframework.security.core.AuthenticationException;

class JwtAuthenticationException extends AuthenticationException {
    JwtAuthenticationException(String msg) {
        super(msg);
    }
}
