package lets.songko.users.controller;

import lets.songko.users.entity.Users;
import lets.songko.users.service.UsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Log4j2
@RequiredArgsConstructor
@RestController
public class UsersController {
    private final UsersService usersService;

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/users")
    public Mono<Users> getUsers(@PathVariable("userId") String userId) {
        return this.usersService.findByUserId(userId);
    }
}
