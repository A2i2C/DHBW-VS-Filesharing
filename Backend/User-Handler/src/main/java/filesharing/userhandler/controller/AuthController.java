package filesharing.userhandler.controller;

import filesharing.userhandler.model.MyUser;
import filesharing.userhandler.model.MyUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@CrossOrigin("*")
public class AuthController {
    @Autowired
    private MyUserRepository myUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping(value = "/signup", consumes = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public MyUser createUser(@RequestBody MyUser user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return myUserRepository.save(user);
    }

    @PostMapping(value = "/login", consumes = "application/json")
    public HttpStatus loginUser(@RequestBody MyUser user) {
        Optional<MyUser> trueUser = myUserRepository.findByUsername(user.getUsername());
        if (trueUser.isPresent()) {
            if (passwordEncoder.matches(user.getPassword(), trueUser.get().getPassword())) {
                return HttpStatus.OK;
            }
        }
        return HttpStatus.UNAUTHORIZED;
    }
}
