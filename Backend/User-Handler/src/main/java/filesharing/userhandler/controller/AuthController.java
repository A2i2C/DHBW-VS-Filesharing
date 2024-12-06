package filesharing.userhandler.controller;

import filesharing.userhandler.model.MyUser;
import filesharing.userhandler.model.MyUserRepository;
import filesharing.userhandler.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@CrossOrigin("*")
public class AuthController {
    @Autowired
    private MyUserRepository myUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping(value = "/signup", consumes = "application/json")
    public ResponseEntity<MyUser>  createUser(@RequestBody MyUser user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        MyUser savedUser = myUserRepository.save(user);
        return ResponseEntity.ok(savedUser);
    }

    @PostMapping(value = "/login", consumes = "application/json")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody MyUser user) {
        Optional<MyUser> trueUser = myUserRepository.findByUsername(user.getUsername());
        if (trueUser.isPresent() && passwordEncoder.matches(user.getPassword(), trueUser.get().getPassword())) {
            String token = jwtUtil.generateToken(user.getUsername());
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("username", user.getUsername());
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(401).body(Collections.singletonMap("error", "Invalid credentials"));
    }
}
