package com.example.filehandler.repository;

import com.example.filehandler.model.FileUser;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FileUserRepository extends JpaRepository<FileUser, Integer> {

    //Get UserId via fileId
    @Query("SELECT userId FROM FileUser WHERE file_id_user = ?1")
    Long findUserIdByFileId(Long fileId);

    @Transactional
    void deleteByUserId(Long userId);
}
