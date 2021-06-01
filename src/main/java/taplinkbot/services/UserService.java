package taplinkbot.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import taplinkbot.entities.User;
import taplinkbot.repositories.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return user;
    }

    public boolean registerUser(User user) {
        User userFromDB = userRepository.findByUsername(user.getUsername());

        if (userFromDB != null) {
            return false;
        }

        //@Todo
        //user.setRoles(Collections.singleton(new Role(1L, "USER_ROLE")));

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        userRepository.save(user);

        return true;
    }

    /**
     * Возвращает текущего авторизованного пользователя.
     *
     * @return User текущий авторизованный пользователь или пустой объект User.
     */
    public User getCurrentUser() {
        Object object = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        if (object instanceof User) {
            return (User) object;
        } else {
            return new User();
        }
    }
}
