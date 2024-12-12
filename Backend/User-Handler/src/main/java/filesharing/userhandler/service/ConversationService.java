package filesharing.userhandler.service;

import filesharing.userhandler.dto.UserDto;
import filesharing.userhandler.model.MyUser;
import filesharing.userhandler.repository.MyUserRepository;
import filesharing.userhandler.repository.UserCommunicationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
@Slf4j
@Service
public class ConversationService {
    @Autowired
    private UserCommunicationRepository conversationRepository;

    @Autowired
    private MyUserRepository MyUserRepository;

    public List<UserDto> getPersonsByUsername(String username) {
        MyUser myUser = MyUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Fetch related persons by user ID
        return conversationRepository.findPersonsByUserId(myUser.getUserId());
    }
}
