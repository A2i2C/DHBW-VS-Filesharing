package com.example.filehandler.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "FileDetails")
public class FileDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long fileId;
    private String bucketname;
    private String filename;
    private boolean shardeins;
    private boolean shardzwei;
    private int yearweek;
}
