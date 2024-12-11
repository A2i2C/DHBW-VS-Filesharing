package com.example.filehandler.model;

import com.example.filehandler.repository.FileDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // This annotation is used to indicate that the class provides the mechanism for storage, retrieval, search, update and delete operation on objects.
public interface FileDetailsRepository extends JpaRepository<FileDetails, Integer> {
}
