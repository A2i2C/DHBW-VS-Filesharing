package com.example.filehandler.dto;

import org.springframework.web.multipart.MultipartFile;

public record FileHandlerFileRequest(MultipartFile file, String model, String language) {
}
