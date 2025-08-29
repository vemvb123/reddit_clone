package com.example.Reddit.clone.Services;


import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

@Service
public class FileService {


    public static final String pathToSaveImages = "C:\\Users\\Greencom\\Pictures\\wg\\reddit_clone\\";

    public byte[] downloadFile(String fileName) throws IOException {
        String filepath = pathToSaveImages + fileName;
        Path path = Paths.get(filepath);
        byte[] fileContent = Files.readAllBytes(path);

        return fileContent;

    }


    //Todo: ensure saved file has an original name
    public static String makeOriginalFileName(String filepath) {
        while (true)
        {
            if (!new File(filepath).exists())
                return filepath;


            String filetype = null;
            if (filepath.contains(".jpg"))
                filetype = ".jpg";
            else if (filepath.contains(".png"))
                filetype = ".png";
            else if (filepath.contains(".webm"))
                filetype = ".webm";
            else if (filepath.contains(".gif"))
                filetype = ".gif";
            else
                return filetype; //filetype not recognized

            Random random = new Random();
            int randomNum = random.nextInt(10);

            filepath = filepath.replace(filetype, "");
            filepath = filepath + randomNum + filetype;
        }
    }
}
