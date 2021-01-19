package start.find.dir;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static start.controllers.rest.UserRESTController.uploadDirectory;

public class BuildFileDir {
    public static StringBuilder getFileName(MultipartFile[] files) {
        StringBuilder fileNames = new StringBuilder();

        for (MultipartFile file : files) {
            Path fileNameAndPath = Paths.get(uploadDirectory, file.getOriginalFilename());
            fileNames.append(file.getOriginalFilename()).append(" ");
            try {
                Files.write(fileNameAndPath, file.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return fileNames;
    }

}
