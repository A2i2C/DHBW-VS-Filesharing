package filesharing.userhandler.model;

import filesharing.userhandler.dto.UserDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserCommunicationRepository extends JpaRepository<UserCommunication, Long> {
    @Query("SELECT new filesharing.userhandler.dto.UserDto(uc.user2.userId, uc.user2.username, uc.user2.password) " +
            "FROM UserCommunication uc " +
            "WHERE uc.user1.userId = :userId")
    List<UserDto> findPersonsByUserId(@Param("userId") Long userId);

    boolean existsByUser1UsernameAndUser2UsernameOrUser1UsernameAndUser2Username(String user1, String user2, String user2Reversed, String user1Reversed);
}
