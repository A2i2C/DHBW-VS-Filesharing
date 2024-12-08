package filesharing.userhandler.controller;

import filesharing.userhandler.dto.UserDto;
import filesharing.userhandler.dto.UsernameDto;
import filesharing.userhandler.service.ConversationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private ConversationService conversationService;

    @PostMapping("/partner")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<List<UserDto>> getPersons(@RequestBody UsernameDto request) {
        List<UserDto> persons = conversationService.getPersonsByUsername(request.username());
        return ResponseEntity.ok(persons);
    }
}
