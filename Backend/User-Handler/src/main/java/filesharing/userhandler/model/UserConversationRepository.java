package filesharing.userhandler.model;

import filesharing.userhandler.dto.UserDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserConversationRepository extends JpaRepository<UserCommunication, Integer> {
    @Query("SELECT new filesharing.userhandler.dto.UserDto(u.user_id, u.username) " +
            "FROM MyUser u " +
            "JOIN UserCommunication uc ON (uc.userId = :userId AND uc.userId2 = u.user_id) " +
            "OR (uc.userId2 = :user_id AND uc.userId = u.user_id)")

    List<UserDto> findPersonsByUserId(@Param("user_id") int user_id);
}
