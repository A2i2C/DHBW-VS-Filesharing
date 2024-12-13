package com.example.filehandler.repository;

import com.example.filehandler.model.FileDetails;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FileDetailsRepository extends JpaRepository<FileDetails, Integer> {

    @Query("SELECT shardeins FROM FileDetails WHERE filename = ?1")
    boolean findShardEinsByFilename(String filename);

    @Query("SELECT filename FROM FileDetails WHERE filename = ?1 AND bucketname = ?2")
    String findFilenameByFilenameAndBucketname(String filename, String bucketname);

    //Get FileId via filename
    @Query("SELECT fileId FROM FileDetails WHERE filename = ?1")
    Long findFileIdByFilename(String filename);

    //Get FileId to be deleted via filename and userId
    @Query("SELECT fd.fileId FROM FileDetails fd JOIN FileUser fu ON fd.fileId = fu.file_id_user WHERE fd.filename = ?1 AND fu.userId = ?2 AND fd.bucketname = ?3")
    Long findFileIdByFilenameAndUserId(String filename, Long userId, String bucketname);

    @Transactional
    void deleteByFileId(Long fileId);
}