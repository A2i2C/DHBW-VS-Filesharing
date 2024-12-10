package filesharing.userhandler.controller;

import filesharing.userhandler.dto.TwoUserDto;
import filesharing.userhandler.dto.UserDto;
import filesharing.userhandler.dto.UsernameDto;
import filesharing.userhandler.model.MyUser;
import filesharing.userhandler.model.MyUserRepository;
import filesharing.userhandler.model.UserCommunication;
import filesharing.userhandler.model.UserCommunicationRepository;
import filesharing.userhandler.service.ConversationService;
import filesharing.userhandler.service.FileHandlerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private ConversationService conversationService;

    @Autowired
    private FileHandlerService fileHandlerService;

    @Autowired
    private UserCommunicationRepository userCommunicationRepository;

    @Autowired
    private MyUserRepository myUserRepository;

    @PostMapping("/partner")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<List<UserDto>> getPersons(@RequestBody UsernameDto request) {
        List<UserDto> persons = conversationService.getPersonsByUsername(request.username());
        return ResponseEntity.ok(persons);
    }

    @PostMapping("/partner/add")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<Map<String, String>> addPerson(@RequestBody TwoUserDto request) {
        String currentUser = request.username_1();
        String partnerName = request.username_2();
        System.out.println("Received request to add partner: " + partnerName);

        // Generate bucket name (sorted to ensure consistency)
        String bucketName = createConsistentBucketName(currentUser, partnerName);

        // Check if communication already exists
        boolean communicationExists = userCommunicationRepository.existsByUser1UsernameAndUser2UsernameOrUser1UsernameAndUser2Username(
                currentUser, partnerName, partnerName, currentUser);

        System.out.println(communicationExists);

        if (communicationExists) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "UserCommunication entry already exists between: " + currentUser + " and " + partnerName));
        }

        // Create the bucket
        try {
            fileHandlerService.createBucket(bucketName);

            // Retrieve user entities
            MyUser user1 = myUserRepository.findByUsername(currentUser)
                    .orElseThrow(() -> new RuntimeException("User not found: " + currentUser));
            MyUser user2 = myUserRepository.findByUsername(partnerName)
                    .orElseThrow(() -> new RuntimeException("User not found: " + partnerName));

            // Create a new UserCommunication entry
            UserCommunication userCommunication = new UserCommunication();
            userCommunication.setUser1(user1);
            userCommunication.setUser2(user2);
            userCommunicationRepository.save(userCommunication);

            return ResponseEntity.ok(Map.of("message", "Bucket created successfully", "bucketName", bucketName));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to create bucket: " + bucketName));
        }
    }

    // Helper method to generate a consistent bucket name
    private String createConsistentBucketName(String user1, String user2) {
        return (user1.compareTo(user2) < 0 ? user1 + "+" + user2 : user2 + "+" + user1);
    }

}
