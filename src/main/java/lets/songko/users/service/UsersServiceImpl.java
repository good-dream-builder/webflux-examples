package lets.songko.users.service;

import lets.songko.users.entity.Users;
import lets.songko.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Log4j2
@RequiredArgsConstructor
@Service
public class UsersServiceImpl implements UsersService {
    private final UsersRepository usersRepository;

    public Mono<Users> findByUserId(String userId) {
        return this.usersRepository.findByUserId(userId);
    }
}
