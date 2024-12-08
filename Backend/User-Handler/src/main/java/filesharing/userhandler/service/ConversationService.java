package filesharing.userhandler.service;

import filesharing.userhandler.dto.UserDto;
import filesharing.userhandler.model.MyUser;
import filesharing.userhandler.model.MyUserRepository;
import filesharing.userhandler.model.UserConversationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConversationService {
    @Autowired
    private UserConversationRepository conversationRepository;

    @Autowired
    private MyUserRepository MyUserRepository;

    public List<UserDto> getPersonsByUsername(String username) {
        MyUser myUser = MyUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Fetch related persons by user ID
        return conversationRepository.findPersonsByUserId(myUser.getUser_id())
                .stream()
                .map(user -> new UserDto(user.userId(), user.username()))
                .toList();
    }
}
