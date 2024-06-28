package ru.manakas.study.java_bot.Repository;

import org.springframework.data.repository.CrudRepository;
import ru.manakas.study.java_bot.Model.UserRole;

public interface UserRoleRepository extends CrudRepository<UserRole, Long> {
}
