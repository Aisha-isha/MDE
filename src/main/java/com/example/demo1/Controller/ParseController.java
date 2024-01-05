package com.example.demo1.Controller;

import org.eclipse.epsilon.emc.emf.InMemoryEmfModel;
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
import com.example.demo1.service.Model2Model;
import com.example.demo1.service.Model2Text;
import com.example.demo1.service.ParserService;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;

@Controller
public class ParseController {

    @Autowired
    private Model2Model model2Model;
    @Autowired
    private Model2Text model2Text;

    @GetMapping("/uploadFiles")
    public String parse() {
        return "index";
    }

    private final ParserService parserService;

    @Autowired
    public ParseController(ParserService parserService) {
        this.parserService = parserService;
    }

    @PostMapping("/uploadFiles")
    public ResponseEntity<FileSystemResource> uploadFiles(
            @RequestParam("files") MultipartFile[] files,
            @RequestParam("numFiles") int numFiles,
            Model model) throws Exception {
        String uploadDir = "src/main/resources/static/uploads";

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
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("message", "Error uploading files. Please try again.");
        }

        parserService.parsePropretiesFile();
        parserService.parseDockerFile(numFiles);
        parserService.confFile();

        InMemoryEmfModel targetModel = model2Model.model2ModelTransformation("static/uploads/config.flexmi");
        String generatedConfigFile = model2Text.model2TextTransformer(targetModel);

        String filePath = "src/main/resources/static/uploads/docker-compose.yml";

        try (FileWriter fileWriter = new FileWriter(filePath);
             PrintWriter printWriter = new PrintWriter(fileWriter)) {

            printWriter.println(generatedConfigFile);

            System.out.println("File docker-compose created successfully at: " + filePath);

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error creating docker-compose file.");
            model.addAttribute("message", "Error creating docker-compose file. Please try again.");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Generate a downloadable file
        Path resultFilePath = Path.of(filePath);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "docker-compose.yml");

        return new ResponseEntity<>(new FileSystemResource(resultFilePath), headers, HttpStatus.OK);
    }
}
