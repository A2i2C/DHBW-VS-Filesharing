package com.example.filehandler.repository;

import com.example.filehandler.model.FileDetails;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FileDetailsRepository extends JpaRepository<FileDetails, Integer> {

    @Query("SELECT userId FROM FileDetails WHERE filename = ?1")
    Long findUserIdByFilename(String filename);

    //Find shard in which file is stored
    @Query("SELECT shardeins FROM FileDetails WHERE filename = ?1")
    boolean findShardEinsByFilename(String filename);

    //Check if file is already in Bucket and from same User
    @Query("SELECT fileId FROM FileDetails WHERE filename = ?1 AND bucketname = ?2 AND userId = ?3")
    String findFilenameByFilenameAndBucketname(String filename, String bucketname, Long userId);

    //If file is arleady in Bucket, update the file to new File from fileID
    @Transactional
    void UpdateFileDetailsFromFileId(Long fileId);

    //Get FileId to be deleted via filename and userId
    @Query("SELECT fileId FROM FileDetails WHERE filename = ?1 AND userId = ?2")
    Long findFileIdByFilenameAndUserId(String filename, Long userId);

    @Transactional
    void deletebyFileId(Long fileId);
}
