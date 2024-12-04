package filesharing.userhandler.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api")
@CrossOrigin("*")
public class ContentController {
    @GetMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public String login(){
        return "login";
    }

    @GetMapping("/signup")
    @ResponseStatus(HttpStatus.OK)
    public String signup(){
        return "signup";
    }

    @GetMapping("/index")
    @ResponseStatus(HttpStatus.OK)
    public String home(){
        return "index";
    }
}
