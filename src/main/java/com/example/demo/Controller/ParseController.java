package com.example.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.example.demo.service.ParserService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;

@Controller
public class ParseController {

    private final ParserService parserService;

    @Autowired
    public ParseController(ParserService parserService) {
        this.parserService = parserService;
    }

    @GetMapping("/uploadFiles")
    public String parse() {
        return "index";
    }

    @PostMapping("/uploadFiles")
    public ResponseEntity<FileSystemResource> uploadFiles(@RequestParam("files") MultipartFile[] files, @RequestParam("numFiles") int numFiles, Model model) {
        String uploadDir = "src/main/resources/static/uploads"; // Update the uploadDir path

        try {
            Path uploadPath = Path.of(uploadDir);
            Files.createDirectories(uploadPath);

            for (int i = 0; i < files.length; i++) {
                MultipartFile file = files[i];

                if (file.isEmpty()) {
                    continue;
                }

                String fileName = file.getOriginalFilename();
                Path filePath = uploadPath.resolve(fileName);

                // If a file with the same name already exists, rename the current file
                int count = 0;
                while (Files.exists(filePath)) {
                    count++;
                    int lastIndex = fileName.lastIndexOf('.');
                    String baseFileName = (lastIndex > 0 && lastIndex < fileName.length() - 1) ?
                            fileName.substring(0, lastIndex) : fileName;
                    String fileExtension = (lastIndex > 0 && lastIndex < fileName.length() - 1) ?
                            fileName.substring(lastIndex) : "";
                    fileName = baseFileName + count + fileExtension;
                    filePath = uploadPath.resolve(fileName);
                }

                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            }

            model.addAttribute("message", "Files uploaded successfully!");
            model.addAttribute("filePaths", Arrays.stream(files)
                    .filter(file -> !file.isEmpty())
                    .map(file -> uploadDir + "/" + file.getOriginalFilename())
                    .toArray(String[]::new));

            // Process uploaded files with ParserService
            parserService.parsePropretiesFile();
            parserService.parseDockerFile(numFiles);
            parserService.confFile();

            // Generate a downloadable file (replace this with your actual logic)
            Path resultFilePath = Path.of("src/main/resources/static/uploads/docker-compose.yml");
           
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "docker-compose.yml");

            return new ResponseEntity<>(new FileSystemResource(resultFilePath), headers, org.springframework.http.HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("message", "Error uploading files. Please try again.");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
