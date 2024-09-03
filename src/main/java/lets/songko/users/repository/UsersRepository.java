package lets.songko.users.repository;


import lets.songko.users.entity.Users;
import org.bson.types.ObjectId;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UsersRepository extends ReactiveCrudRepository<Users, ObjectId> {
    Mono<Users> findByUserId(String userId);
}
