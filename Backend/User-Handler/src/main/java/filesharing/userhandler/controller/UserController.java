package filesharing.userhandler.controller;

import filesharing.userhandler.dto.TwoUserDto;
import filesharing.userhandler.dto.UserCommunicationDto;
import filesharing.userhandler.dto.UsernameDto;
import filesharing.userhandler.model.MyUser;
import filesharing.userhandler.repository.MyUserRepository;
import filesharing.userhandler.model.UserCommunication;
import filesharing.userhandler.repository.UserCommunicationRepository;
import filesharing.userhandler.service.UserCommunicationService;
import filesharing.userhandler.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@CrossOrigin("*")
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private UserCommunicationService userCommunicationService;

    @Autowired
    private FileService fileService;

    @Autowired
    private UserCommunicationRepository userCommunicationRepository;

    @Autowired
    private MyUserRepository myUserRepository;

    @PostMapping("/partner")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<List<UserCommunicationDto>> getPersons(@RequestBody UsernameDto request) {
        List<UserCommunicationDto> persons = userCommunicationService.getPersonsByUsername(request.username());
        return ResponseEntity.ok(persons);
    }

    @PostMapping("/partner/add")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<Map<String, String>> addPerson(@RequestBody TwoUserDto request) {
        String currentUser = request.username_1();
        String partnerName = request.username_2();
        log.info("Received request to add partner: " + partnerName);

        // Generate bucket name (sorted to ensure consistency)
        String bucketName = createConsistentBucketName(currentUser, partnerName);

        // Check if communication already exists (bidirectional check as bucket name is consistent and always starts with the user with the lexicographically smaller username)
        boolean communicationExists = userCommunicationRepository.existsByUser1UsernameAndUser2UsernameOrUser1UsernameAndUser2Username(
                currentUser, partnerName, partnerName, currentUser);

        if (communicationExists) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "UserCommunication entry already exists between: " + currentUser + " and " + partnerName));
        }

        try {
            fileService.createBucket(bucketName);

            // Retrieve user entities
            MyUser user1 = myUserRepository.findByUsername(currentUser)
                    .orElseThrow(() -> new RuntimeException("User not found: " + currentUser));
            MyUser user2 = myUserRepository.findByUsername(partnerName)
                    .orElseThrow(() -> new RuntimeException("User not found: " + partnerName));

            // Create a new UserCommunication entry
            UserCommunication userCommunication = new UserCommunication();
            userCommunication.setUser1(user1);
            userCommunication.setUser2(user2);
            userCommunication.setBucketname(bucketName);
            userCommunicationRepository.save(userCommunication);

            log.info("UserCommunication entry created between {} and {}: ", currentUser,  partnerName);
            return ResponseEntity.ok(Map.of("message", "Bucket created successfully", "bucketName", bucketName));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to create bucket: " + bucketName));
        }
    }

    // Helper method to generate a consistent bucket name
    private String createConsistentBucketName(String user1, String user2) {
        return (user1.compareTo(user2) < 0 ? user1 + "-" + user2 + "-bucket" : user2 + "-" + user1 + "-bucket");
    }

}
