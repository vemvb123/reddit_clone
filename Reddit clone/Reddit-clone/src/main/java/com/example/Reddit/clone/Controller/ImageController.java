package com.example.Reddit.clone.Controller;


import com.example.Reddit.clone.Services.CommunityService;
import com.example.Reddit.clone.Services.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.bind.annotation.*;
import com.example.Reddit.clone.ACL.CanPartakeInCommunity;
import com.example.Reddit.clone.ACL.OwnerCheck;
import java.io.IOException;

@RestController
@RequestMapping("/image")
@EnableAutoConfiguration
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ImageController {



    final String origin = "http://localhost:3000";
    @Autowired
    private CommunityService communityService;

    @Autowired
    private FileService fileService;
    @Autowired
    private OwnerCheck ownerCheck;

    @Autowired
    private CanPartakeInCommunity canPartakeInCommunity;

    @CrossOrigin(origins = origin)
    @GetMapping("/get_file_by_filename/{fileName}")
    public ResponseEntity<byte[]> downloadFile(
            @PathVariable String fileName
    ) throws IOException {
        System.out.println("herigetfilename");
        System.out.println(fileName);


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
