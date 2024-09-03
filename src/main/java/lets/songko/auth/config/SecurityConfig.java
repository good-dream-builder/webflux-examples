package lets.songko.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Configuration
@EnableReactiveMethodSecurity
public class SecurityConfig {
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http,
                                                         ReactiveAuthenticationManager authManager,
                                                         ServerAuthenticationConverter authConverter) {

        AuthenticationWebFilter authenticationWebFilter = new AuthenticationWebFilter(authManager);
        authenticationWebFilter.setServerAuthenticationConverter(authConverter);

        return http
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers(HttpMethod.POST, "/login").permitAll()
                        .pathMatchers(HttpMethod.POST, "/signUp").permitAll()
                        .pathMatchers("/swagger-ui.html", "/webjars/**", "/v3/api-docs/**").permitAll()
                        .anyExchange().authenticated()
                )
                .addFilterAt(authenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(ServerHttpSecurity.CorsSpec::disable)
                .build();

        //        return http.authorizeExchange()
//                .pathMatchers(HttpMethod.GET, "/hello")
//                .permitAll()
//                .anyExchange()
//                .authenticated()
//                .and().httpBasic()
//                .and().build();


//        return http.authorizeExchange()
//                .anyExchange()
//                .access(this::getAuthorizationDecisionMono)
//                .and().httpBasic()
//                .and().build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    private Mono<AuthorizationDecision> getAuthorizationDecisionMono(Mono<Authentication> authentication,
                                                                     AuthorizationContext context) {
        String path = this.getRequestPath(context); // 경로 추출

        if (path.equals("/hello")) {
            return authentication
                    .map(isAdmin())
                    .map(AuthorizationDecision::new);
        }
        return Mono.just(new AuthorizationDecision(false));
    }

    /**
     * @param context
     * @return 요청의 경로
     */
    private String getRequestPath(AuthorizationContext context) {
        return context.getExchange()
                .getRequest()
                .getPath()
                .toString();
    }

    /**
     * @return Authentication 인스턴스에 ROLE_ADMIN 특성이 있으면 true를 반환
     */
    private Function<Authentication, Boolean> isAdmin() {
        return authentication -> authentication.getAuthorities()
                .stream()
                .anyMatch(e -> e.getAuthority()
                        .equals("ROLE_ADMIN"));
    }
}
