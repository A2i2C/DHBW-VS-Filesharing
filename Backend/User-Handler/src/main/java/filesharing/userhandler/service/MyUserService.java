package filesharing.userhandler.service;

import filesharing.userhandler.model.MyUser;
import filesharing.userhandler.repository.MyUserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class MyUserService implements UserDetailsService {
    @Autowired
    private MyUserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // get the user from the database and return it as a UserDetails object
        Optional<MyUser> user = repository.findByUsername(username);
        if (user.isPresent()) {
            var userObj = user.get();
            log.info("User found: {}", userObj);
            return User.builder()
                    .username(userObj.getUsername())
                    .password(userObj.getPassword())
                    .build();
        } else{
            log.error("User not found: {}", username);
            throw new UsernameNotFoundException(username);
        }
    }
}
