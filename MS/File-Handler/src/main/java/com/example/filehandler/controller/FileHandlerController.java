package com.example.filehandler.controller;

import com.example.filehandler.service.FileHandlerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/filehandler")
@RequiredArgsConstructor
public class FileHandlerController {

    private final FileHandlerService fileHandlerService;

    @PostMapping("/upload")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void sendAudio(/*@RequestPart("file") MultipartFile file*/) {
        //FileHandlerFileRequest audioRequest = new FileHandlerFileRequest(file, "whisper-1", "de");
        fileHandlerService.fileUpload();  //
    }

}
