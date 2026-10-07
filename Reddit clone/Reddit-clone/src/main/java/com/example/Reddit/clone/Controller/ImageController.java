package com.example.Reddit.clone.Controller;


import com.example.Reddit.clone.ACL.CanPartakeInCommunity;
import com.example.Reddit.clone.ACL.OwnerCheck;
import com.example.Reddit.clone.Services.CommunityService;
import com.example.Reddit.clone.Services.FileService;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/image")
@EnableAutoConfiguration
@AllArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ImageController {

    final String origin = "http://localhost:3000";
    private CommunityService communityService;
    private FileService fileService;
    private OwnerCheck ownerCheck;
    private CanPartakeInCommunity canPartakeInCommunity;

    @GetMapping("/{fileName}")
    public ResponseEntity<byte[]> downloadFile(
            @PathVariable String fileName
    ) throws IOException {
        byte[] fileContent = fileService.downloadFile(fileName);

        HttpHeaders headers = new HttpHeaders();
        if (fileName.contains(".jpg"))
            headers.setContentType(MediaType.IMAGE_JPEG);
        else if (fileName.contains(".png"))
            headers.setContentType(MediaType.IMAGE_PNG);
        else if (fileName.contains(".webm"))
            headers.setContentType(MediaType.parseMediaType("video/webm"));
        else if (fileName.contains(".gif"))
            headers.setContentType(MediaType.IMAGE_GIF);


        headers.setContentLength(fileContent.length);

        return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
    }



}
