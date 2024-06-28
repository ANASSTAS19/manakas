package ru.manakas.study.java_bot.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.manakas.study.java_bot.Exception.UsernameAlreadyExistsException;
import ru.manakas.study.java_bot.Model.User;
import ru.manakas.study.java_bot.Model.UserAuthority;
import ru.manakas.study.java_bot.Model.UserRole;
import ru.manakas.study.java_bot.Repository.UserRepository;
import ru.manakas.study.java_bot.Repository.UserRoleRepository;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserDetailsService, UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRoleRepository userRoleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    @Override
    public void registration(String username, String password) {
        if (userRepository.findByUsername(username).isEmpty()) {
            User user = userRepository.save(
                    new User()
                            .setId(null)
                            .setUsername(username)
                            .setPassword(passwordEncoder.encode(password))
                            .setLocked(false)
                            .setExpired(false)
                            .setEnabled(true)
            );
            userRoleRepository.save(new UserRole(null, UserAuthority.USER, user));
        } else {
            throw new UsernameAlreadyExistsException();
        }
    }

    @Override
    public void changeUserRole(Long userId, UserAuthority newAuthority) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        UserRole userRole = user.getUserRoles().get(0); // предполагаем, что у пользователя всегда одна роль
        userRole.setUserAuthority(newAuthority);

        userRepository.save(user);
    }

    @Override
    public List<UserAuthority> getUserRoles(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        return user.getUserRoles().stream().map(UserRole::getUserAuthority).collect(Collectors.toList());
    }
}
