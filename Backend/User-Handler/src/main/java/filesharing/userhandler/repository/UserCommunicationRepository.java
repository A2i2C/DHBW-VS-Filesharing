package filesharing.userhandler.repository;

import filesharing.userhandler.dto.UserCommunicationDto;
import filesharing.userhandler.model.UserCommunication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserCommunicationRepository extends JpaRepository<UserCommunication, Long> {

    // Query to find all the users that have a filechat with the user with the given userId
    @Query("SELECT new filesharing.userhandler.dto.UserCommunicationDto(uc.user2.userId, uc.user2.username, uc.bucketname) " +
            "FROM UserCommunication uc WHERE uc.user1.userId = :userId " +
            "UNION " +
            "SELECT new filesharing.userhandler.dto.UserCommunicationDto(uc.user1.userId, uc.user1.username, uc.bucketname) " +
            "FROM UserCommunication uc WHERE uc.user2.userId = :userId")
    List<UserCommunicationDto> findPersonsByUserId(@Param("userId") Long userId);

    // Query to check if a filechat between two users already exists
    boolean existsByUser1UsernameAndUser2UsernameOrUser1UsernameAndUser2Username(String user1, String user2, String user2Reversed, String user1Reversed);
}
