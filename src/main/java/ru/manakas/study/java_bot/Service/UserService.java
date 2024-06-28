package ru.manakas.study.java_bot.Service;

import ru.manakas.study.java_bot.Model.UserAuthority;

import java.util.List;

public interface UserService {

    void registration(String username, String password);

    void changeUserRole(Long userId, UserAuthority newAuthority);

    List<UserAuthority> getUserRoles(Long userId);

}
