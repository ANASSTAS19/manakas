package ru.manakas.study.java_bot.Repository;

import org.springframework.data.repository.CrudRepository;
import ru.manakas.study.java_bot.Model.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
