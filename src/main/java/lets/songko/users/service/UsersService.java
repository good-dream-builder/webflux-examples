package lets.songko.users.service;

import lets.songko.users.entity.Users;
import reactor.core.publisher.Mono;


public interface UsersService {
    Mono<Users> findByUserId(String userId);
}
