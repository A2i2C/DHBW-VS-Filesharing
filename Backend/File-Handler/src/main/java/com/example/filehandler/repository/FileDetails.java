package com.example.filehandler.repository;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class FileDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long file_id;
    private String filename;
    private boolean shardeins;
    private boolean shardzwei;
    private int user_id;
}
