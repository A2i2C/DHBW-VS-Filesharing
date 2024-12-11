package filesharing.userhandler.repository;

import java.util.Optional;

import filesharing.userhandler.model.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MyUserRepository extends JpaRepository<MyUser, Long>{
    Optional<MyUser> findByUsername(String username);
}